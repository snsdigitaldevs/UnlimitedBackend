package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.ChildMediaSet;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.MediaItem;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProductAttribute;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.MediaItemsByLevel;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.MediaSetByLevel;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.PcmProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Service
public class PcmMediaItemsFilterService {
    private static final Logger logger = LoggerFactory.getLogger(PcmMediaItemsFilterService.class);

    public List<MediaItemsByLevel> getMatchedMediaItems(PcmProduct pcmProduct,
                                                        List<MediaSetByLevel> entitlementTokens,
                                                        String productCode) {

        Optional<OrdersProduct> firstMatchedOrderProduct = pcmProduct.getOrdersProducts().stream()
                .filter(ordersProduct -> Objects.equals(ordersProduct.getProduct().getProductCode(), productCode))
                .findFirst();

        if (firstMatchedOrderProduct.isPresent()) {
            Map<String, OrdersProduct> filteredOrdersProductList = new HashMap<>();
            filteredOrdersProductList.put(productCode, firstMatchedOrderProduct.get());
            pcmProduct.setOrdersProducts(singletonList(firstMatchedOrderProduct.get()));
            return filterItemIdsOfAllLevels(entitlementTokens, productCode, filteredOrdersProductList);
        } else if (findMatchedProductInfo(pcmProduct, productCode)) {
            return filterItemIdsOfOneLevel(entitlementTokens, productCode, pcmProduct);
        } else {
            String errorMessage = "No product info found from PCM with matched product code";
            logger.error(errorMessage);
            return new ArrayList<>();
        }
    }

    private List<MediaItemsByLevel> filterItemIdsOfAllLevels(List<MediaSetByLevel> entitlementTokens, String productCode, Map<String, OrdersProduct> ordersProductList) {
        OrdersProduct orderProduct = ordersProductList.get(productCode);

        Optional<List<MediaItemsByLevel>> matchedMediaItemsAllLevelAllOrders = orderProduct.getOrdersProductsAttributes()
                .stream()
                .filter(OrdersProductAttribute::isDownload)
                .map(attribute -> {
                    List<MediaItemsByLevel> matchedMediaItemsByLevel = new ArrayList<>();
                    attribute.getOrdersProductsDownloads()
                            .stream()
                            .peek(download -> {
                                String level = download.getMediaSet().getProduct().getProductsLevel().toString();
                                entitlementTokens.add(new MediaSetByLevel(level, download.getEntitlementToken(), download.getMediaSetId()));
                            })
                            .forEach(downloadInfo -> {
                                List<MediaItem> matchedMediaItems = new ArrayList<>();

                                String levelInDownload = downloadInfo.getMediaSet().getProduct().getProductsLevel().toString();
                                downloadInfo.getMediaSet().getChildMediaSets().stream()
                                        .filter(ChildMediaSet::isLesson)
                                        .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                                        .filter(MediaItem::isLesson)
                                        .forEach(matchedMediaItems::add);
                                matchedMediaItemsByLevel.add(new MediaItemsByLevel(levelInDownload, matchedMediaItems));
                            });
                    return matchedMediaItemsByLevel;
                })
                .reduce((result, matchedMediaItemsAllLevelForOneOrder) -> {
                    result.addAll(matchedMediaItemsAllLevelForOneOrder);
                    return result;
                });

        return matchedMediaItemsAllLevelAllOrders.isPresent() ? matchedMediaItemsAllLevelAllOrders.get() : new ArrayList<>();
    }

    private boolean findMatchedProductInfo(PcmProduct pcmProduct, String productCode) {
        int size = pcmProduct.getOrdersProducts()
                .stream()
                .flatMap(ordersProduct -> ordersProduct.getOrdersProductsAttributes().stream())
                .filter(OrdersProductAttribute::isDownload)
                .flatMap(attribute -> attribute.getOrdersProductsDownloads().stream())
                .filter(download -> download.getMediaSet().getProduct().getProductCode().equals(productCode))
                .collect(toList())
                .size();

        return size > 0;
    }

    private List<MediaItemsByLevel> filterItemIdsOfOneLevel(List<MediaSetByLevel> entitlementTokens,
                                                            String productCode, PcmProduct pcmProduct) {
        List<MediaItemsByLevel> matchedMediaItems = new ArrayList<>();

        OrdersProductAttribute attribute = getMatchedProductAttribute(pcmProduct, productCode);

        if (attribute != null && attribute.getProductsOptions() != null) {
            String level = attribute.getProductsOptions().split(" ")[1];
            List<MediaItem> mediaItemsForOneLevel = new ArrayList<>();

            attribute.getOrdersProductsDownloads()
                    .stream()
                    .peek(download -> {
                        entitlementTokens.add(new MediaSetByLevel(level, download.getEntitlementToken(), download.getMediaSetId()));
                    })
                    .flatMap(downloadInfo -> downloadInfo.getMediaSet().getChildMediaSets().stream())
                    .filter(ChildMediaSet::isLesson)
                    .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                    .filter(MediaItem::isLesson)
                    .forEach(mediaItemsForOneLevel::add);

            matchedMediaItems.add(new MediaItemsByLevel(level, mediaItemsForOneLevel));
        }

        return matchedMediaItems;
    }

    private OrdersProductAttribute getMatchedProductAttribute(PcmProduct pcmProduct, String productCode) {
        List<OrdersProductAttribute> matchedProductAttribute = new ArrayList<>();
        Map<String, OrdersProduct> filteredOrdersProductList = new HashMap<>();

        pcmProduct.getOrdersProducts().forEach((ordersProduct) -> {
            List<OrdersProductAttribute> matchedAttributes = ordersProduct.getOrdersProductsAttributes().stream()
                    .filter(OrdersProductAttribute::isDownload)
                    .filter(attribute -> attribute.getOrdersProductsDownloads()
                            .stream()
                            .anyMatch(download -> download.getMediaSet().getProduct().getProductCode().equals(productCode)))
                    .collect(toList());

            if (matchedAttributes.size() > 0) {
                matchedProductAttribute.add(matchedAttributes.get(0));
                filteredOrdersProductList.put(ordersProduct.getProduct().getProductCode(), ordersProduct);
            }
        });

        pcmProduct.setOrdersProducts(new ArrayList<>(filteredOrdersProductList.values()));
        return matchedProductAttribute.size() > 0 ? matchedProductAttribute.get(0) : null;
    }
}
