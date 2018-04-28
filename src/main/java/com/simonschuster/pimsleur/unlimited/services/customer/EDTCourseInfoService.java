package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.*;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.*;
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
        AggregatedProductInfo productInfo = new AggregatedProductInfo();
        //Will get only one course(one level) info for the productcode
        if (isPUProductCode) {
            getPuProductInfo(productCode, productInfo);
        } else {
            getPcmProductInfo(auth0UserId, productCode, productInfo);
            getPcmLessonsInfo(productInfo);
        }
        return productInfo;
    }

    private void getPuProductInfo(String productCode, AggregatedProductInfo productInfo) {
        try {
            productInfo.setPuProductInfo(getProductInfoFromPu(productCode));
        } catch (Exception exception) {
            logger.error("Exception occured when get product info with PU product code.");
            exception.printStackTrace();
            throw exception;
        }
    }

    private void getPcmProductInfo(String auth0UserId, String productCode, AggregatedProductInfo productInfo) {
        try {
            productInfo.setPcmProductInfo(getProductInfoFromPCM(auth0UserId, productCode));
        } catch (Exception exception) {
            logger.error("Exception occured when get product info with PU product code.");
            exception.printStackTrace();
            throw exception;
        }
    }

    private void getPcmLessonsInfo(AggregatedProductInfo productInfo) {
        PCMAudioRequestInfo pcmAudioRequestInfo = productInfoMapper.getMediaItemInfo(productInfo.getPcmProductInfo());
        productInfo.setPcmAudioInfo(getAudioInfo(pcmAudioRequestInfo));
    }

    private Map<String, List<Lesson>> getAudioInfo(PCMAudioRequestInfo requestInfo) {
        Map<String, Map<String, Integer>> mediaItemIds = requestInfo.getMediaItemIds();
        Map<String, List<Lesson>> pcmAudioRespInfo = new HashMap<>();

        mediaItemIds.forEach((level, mediaItemInfo) ->
            pcmAudioRespInfo.put(level, generateLevelInfo(requestInfo, level, mediaItemInfo))
        );

        return pcmAudioRespInfo;
    }

    private List<Lesson> generateLevelInfo(PCMAudioRequestInfo pcmAudioRequestInfo, String level, Map<String, Integer> mediaItemInfos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        List<Lesson> lessonsForThisLevel = new ArrayList<>();
        mediaItemInfos.forEach((lessonTitle, mediaItemId) -> {
            Lesson lesson = new Lesson();
            AudioInfoFromPCM audioInfoFromPCM = null;
            audioInfoFromPCM = postToEdt(
                    new HttpEntity<>(
                            String.format(config.getApiParameter("pCMMp3Parameters"),
                                    mediaItemId,
                                    pcmAudioRequestInfo.getCustomerToken(),
                                    pcmAudioRequestInfo.getEntitlementTokens().get(level),
                                    pcmAudioRequestInfo.getCustomersId()),
                            headers),
                    config.getProperty("edt.api.pCMMp3ApiUrl"),
                    AudioInfoFromPCM.class);

            //todo: get audio link from pcm, request not working
            lesson.setAudioLink(audioInfoFromPCM.getResult_data().getUrl());
            lesson.setName(lessonTitle);
            lesson.setLevel(Integer.parseInt(level));
            lesson.setMediaItemId(mediaItemId);
            lesson.setLessonNumber(lessonTitle.replace("Unit ", ""));
            lessonsForThisLevel.add(lesson);
        });
        return lessonsForThisLevel;
    }

    private ProductInfoFromPCM getProductInfoFromPCM(String sub, String productCode) {
        ProductInfoFromPCM productInfo = new ProductInfoFromPCM();

        CustomerInfo pcmCustomerInfo = getCustomerInfo(
                sub,
                config.getApiParameter("pcmCustomerAction"),
                config.getApiParameter("pcmDomain")
        );

        extractProduct(productCode, productInfo, pcmCustomerInfo);

        return productInfo;
    }

    private void extractProduct(String productCode, ProductInfoFromPCM product, CustomerInfo pcmCustomerInfo) {
        Customer customer = pcmCustomerInfo.getResultData().getCustomer();


        product.setCustomersId(customer.getCustomersId());
        product.setCustomerToken(customer.getIdentityVerificationToken());

        OrdersProduct ordersProduct = customer.getCustomersOrders()
                .stream()
                .flatMap(customersOrder -> customersOrder.getOrdersProducts().stream())
                .filter(it -> Objects.equals(productCode, it.getProduct().getIsbn13().replace("-", "")))
                .findFirst()
                .get();

        product.setOrderProduct(ordersProduct);

    }


    private CustomerInfo getCustomerInfo(String sub, String action, String domain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return postToEdt(
                new HttpEntity<>(
                        String.format(config.getApiParameter("customerInfoDefaultParameters"), sub, action, domain),
                        headers),
                config.getProperty("edt.api.customerInfoApiUrl"),
                CustomerInfo.class);
    }

    private ProductInfoFromUnlimited getProductInfoFromPu(String product_code) {
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
