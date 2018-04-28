package com.simonschuster.pimsleur.unlimited.mapper.productInfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.Customer;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomersOrder;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.PCMAudioRequestInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.ProductInfoFromPCM;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCourseInfoService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductInfoMapper {
    public PCMAudioRequestInfo getMediaItemInfo(ProductInfoFromPCM productInfoFromPCM) {

        Map<String, String> entitlementTokens = new HashMap<>();

        Map<String, Map<String, Integer>> mediaItemIds = productInfoFromPCM.getOrderProduct().getOrdersProductsAttributes()
                .stream()
                .filter(attribute -> attribute.getProductsOptions().contains("Download"))
                .map(attribute -> {
                    String level = attribute.getProductsOptions().split(" ")[1];
                    Map<String, Integer> itemIds = new HashMap<String, Integer>();
                            attribute.getOrdersProductsDownloads()
                            .stream()
                            .peek(download -> entitlementTokens.put(level, download.getEntitlementToken()))
                            .flatMap(downloadInfo -> downloadInfo.getMediaSet().getChildMediaSets().stream())
                            .filter(mediaSet -> mediaSet.getMediaSetTitle().contains("Units"))
                            .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                            .filter(item -> item.getMediaItemTypeId() == EDTCourseInfoService.MP3_MEDIA_TYPE)
                            .forEach(item -> {
                                itemIds.put(item.getMediaItemTitle(), item.getMediaItemId());
                            });

                    return new ImmutablePair<>(level, itemIds);
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        PCMAudioRequestInfo pcmAudioRequestInfo = new PCMAudioRequestInfo();
        pcmAudioRequestInfo.setMediaItemIds(mediaItemIds);
        pcmAudioRequestInfo.setEntitlementTokens(entitlementTokens);
        pcmAudioRequestInfo.setCustomersId(productInfoFromPCM.getCustomersId());
        pcmAudioRequestInfo.setCustomerToken(productInfoFromPCM.getCustomerToken());
        return pcmAudioRequestInfo;
    }

    public static ProductInfoFromPCM setProductInfo(String productCode, ProductInfoFromPCM productInfoFromPCM, CustomerInfo pcmCustInfo) {
        Customer customer = pcmCustInfo.getResultData().getCustomer();
        List<CustomersOrder> customersOrders = customer.getCustomersOrders();

        productInfoFromPCM.setCustomersId(customer.getCustomersId());
        productInfoFromPCM.setCustomerToken(customer.getIdentityVerificationToken());

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
