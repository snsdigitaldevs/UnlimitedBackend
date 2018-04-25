package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomersOrder;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.ProductInfoFromPCM;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.ProductInfoFromUnlimited;
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
    @Autowired
    private ApplicationConfiguration config;

    //todo: make PCM api call available with parameter auth0_user_id
    //todo: make it available to get a list of product??
    public AggregatedProductInfo getCourseInfos(String productCode, String auth0UserId) {
        AggregatedProductInfo aggregatedProductInfo = new AggregatedProductInfo();
        try {
            putInProductInfoFromPU(productCode, aggregatedProductInfo);
        } catch (Exception exceptionWhenGetProductInfoFromUnlimited) {
            putInProductInfoFromPCM(auth0UserId, aggregatedProductInfo);
        }
        return aggregatedProductInfo;
    }

    private void putInProductInfoFromPU(String productCode, AggregatedProductInfo aggregatedProductInfo) {
        ProductInfoFromUnlimited productInfoForPimsleurUnlimited
                = getProductInfoForPimsleurUnlimited(productCode);
        aggregatedProductInfo.setProductInfoFromPU(productInfoForPimsleurUnlimited);
    }

    private void putInProductInfoFromPCM(String auth0UserId, AggregatedProductInfo aggregatedProductInfo) {
        try{
            ProductInfoFromPCM productInfoForPCM = getProductInfoForPCM(auth0UserId);
            aggregatedProductInfo.setProductInfoFromPCM(productInfoForPCM);
        } catch (Exception exceptionWhenGetProductInfoFromPCM) {
            exceptionWhenGetProductInfoFromPCM.printStackTrace();
        }
    }

    private ProductInfoFromPCM getProductInfoForPCM(String sub) {
        ProductInfoFromPCM productInfoFromPCM = new ProductInfoFromPCM();
        productInfoFromPCM.setOrdersProductList(new ArrayList<>());
        CustomerInfo pcmCustInfo = getCustomerInfo(sub,
                config.getApiParameter("pcmCustomerAction"),
                config.getApiParameter("pcmDomain"));

        List<CustomersOrder> customersOrders = pcmCustInfo.getResultData().getCustomer().getCustomersOrders();
        customersOrders.forEach(customersOrder -> {
            productInfoFromPCM.getOrdersProductList().addAll(customersOrder.getOrdersProducts());
        });

        return productInfoFromPCM;
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
