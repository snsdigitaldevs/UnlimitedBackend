package com.simonschuster.pimsleur.unlimited.mapper.productInfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomersOrder;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.MediaItem;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.ProductInfoFromPCM;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCourseInfoService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductInfoMapper {
    public Map<String, List<Integer>> getMediaItemInfo(ProductInfoFromPCM productInfoFromPCM) {

        return productInfoFromPCM.getOrderProduct().getOrdersProductsAttributes()
                .stream()
                .filter(attribute -> attribute.getProductsOptions().contains("Download"))
                .map(attribute -> {
                    String level = attribute.getProductsOptions().split(" ")[1];
                    List<Integer> itemIds = attribute.getOrdersProductsDownloads()
                            .stream()
                            .flatMap(downloadInfo -> downloadInfo.getMediaSet().getChildMediaSets().stream())
                            .filter(mediaSet -> mediaSet.getMediaSetTitle().contains("Units"))
                            .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                            .filter(item -> item.getMediaItemTypeId() == EDTCourseInfoService.MP3_MEDIA_TYPE)
                            .map(MediaItem::getMediaItemId)
                            .collect(Collectors.toList());
                    return new ImmutablePair<>(level, itemIds);
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public static ProductInfoFromPCM setProductInfo(String productCode, ProductInfoFromPCM productInfoFromPCM, CustomerInfo pcmCustInfo) {
        List<CustomersOrder> customersOrders = pcmCustInfo.getResultData().getCustomer().getCustomersOrders();

        customersOrders.forEach(customersOrder -> {
            productInfoFromPCM.getOrdersProductList().addAll(customersOrder.getOrdersProducts());
        });

        productInfoFromPCM.getOrdersProductList().forEach(ordersProduct -> {
            if (productCode.equals(ordersProduct.getProduct().getIsbn13().replace("-", ""))) {
                productInfoFromPCM.setOrderProduct(ordersProduct);
            }
        });

        return productInfoFromPCM;
    }
}
