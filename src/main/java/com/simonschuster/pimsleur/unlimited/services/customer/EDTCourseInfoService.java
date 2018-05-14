package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.*;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;

@Service
public class EDTCourseInfoService {
    public static final int MP3_MEDIA_TYPE = 1;
    public static final String KEY_UNITS = "Units";
    public static final String KEY_LESSONS = "Lessons";
    public static final String KEY_DOWNLOAD = "Download";

    @Autowired
    private ApplicationConfiguration config;

    private static final Logger logger = LoggerFactory.getLogger(EDTCourseInfoService.class);

    public AggregatedProductInfo getCourseInfos(boolean isPUProductCode, String productCode, String auth0UserId) {
        AggregatedProductInfo productInfo = new AggregatedProductInfo();
        if (isPUProductCode) {
            getPuProductInfo(productCode, productInfo);
        } else {
            getPcmProductInfo(auth0UserId, productInfo);
            getPcmLessons(productInfo, productCode);
        }

        return productInfo;
    }

    /**
     * Get product info for Pimsleur Unlimited.
     *
     * @param productCode
     * @param productInfo
     */
    private void getPuProductInfo(String productCode, AggregatedProductInfo productInfo) {
        try {
            productInfo.setPuProductInfo(getProductInfoFromPu(productCode));
        } catch (Exception exception) {
            logger.error("Exception occured when get product info with PU product code.");
            exception.printStackTrace();
            throw new PimsleurException("Exception occured when get product info with PU product code.");
        }
    }

    /**
     * Get product info for Pimsleur Course Manager.
     *
     * @param auth0UserId
     * @param aggregatedProductInfo
     */
    private void getPcmProductInfo(String auth0UserId, AggregatedProductInfo aggregatedProductInfo) {
        try {
            aggregatedProductInfo.setPcmProduct(getProductInfoFromPCM(auth0UserId));
        } catch (Exception exception) {
            logger.error("Exception occured when get product info with PU product code.");
            exception.printStackTrace();
            throw new PimsleurException("Exception occured when get product info with PU product code.");
        }
    }

    /**
     * Get lesson detail info, such as mp3 urls, lesson names...
     *
     * @param productInfo
     * @param productCode
     */
    private void getPcmLessons(AggregatedProductInfo productInfo, String productCode) {
        PcmAudioReqParams audioRequestInfo = buildRequestParams(productInfo.getPcmProduct(), productCode);
        productInfo.setPcmAudioInfo(getAudioInfo(audioRequestInfo));
    }

    /**
     * Build parameters that're used to send the request for fetching mp3 urls.
     *
     * @param pcmProduct
     * @param productCode
     * @return
     */
    private PcmAudioReqParams buildRequestParams(PcmProduct pcmProduct, String productCode) {
        Map<String, Pair<String, Integer>> entitlementTokens = new HashMap<>();
        Map<String, Map<String, Integer>> mediaItemIds = getMediaItemIds(pcmProduct, entitlementTokens, productCode);

        PcmAudioReqParams pcmAudioReqParams = new PcmAudioReqParams();
        pcmAudioReqParams.setMediaItemIds(mediaItemIds);
        pcmAudioReqParams.setEntitlementTokens(entitlementTokens);
        pcmAudioReqParams.setCustomersId(pcmProduct.getCustomersId());
        pcmAudioReqParams.setCustomerToken(pcmProduct.getCustomerToken());

        return pcmAudioReqParams;
    }


    /**
     * Get media item ids
     *
     * @param pcmProduct
     * @param entitlementTokens
     * @param productCode
     * @return
     */
    private Map<String, Map<String, Integer>> getMediaItemIds(
            PcmProduct pcmProduct, Map<String, Pair<String, Integer>> entitlementTokens, String productCode) {

        Map<String, OrdersProduct> ordersProductList = pcmProduct.getOrdersProductList();

        if (ordersProductList.containsKey(productCode)) {
            Map<String, OrdersProduct> filteredOrdersProductList = new HashMap<>();
            filteredOrdersProductList.put(productCode, ordersProductList.get(productCode));
            pcmProduct.setOrdersProductList(filteredOrdersProductList);

            return filterItemIdsOfAllLevels(entitlementTokens, productCode, filteredOrdersProductList);
        } else if (findMatchedProductInfo(pcmProduct, productCode)) {
            return filterItemIdsOfOneLevel(entitlementTokens, productCode, pcmProduct);
        } else {
            String errorMessage = "No product info found from PCM with matched product code";
            logger.error(errorMessage);
            return new HashMap();
        }
    }

    /**
     * Filter item ids for one level if the product code represents product for only one level.
     *
     * @param entitlementTokens
     * @param productCode
     * @param pcmProduct
     * @return
     */
    private Map<String, Map<String, Integer>> filterItemIdsOfOneLevel(Map<String, Pair<String, Integer>> entitlementTokens,
                                                                      String productCode, PcmProduct pcmProduct) {
        HashMap<String, Map<String, Integer>> oneLevelItemIds = new HashMap<>();

        OrdersProductAttribute attribute = getMatchedProductAttribute(pcmProduct, productCode);

        if (attribute != null) {
            String level = attribute.getProductsOptions().split(" ")[1];
            Map<String, Integer> itemIds = new HashMap<>();

            attribute.getOrdersProductsDownloads()
                    .stream()
                    .peek(download -> entitlementTokens.put(level,
                            new ImmutablePair<>(download.getEntitlementToken(), download.getMediaSetId())))
                    .flatMap(downloadInfo -> downloadInfo.getMediaSet().getChildMediaSets().stream())
                    .filter(mediaSet -> isLesson(mediaSet.getMediaSetTitle()))
                    .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                    .filter(item -> isLesson(item.getMediaItemTitle()))
                    .filter(item -> (item.getMediaItemTypeId() == EDTCourseInfoService.MP3_MEDIA_TYPE)
                            && item.getMediaItemTitle().contains("Unit"))
                    .forEach(item -> itemIds.put(item.getMediaItemTitle(), item.getMediaItemId()));


            oneLevelItemIds.put(level, itemIds);
        }

        return oneLevelItemIds;
    }

    /**
     * Filter out all the item ids if the product code represents several levels.
     *
     * @param entitlementTokens
     * @param productCode
     * @param ordersProductList
     * @return
     */
    private Map<String, Map<String, Integer>> filterItemIdsOfAllLevels(Map<String, Pair<String, Integer>> entitlementTokens, String productCode, Map<String, OrdersProduct> ordersProductList) {
        OrdersProduct orderProduct = ordersProductList.get(productCode);

        return orderProduct.getOrdersProductsAttributes()
                .stream()
                .filter(attribute -> attribute.getProductsOptions().contains(KEY_DOWNLOAD))
                .map(attribute -> {
                    String level = attribute.getProductsOptions().split(" ")[1];
                    Map<String, Integer> itemIds = new HashMap<>();
                    attribute.getOrdersProductsDownloads()
                            .stream()
                            .peek(download -> entitlementTokens.put(level,
                                    new ImmutablePair<>(download.getEntitlementToken(), download.getMediaSetId())))
                            .flatMap(downloadInfo -> downloadInfo.getMediaSet().getChildMediaSets().stream())
                            .filter(mediaSet -> isLesson(mediaSet.getMediaSetTitle()))
                            .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                            .filter(item -> (item.getMediaItemTypeId() == EDTCourseInfoService.MP3_MEDIA_TYPE)
                                    && item.getMediaItemTitle().contains("Unit"))
                            .forEach(item -> itemIds.put(item.getMediaItemTitle(), item.getMediaItemId()));

                    return new ImmutablePair<>(level, itemIds);
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private boolean isLesson(String title) {
        return title.contains(KEY_UNITS) || title.contains(KEY_LESSONS);
    }

    /**
     * Check if any 'OrdersProductsDownload' has the same product code as given.
     *
     * @param pcmProduct
     * @param productCode
     * @return
     */
    private boolean findMatchedProductInfo(PcmProduct pcmProduct, String productCode) {
        int size = pcmProduct.getOrdersProductList().entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().getOrdersProductsAttributes().stream())
                .filter(attribute -> attribute.getProductsOptions().contains(KEY_DOWNLOAD))
                .flatMap(attribute -> attribute.getOrdersProductsDownloads().stream())
                .filter(download -> download.getMediaSet().getProduct().getProductCode().equals(productCode))
                .collect(Collectors.toList())
                .size();

        return size > 0;
    }

    /**
     * Get the 'OrdersProductAttribute' which contains the given product code in it's child
     * 'OrdersProductsDownload' object. This 'OrdersProductAttribute' contains information of a level and
     * will be used to get all lesson information.
     *
     * @param pcmProduct
     * @param productCode
     * @return
     */
    private OrdersProductAttribute getMatchedProductAttribute(PcmProduct pcmProduct, String productCode) {
        final List<OrdersProductAttribute> matchedProductAttribute = new ArrayList<>();
        Map<String, OrdersProduct> filteredOrdersProductList = new HashMap<>();

        pcmProduct.getOrdersProductList().forEach((orderProductCode, ordersProduct) -> {
            List<OrdersProductAttribute> matchedAttributes = ordersProduct.getOrdersProductsAttributes().stream()
                    .filter(attribute -> attribute.getProductsOptions().contains(KEY_DOWNLOAD))
                    .filter(attribute -> attribute.getOrdersProductsDownloads()
                            .stream()
                            .anyMatch(download -> download.getMediaSet().getProduct().getProductCode().equals(productCode)))
                    .collect(Collectors.toList());

            if (matchedAttributes.size() > 0) {
                matchedProductAttribute.add(matchedAttributes.get(0));
                filteredOrdersProductList.put(orderProductCode, ordersProduct);
            }
        });

        pcmProduct.setOrdersProductList(filteredOrdersProductList);

        return matchedProductAttribute.size() > 0 ? matchedProductAttribute.get(0) : null;
    }


    /**
     * Get audio urls for all lessons of all levels.
     *
     * @param params
     * @return
     */
    private Map<String, List<Lesson>> getAudioInfo(PcmAudioReqParams params) {
        Map<String, List<Lesson>> pcmAudioRespInfo = new HashMap<>();

        params.getMediaItemIds().forEach((level, mediaItemInfo) -> {
            boolean isBatched = Boolean.parseBoolean(config.getProperty("toggle.fetch.mp3.url.batch"));
            pcmAudioRespInfo.put(level, isBatched ? fetchAudioForALevel(params, level, mediaItemInfo) : fetchLessons(params, level, mediaItemInfo));
        });

        return pcmAudioRespInfo;
    }


    /**
     * Send request to get mp3 urls for lessons of a level.
     *
     * @param pcmAudioReqParams
     * @param level
     * @param mediaItemInfos
     * @return
     */
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
                                    pcmAudioReqParams.getEntitlementTokens().get(level).getLeft(),
                                    pcmAudioReqParams.getCustomersId()),
                            headers),
                    config.getProperty("edt.api.pCMMp3ApiUrl"),
                    AudioInfoFromPCM.class);

            lesson.setAudioLink(audioInfoFromPCM.getResult_data().getUrl());
            lesson.setName(title);
            lesson.setLevel(Integer.parseInt(level));
            lesson.setMediaItemId(itemId);
            lesson.setLessonNumber(title.split(" ")[1]);
            return lesson;
        }).collect(Collectors.toList());

        return lessons;

    }

    private List<Lesson> fetchAudioForALevel(PcmAudioReqParams pcmAudioReqParams, String level, Map<String, Integer> mediaItemInfos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        int mediaSetId = pcmAudioReqParams.getEntitlementTokens().get(level).getValue();
        String entitlementToken = pcmAudioReqParams.getEntitlementTokens().get(level).getKey();

        BatchedAudio batchedAudio = postToEdt(
                new HttpEntity<>(
                        String.format(config.getApiParameter("pCMBatchMp3Parameters"),
                                mediaSetId, // media set id for level
                                pcmAudioReqParams.getCustomerToken(),
                                entitlementToken, // entitlement token
                                pcmAudioReqParams.getCustomersId()),
                        headers),
                config.getProperty("edt.api.pcmBatchMp3ApiUrl"),
                BatchedAudio.class);

        List<Lesson> lessons = mediaItemInfos.entrySet().stream().map(entry -> {
            Lesson lesson = new Lesson();
            String title = entry.getKey();
            Integer itemId = entry.getValue();

            List<BatchedAudio.ResultDataBean.UrlsBean> urls = batchedAudio.getResult_data().getUrls();

            // find the audio url
            for (int i = 0; i < urls.size(); i++) {
                if (urls.get(i).getMediaItemId() == itemId.intValue()) {
                    lesson.setAudioLink(urls.get(i).getUrl());
                    break;
                }
            }

            lesson.setName(title);
            lesson.setLevel(Integer.parseInt(level));
            lesson.setMediaItemId(itemId);
            lesson.setLessonNumber(title.split(" ")[1]);
            return lesson;
        }).collect(Collectors.toList());

        return lessons;

    }

    /**
     * Send request to get get full customer info and extract all products has been ordered by the user.
     *
     * @param sub
     * @return
     */
    private PcmProduct getProductInfoFromPCM(String sub) {
        PcmProduct pcmProduct = new PcmProduct();

        CustomerInfo pcmCustomerInfo = getCustomerInfo(
                sub,
                config.getApiParameter("pcmCustomerAction"),
                config.getApiParameter("pcmDomain")
        );

        extractPCMProduct(pcmProduct, pcmCustomerInfo);

        return pcmProduct;
    }

    /**
     * Extract products from the given customer info.
     *
     * @param pcmProduct
     * @param pcmCustomerInfo
     */
    private void extractPCMProduct(PcmProduct pcmProduct, CustomerInfo pcmCustomerInfo) {
        Customer customer = pcmCustomerInfo.getResultData().getCustomer();

        pcmProduct.setCustomersId(customer.getCustomersId());
        pcmProduct.setCustomerToken(customer.getIdentityVerificationToken());


        Map<String, OrdersProduct> ordersProductList = new HashMap();
        customer.getCustomersOrders()
                .stream()
                .flatMap(customersOrder -> customersOrder.getOrdersProducts().stream())
                .forEach(ordersProduct -> {
                    ordersProductList.put(ordersProduct.getProduct().getProductCode(), ordersProduct);
                });

        pcmProduct.setOrdersProductList(ordersProductList);
    }


    /**
     * Get full customer info of the given sub(user token)
     *
     * @param sub
     * @param action
     * @param domain
     * @return
     */
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

    /**
     * Send reqeust to get Pimsleur Unlimited product info.
     *
     * @param product_code
     * @return
     */
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
