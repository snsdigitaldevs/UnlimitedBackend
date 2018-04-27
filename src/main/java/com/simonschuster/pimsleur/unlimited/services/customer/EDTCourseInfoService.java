package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.*;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.LessonsAudioInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.ProductInfoFromPCM;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.ProductInfoFromUnlimited;
import com.simonschuster.pimsleur.unlimited.mapper.productInfo.ProductInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

@Service
public class EDTCourseInfoService {
    public static final int MP3_MEDIA_TYPE = 1;
    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private ProductInfoMapper productInfoMapper;

    private static final Logger logger = LoggerFactory.getLogger(EDTCourseInfoService.class);

    public AggregatedProductInfo getCourseInfos(boolean isPUProductCode, String productCode, String auth0UserId) {
        AggregatedProductInfo aggregatedProductInfo = new AggregatedProductInfo();
        //Will get only one course(one level) info for the productcode
        if (isPUProductCode) {
            putInProductInfoFromPU(productCode, aggregatedProductInfo);
        } else {
            putInProductInfoFromPCM(auth0UserId, productCode, aggregatedProductInfo);
            putInLessonsInfoFromPCM(aggregatedProductInfo);
        }
        return aggregatedProductInfo;
    }

    private void putInProductInfoFromPU(String productCode, AggregatedProductInfo aggregatedProductInfo) {
        try {
            ProductInfoFromUnlimited productInfoForPimsleurUnlimited = getProductInfoForPimsleurUnlimited(productCode);
            aggregatedProductInfo.setProductInfoFromPU(productInfoForPimsleurUnlimited);
        } catch (Exception exceptionWhenGetProductInfoFromPU) {
            logger.error("Exception occured when get product info with PU product code.");
            exceptionWhenGetProductInfoFromPU.printStackTrace();
            throw exceptionWhenGetProductInfoFromPU;
        }
    }

    private void putInProductInfoFromPCM(String auth0UserId, String productCode, AggregatedProductInfo aggregatedProductInfo) {
        try {
            ProductInfoFromPCM productInfoForPCM = getTheProductInfoFromPCM(auth0UserId, productCode);
            aggregatedProductInfo.setProductInfoFromPCM(productInfoForPCM);
        } catch (Exception exceptionWhenGetProductInfoFromPCM) {
            logger.error("Exception occured when get product info with PU product code.");
            exceptionWhenGetProductInfoFromPCM.printStackTrace();
            throw exceptionWhenGetProductInfoFromPCM;
        }
    }

    private void putInLessonsInfoFromPCM(AggregatedProductInfo aggregatedProductInfo) {
        ProductInfoFromPCM productInfoFromPCM = aggregatedProductInfo.getProductInfoFromPCM();

        Map<String, Map<String, Integer>> mediaItemInfo = productInfoMapper.getMediaItemInfo(productInfoFromPCM).getMediaItemIds();
        //todo: get lesson (mp3) info from rdlss API
        LessonsAudioInfo lessonsAudioInfo = getLessonsAudioInfoFromEDT(mediaItemInfo);

        //mediasetinfo might not need to put in aggregatedProductInfo if lessonsAudioInfo contains level and mediasetid info.
        aggregatedProductInfo.setMediaSetInfo(mediaItemInfo);
        aggregatedProductInfo.setLessonAudioInfoFromPCM(lessonsAudioInfo);
    }

    private LessonsAudioInfo getLessonsAudioInfoFromEDT(Map<String, Map<String, Integer>> mediaItemInfo) {


        return null;
    }

    private ProductInfoFromPCM getTheProductInfoFromPCM(String sub, String productCode) {
        ProductInfoFromPCM productInfoFromPCM = new ProductInfoFromPCM();
        productInfoFromPCM.setOrdersProductList(new ArrayList<>());
        CustomerInfo pcmCustInfo = getCustomerInfo(sub,
                config.getApiParameter("pcmCustomerAction"),
                config.getApiParameter("pcmDomain"));

        productInfoMapper.setProductInfo(productCode, productInfoFromPCM, pcmCustInfo);

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
