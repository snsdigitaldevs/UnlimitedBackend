package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Lesson;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Customer;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.MediaItem;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProductAttribute;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.*;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo.createInstanceForPcm;
import static java.lang.Boolean.parseBoolean;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Service
public class PcmCourseInfoService {
    public static final String KEY_LESSONS = "Lessons";

    private static final String KEY_UNITS = "Units";

    @Autowired
    private EDTCustomerInfoService customerInfoService;
    @Autowired
    private PcmMediaItemUrlService pcmMediaItemUrlService;
    @Autowired
    private PcmReadingsService pcmReadingsService;

    @Autowired
    private ApplicationConfiguration config;

    private static final Logger logger = LoggerFactory.getLogger(PUCourseInfoService.class);

    public List<Course> getCourses(String productCode, String sub) {
        PcmProduct pcmProductInfo = getPcmProductInfo(sub);
        Map<String, List<Lesson>> pcmAudioInfo = getPcmAudioInfo(productCode, pcmProductInfo);

        List<Course> courses = createInstanceForPcm(pcmProductInfo, pcmAudioInfo).toDto();
        pcmReadingsService.addReadingsToCourses(courses,pcmProductInfo.getOrdersProducts());
        return courses;
    }

    private PcmProduct getPcmProductInfo(String sub) {
        try {
            Customer customer = customerInfoService.getPcmCustomerInfo(sub)
                    .getResultData().getCustomer();

            PcmProduct pcmProduct = new PcmProduct();
            pcmProduct.setCustomersId(customer.getCustomersId());
            pcmProduct.setCustomerToken(customer.getIdentityVerificationToken());
            pcmProduct.setOrdersProducts(customer.getAllOrdersProducts());
            return pcmProduct;
        } catch (Exception exception) {
            logger.error("Exception occurred when get product info with PCM product code.");
            exception.printStackTrace();
            throw new PimsleurException("Exception occurred when get product info with PCM product code.");
        }
    }

    /**
     * Get lesson detail info, such as mp3 urls, lesson names...
     *
     * @param productCode
     * @param pcmProduct
     */
    private Map<String, List<Lesson>> getPcmAudioInfo(String productCode, PcmProduct pcmProduct) {
        Map<String, Pair<String, Integer>> entitlementTokens = new HashMap<>();
        Map<String, Map<String, Integer>> mediaItemIds = getMediaItemIds(pcmProduct, entitlementTokens, productCode);

        PcmAudioReqParams pcmAudioReqParams = new PcmAudioReqParams();
        pcmAudioReqParams.setMediaItemIds(mediaItemIds);
        pcmAudioReqParams.setEntitlementTokens(entitlementTokens);
        pcmAudioReqParams.setCustomersId(pcmProduct.getCustomersId());
        pcmAudioReqParams.setCustomerToken(pcmProduct.getCustomerToken());

        return getAudioInfo(pcmAudioReqParams);
    }

    /**
     * Get audio urls for all lessons of all levels.
     *
     * @param params
     * @return
     */
    private Map<String, List<Lesson>> getAudioInfo(PcmAudioReqParams params) {
        Map<String, List<Lesson>> pcmAudioRespInfo = new HashMap<>();

        boolean isBatched = parseBoolean(config.getProperty("toggle.fetch.mp3.url.batch"));
        params.getMediaItemIds().forEach((level, mediaItemInfo) -> {
            if (isBatched) {
                pcmAudioRespInfo.put(level, batchFetchLessons(params, level, mediaItemInfo));
            } else {
                pcmAudioRespInfo.put(level, fetchLessonsOneByOne(params, level, mediaItemInfo));
            }
        });

        return pcmAudioRespInfo;
    }

    /**
     * Get media item ids
     *
     * @param pcmProduct
     * @param entitlementTokens
     * @param productCode
     * @return
     */
    private Map<String, Map<String, Integer>> getMediaItemIds(PcmProduct pcmProduct,
                                                              Map<String, Pair<String, Integer>> entitlementTokens,
                                                              String productCode) {

        Optional<OrdersProduct> first = pcmProduct.getOrdersProducts().stream()
                .filter(ordersProduct -> Objects.equals(ordersProduct.getProduct().getProductCode(), productCode))
                .findFirst();

        if (first.isPresent()) {
            Map<String, OrdersProduct> filteredOrdersProductList = new HashMap<>();
            filteredOrdersProductList.put(productCode, first.get());
            pcmProduct.setOrdersProducts(singletonList(first.get()));
            return filterItemIdsOfAllLevels(entitlementTokens, productCode, filteredOrdersProductList);
        } else if (findMatchedProductInfo(pcmProduct, productCode)) {
            return filterItemIdsOfOneLevel(entitlementTokens, productCode, pcmProduct);
        } else {
            String errorMessage = "No product info found from PCM with matched product code";
            logger.error(errorMessage);
            return new HashMap<>();
        }
    }

    private List<Lesson> batchFetchLessons(PcmAudioReqParams pcmAudioReqParams, String level, Map<String, Integer> mediaItemInfos) {
        int mediaSetId = pcmAudioReqParams.getEntitlementTokens().get(level).getValue();

        BatchedMediaItemUrls batchedMediaItemUrls =
                pcmMediaItemUrlService.getBatchedMediaItemUrls(mediaSetId,
                        pcmAudioReqParams.getCustomerToken(),
                        pcmAudioReqParams.getEntitlementTokens().get(level).getKey(),
                        pcmAudioReqParams.getCustomersId());

        return mediaItemInfos.entrySet().stream().map(entry -> {
            Lesson lesson = new Lesson();
            String title = entry.getKey();
            Integer itemId = entry.getValue();

            List<BatchedMediaItemUrls.ResultDataBean.UrlsBean> urls = batchedMediaItemUrls.getResult_data().getUrls();

            // find the audio url
            for (BatchedMediaItemUrls.ResultDataBean.UrlsBean url : urls) {
                if (url.getMediaItemId() == itemId) {
                    lesson.setAudioLink(url.getUrl());
                    break;
                }
            }

            lesson.setName(title);
            lesson.setLevel(Integer.parseInt(level));
            lesson.setMediaItemId(itemId);
            lesson.setLessonNumber(title.split(" ")[1]);
            return lesson;
        }).collect(toList());

    }

    /**
     * Send request to get mp3 urls for lessons of a level.
     *
     * @param pcmAudioReqParams
     * @param level
     * @param mediaItemInfos
     * @return
     */
    private List<Lesson> fetchLessonsOneByOne(PcmAudioReqParams pcmAudioReqParams, String level, Map<String, Integer> mediaItemInfos) {

        List<Lesson> lessons = mediaItemInfos.entrySet().parallelStream().map(entry -> {

            String title = entry.getKey();
            Integer mediaItemId = entry.getValue();

            MediaItemUrl mediaItemUrl = pcmMediaItemUrlService.getMediaItemUrl(mediaItemId,
                    pcmAudioReqParams.getCustomerToken(),
                    pcmAudioReqParams.getEntitlementTokens().get(level).getLeft(),
                    pcmAudioReqParams.getCustomersId());

            Lesson lesson = new Lesson();
            lesson.setAudioLink(mediaItemUrl.getResult_data().getUrl());
            lesson.setName(title);
            lesson.setLevel(Integer.parseInt(level));
            lesson.setMediaItemId(mediaItemId);
            lesson.setLessonNumber(title.split(" ")[1]);
            return lesson;
        }).collect(toList());

        return lessons;

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

        List<Map> listOfItemIdsByLevel = orderProduct.getOrdersProductsAttributes()
                .stream()
                .filter(attribute -> attribute.getProductsOptions().contains(PUCourseInfoService.KEY_DOWNLOAD))
                .map(attribute -> {
                    Map itemIdsByLevel = new HashMap();
                    attribute.getOrdersProductsDownloads()
                            .stream()
                            .peek(download -> entitlementTokens.put(download.getMediaSet().getProduct().getProductsLevel().toString(),
                                    new ImmutablePair<>(download.getEntitlementToken(), download.getMediaSetId())))
                            .forEach(downloadInfo -> {
                                Map<String, Integer> itemIds = new HashMap<>();

                                String levelInDownload = downloadInfo.getMediaSet().getProduct().getProductsLevel().toString();
                                downloadInfo.getMediaSet().getChildMediaSets().stream()
                                        .filter(mediaSet -> isLesson(mediaSet.getMediaSetTitle()))
                                        .flatMap(childMediaSet -> childMediaSet.getMediaItems().stream())
                                        .filter(MediaItem::isLesson)
                                        .forEach(item -> itemIds.put(item.getMediaItemTitle(), item.getMediaItemId()));
                                itemIdsByLevel.put(levelInDownload, itemIds);
                            });
                    return itemIdsByLevel;
                })
                .collect(toList());

        Map itemIdsByLevel = new HashMap<String, Map<String, Integer>>();
        listOfItemIdsByLevel.forEach(oneLevelItemIds -> itemIdsByLevel.putAll(oneLevelItemIds));

        return itemIdsByLevel;
    }

    /**
     * Check if any 'OrdersProductsDownload' has the same product code as given.
     *
     * @param pcmProduct
     * @param productCode
     * @return
     */
    private boolean findMatchedProductInfo(PcmProduct pcmProduct, String productCode) {
        int size = pcmProduct.getOrdersProducts()
                .stream()
                .flatMap(ordersProduct -> ordersProduct.getOrdersProductsAttributes().stream())
                .filter(attribute -> attribute.getProductsOptions().contains(PUCourseInfoService.KEY_DOWNLOAD))
                .flatMap(attribute -> attribute.getOrdersProductsDownloads().stream())
                .filter(download -> download.getMediaSet().getProduct().getProductCode().equals(productCode))
                .collect(toList())
                .size();

        return size > 0;
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
                    .filter(MediaItem::isLesson)
                    .forEach(item -> itemIds.put(item.getMediaItemTitle(), item.getMediaItemId()));

            oneLevelItemIds.put(level, itemIds);
        }

        return oneLevelItemIds;
    }

    private boolean isLesson(String title) {
        return title.contains(KEY_UNITS) || title.contains(KEY_LESSONS);
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
        List<OrdersProductAttribute> matchedProductAttribute = new ArrayList<>();
        Map<String, OrdersProduct> filteredOrdersProductList = new HashMap<>();

        pcmProduct.getOrdersProducts().forEach((ordersProduct) -> {
            List<OrdersProductAttribute> matchedAttributes = ordersProduct.getOrdersProductsAttributes().stream()
                    .filter(attribute -> attribute.getProductsOptions().contains(PUCourseInfoService.KEY_DOWNLOAD))
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
