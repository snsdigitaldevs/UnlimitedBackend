package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Customer;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProductAttribute;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

@Service
public class EDTCourseInfoService {
    public static final int MP3_MEDIA_TYPE = 1;
    public static final String UNITS = "Units";
    public static final String LESSONS = "Lessons";
    @Autowired
    private ApplicationConfiguration config;

    private static final Logger logger = LoggerFactory.getLogger(EDTCourseInfoService.class);

    public AggregatedProductInfo getCourseInfos(boolean isPUProductCode, String productCode, String auth0UserId) {
        AggregatedProductInfo productInfo = new AggregatedProductInfo();
        //Will get only one course(one level) info for the productcode
        if (isPUProductCode) {
            getPuProductInfo(productCode, productInfo);
        } else {
            getPcmProductInfo(auth0UserId, productCode, productInfo);
            getPcmLessons(productInfo, productCode);
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

    private void getPcmProductInfo(String auth0UserId, String productCode, AggregatedProductInfo aggregatedProductInfo) {
        try {
            aggregatedProductInfo.setPcmProduct(getProductInfoFromPCM(auth0UserId, productCode));
        } catch (Exception exception) {
            logger.error("Exception occured when get product info with PU product code.");
            exception.printStackTrace();
            throw exception;
        }
    }

    private void getPcmLessons(AggregatedProductInfo productInfo, String productCode) {
        PcmAudioReqParams audioRequestInfo = buildRequestParams(productInfo.getPcmProduct(), productCode);
        productInfo.setPcmAudioInfo(getAudioInfo(audioRequestInfo));
    }

    private PcmAudioReqParams buildRequestParams(PcmProduct pcmProduct, String productCode) {
        Map<String, String> entitlementTokens = new HashMap<>();
        Map<String, Map<String, Integer>> mediaItemIds = getMediaItemIds(pcmProduct, entitlementTokens, productCode);

        PcmAudioReqParams pcmAudioReqParams = new PcmAudioReqParams();
        pcmAudioReqParams.setMediaItemIds(mediaItemIds);
        pcmAudioReqParams.setEntitlementTokens(entitlementTokens);
        pcmAudioReqParams.setCustomersId(pcmProduct.getCustomersId());
        pcmAudioReqParams.setCustomerToken(pcmProduct.getCustomerToken());

        return pcmAudioReqParams;
    }

    private Map<String, Map<String, Integer>> getMediaItemIds(PcmProduct pcmProduct, Map<String, String> entitlementTokens, String productCode) {
        Map<String, OrdersProduct> ordersProductList = pcmProduct.getOrdersProductList();

        if (ordersProductList.containsKey(productCode)) {
            Map filteredOrdersProductList = new HashMap<>();
            filteredOrdersProductList.put(productCode, ordersProductList.get(productCode));
            pcmProduct.setOrdersProductList(filteredOrdersProductList);

            return handleOrderProductCode(entitlementTokens, productCode, filteredOrdersProductList);
        } else if (findMatchedProductInfo(pcmProduct, productCode) != null) {
            return handleLevelProductCode(entitlementTokens, productCode, pcmProduct);
        } else {
            String errorMessage = "No product info found from PCM with matched product code";
            logger.error(errorMessage);
            return new HashMap<>();
        }
    }

    private Map<String, Map<String, Integer>> handleLevelProductCode(Map<String, String> entitlementTokens, String productCode, PcmProduct pcmProduct) {
        OrdersProductAttribute attribute = findMatchedProductInfo(pcmProduct, productCode);
        String level = attribute.getProductsOptions().split(" ")[1];
        Map<String, Integer> itemIds = new HashMap<>();

        attribute.getOrdersProductsDownloads()
                .stream()
                .peek(download -> entitlementTokens.put(level, download.getEntitlementToken()))
                .flatMap(downloadInfo -> downloadInfo.getMediaSet().getChildMediaSets().stream())
                .filter(mediaSet -> mediaSet.getMediaSetTitle().contains(UNITS) || mediaSet.getMediaSetTitle().contains(LESSONS))
                .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                .filter(item -> item.getMediaItemTypeId() == EDTCourseInfoService.MP3_MEDIA_TYPE)
                .forEach(item -> itemIds.put(item.getMediaItemTitle(), item.getMediaItemId()));

        HashMap<String, Map<String, Integer>> oneLevelItemIds = new HashMap<>();
        oneLevelItemIds.put(level, itemIds);
        return oneLevelItemIds;
    }

    private Map<String, Map<String, Integer>> handleOrderProductCode(Map<String, String> entitlementTokens, String productCode, Map<String, OrdersProduct> ordersProductList) {
        OrdersProduct orderProduct = ordersProductList.get(productCode);

        return orderProduct.getOrdersProductsAttributes()
                .stream()
                .filter(attribute -> attribute.getProductsOptions().contains("Download"))
                .map(attribute -> {
                    String level = attribute.getProductsOptions().split(" ")[1];
                    Map<String, Integer> itemIds = new HashMap<>();
                    attribute.getOrdersProductsDownloads()
                            .stream()
                            .peek(download -> entitlementTokens.put(level, download.getEntitlementToken()))
                            .flatMap(downloadInfo -> downloadInfo.getMediaSet().getChildMediaSets().stream())
                            .filter(mediaSet -> mediaSet.getMediaSetTitle().contains("Units") || mediaSet.getMediaSetTitle().contains("Lessons"))
                            .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                            .filter(item -> item.getMediaItemTypeId() == EDTCourseInfoService.MP3_MEDIA_TYPE)
                            .forEach(item -> itemIds.put(item.getMediaItemTitle(), item.getMediaItemId()));

                    return new ImmutablePair<>(level, itemIds);
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private OrdersProductAttribute findMatchedProductInfo(PcmProduct pcmProduct, String productCode) {
        final List<OrdersProductAttribute> matchedProductAttribute = new ArrayList<OrdersProductAttribute>();
        Map<String, OrdersProduct> ordersProductList = pcmProduct.getOrdersProductList();
        Map<String, OrdersProduct> filteredOrdersProductList = new HashMap<>();

        ordersProductList.forEach((orderProductCode, ordersProduct) -> {
            List<OrdersProductAttribute> matchedAttributes = ordersProduct.getOrdersProductsAttributes().stream()
                    .filter(attribute -> attribute.getProductsOptions().contains("Download"))
                    .filter(attribute -> attribute
                            .getOrdersProductsDownloads().stream()
                            .anyMatch(download -> download.getMediaSet().getProduct().getIsbn13().replace("-", "").equals(productCode)))
                    .collect(Collectors.toList());

            if (matchedAttributes.size() != 0) {
                matchedProductAttribute.add(matchedAttributes.get(0));
                filteredOrdersProductList.put(orderProductCode, ordersProduct);
            }
        });

        pcmProduct.setOrdersProductList(filteredOrdersProductList);
        return matchedProductAttribute.size() != 0 ? matchedProductAttribute.get(0) : null;
    }

    private Map<String, List<Lesson>> getAudioInfo(PcmAudioReqParams params) {
        Map<String, List<Lesson>> pcmAudioRespInfo = new HashMap<>();

        params.getMediaItemIds().forEach((level, mediaItemInfo) ->
                pcmAudioRespInfo.put(level, fetchLessons(params, level, mediaItemInfo))
        );

        return pcmAudioRespInfo;
    }

    private List<Lesson> fetchLessons(PcmAudioReqParams pcmAudioReqParams, String level, Map<String, Integer> mediaItemInfos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        List<Lesson> lessons = mediaItemInfos.entrySet().parallelStream().map(entry -> {

            Lesson lesson = new Lesson();
            String title = entry.getKey();
            Integer itemId = entry.getValue();

            AudioInfoFromPCM audioInfoFromPCM = postToEdt(
                    new HttpEntity<>(
                            String.format(config.getApiParameter("pCMMp3Parameters"),
                                    itemId,
                                    pcmAudioReqParams.getCustomerToken(),
                                    pcmAudioReqParams.getEntitlementTokens().get(level),
                                    pcmAudioReqParams.getCustomersId()),
                            headers),
                    config.getProperty("edt.api.pCMMp3ApiUrl"),
                    AudioInfoFromPCM.class);

            lesson.setAudioLink(audioInfoFromPCM.getResult_data().getUrl());
            lesson.setName(title);
            lesson.setLevel(Integer.parseInt(level));
            lesson.setMediaItemId(itemId);
            lesson.setLessonNumber(title.replace("Unit ", "").replace("Lesson ", ""));
            return lesson;
        }).collect(Collectors.toList());

        return lessons;

    }

    private PcmProduct getProductInfoFromPCM(String sub, String productCode) {
        PcmProduct pcmProduct = new PcmProduct();

        CustomerInfo pcmCustomerInfo = getCustomerInfo(
                sub,
                config.getApiParameter("pcmCustomerAction"),
                config.getApiParameter("pcmDomain")
        );

        extractPCMProduct(productCode, pcmProduct, pcmCustomerInfo);

        return pcmProduct;
    }

    private void extractPCMProduct(String productCode, PcmProduct pcmProduct, CustomerInfo pcmCustomerInfo) {
        Customer customer = pcmCustomerInfo.getResultData().getCustomer();

        pcmProduct.setCustomersId(customer.getCustomersId());
        pcmProduct.setCustomerToken(customer.getIdentityVerificationToken());


        Map<String, OrdersProduct> ordersProductList = new HashMap();
        customer.getCustomersOrders()
                .stream()
                .flatMap(customersOrder -> customersOrder.getOrdersProducts().stream())
                .forEach(ordersProduct -> {
                    ordersProductList.put(ordersProduct.getProduct().getIsbn13().replace("-", ""), ordersProduct);
                });

//        pcmProduct.setOrderProduct(ordersProduct);
        pcmProduct.setOrdersProductList(ordersProductList);
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

    private PuProductInfo getProductInfoFromPu(String product_code) {
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
        return restTemplate.postForObject(productInfoApiUrl, requestEntity, PuProductInfo.class);
    }

}
