package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.*;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.ProductInfoFromPCM;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.ProductInfoFromUnlimited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

@Service
public class EDTCourseInfoService {
    public static final int MP3_MEDIA_TYPE = 1;
    @Autowired
    private ApplicationConfiguration config;

    private static final Logger logger = LoggerFactory.getLogger(EDTCourseInfoService.class);

    public AggregatedProductInfo getCourseInfos(boolean isPUProductCode, String productCode, String auth0UserId) {
        AggregatedProductInfo aggregatedProductInfo = new AggregatedProductInfo();
        //Will get only one course(one level) info for the productcode
        if (isPUProductCode) {
            putInProductInfoFromPU(productCode, aggregatedProductInfo);
        } else {
            putInProductInfoFromPCM(auth0UserId, productCode, aggregatedProductInfo);
        }
        return aggregatedProductInfo;
    }

    private void putInProductInfoFromPU(String productCode, AggregatedProductInfo aggregatedProductInfo) {
        try {
            ProductInfoFromUnlimited productInfoForPimsleurUnlimited
                    = getProductInfoForPimsleurUnlimited(productCode);
            aggregatedProductInfo.setProductInfoFromPU(productInfoForPimsleurUnlimited);
        } catch (Exception exceptionWhenGetProductInfoFromPU) {
            logger.error("Exception occured when get product info with PU product code.");
            exceptionWhenGetProductInfoFromPU.printStackTrace();
            throw exceptionWhenGetProductInfoFromPU;
        }
    }

    private void putInProductInfoFromPCM(String auth0UserId, String productCode, AggregatedProductInfo aggregatedProductInfo) {
        try{
            ProductInfoFromPCM productInfoForPCM = getTheProductInfoFromPCM(auth0UserId, productCode);
            aggregatedProductInfo.setProductInfoFromPCM(productInfoForPCM);
        } catch (Exception exceptionWhenGetProductInfoFromPCM) {
            logger.error("Exception occured when get product info with PU product code.");
            exceptionWhenGetProductInfoFromPCM.printStackTrace();
            throw exceptionWhenGetProductInfoFromPCM;
        }
    }

    private ProductInfoFromPCM getTheProductInfoFromPCM(String sub, String productCode) {
        ProductInfoFromPCM productInfoFromPCM = new ProductInfoFromPCM();
        productInfoFromPCM.setOrdersProductList(new ArrayList<>());
        CustomerInfo pcmCustInfo = getCustomerInfo(sub,
                config.getApiParameter("pcmCustomerAction"),
                config.getApiParameter("pcmDomain"));

        filterOutProductInfoForTheProductCode(productCode, productInfoFromPCM, pcmCustInfo);
        //todo: get lesson (mp3) info

        return productInfoFromPCM;
    }

    private void filterOutProductInfoForTheProductCode(String productCode, ProductInfoFromPCM productInfoFromPCM, CustomerInfo pcmCustInfo) {
        List<CustomersOrder> customersOrders = pcmCustInfo.getResultData().getCustomer().getCustomersOrders();
        customersOrders.forEach(customersOrder -> {
            productInfoFromPCM.getOrdersProductList().addAll(customersOrder.getOrdersProducts());
        });

        productInfoFromPCM.getOrdersProductList().forEach(ordersProduct -> {
            if (productCode.equals(ordersProduct.getProduct().getIsbn13().replace("-", ""))) {
                productInfoFromPCM.setOrderProduct(ordersProduct);
            }
        });

        //Get miid(media set id)
//        OrdersProduct theOrderProduct = productInfoFromPCM.getOrderProduct();
//        List<OrdersProductAttribute> ordersProductsAttributes = theOrderProduct.getOrdersProductsAttributes();
//
//        ordersProductsAttributes.forEach(ordersProductAttribute -> {
//            List<OrdersProductsDownload> ordersProductsDownloads = ordersProductAttribute.getOrdersProductsDownloads();
//            ordersProductsDownloads.forEach(ordersProductsDownload -> {
//                List<ChildMediaSet> childMediaSets = ordersProductsDownload.getMediaSet().getChildMediaSets();
//
//                childMediaSets.forEach(childMediaSet -> {
//                    List<MediaItem> mediaItems = childMediaSet.getMediaItems();
//                    mediaItems.forEach(mediaItem -> {
//                        if (mediaItem.getMediaItemTypeId() == MP3_MEDIA_TYPE) {
//
//                        }
//                        mediaItem.getMediaItemId();
//                    });
//                });
//            });
//        });

    }

    private CustomerInfo getCustomerInfo(String sub, String action, String domain) {
        return postToEdt(
                createPostBody(sub, action, domain),
                config.getProperty("edt.api.customerInfoApiUrl"),
                CustomerInfo.class);
    }

    private HttpEntity<String> createPostBody(String sub, String action, String domain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(
                String.format(config.getApiParameter("customerInfoDefaultParameters"), sub, action, domain),
                headers);
    }

    private ProductInfoFromUnlimited getProductInfoForPimsleurUnlimited(String product_code) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(asList(TEXT_HTML, APPLICATION_JSON));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> requestEntity = new HttpEntity<>(
                String.format(config.getApiParameter("unlimitedProductInfoDefaultParameters"), product_code),
                headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(converter);

        String productInfoApiUrl = config.getProperty("edt.api.productInfoApiUrl");
        return restTemplate.postForObject(productInfoApiUrl, requestEntity, ProductInfoFromUnlimited.class);
    }

}
