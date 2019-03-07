package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.PuProductInfo;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class PUCourseInfoService {
    public static final int MP3_MEDIA_TYPE = 1;
    public static final String KEY_DOWNLOAD = "Download";

    @Autowired
    private ApplicationConfiguration config;

    private static final Logger logger = LoggerFactory.getLogger(PUCourseInfoService.class);
    @Autowired
    private AppIdService appIdService;

    /**
     * Get product info for Pimsleur Unlimited.
     *
     * @param productCode
     * @param storeDomain
     */
    public AggregatedProductInfo getPuProductInfo(String productCode, String storeDomain) {
        try {
            AggregatedProductInfo productInfo = new AggregatedProductInfo();
            PuProductInfo productInfoFromPu = getProductInfoFromPu(productCode, storeDomain);
            productInfo.setPuProductInfo(productInfoFromPu);
            return productInfo;
        } catch (Exception exception) {
            logger.error("Exception occured when get product info with PU product code.");
            exception.printStackTrace();
            throw new PimsleurException("Exception occured when get product info with PU product code.");
        }
    }

    /**
     * Send reqeust to get Pimsleur Unlimited product info.
     *
     * @param productCode
     * @param storeDomain
     * @return
     */
    private PuProductInfo getProductInfoFromPu(String productCode, String storeDomain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(storeDomain);
        HttpEntity<String> requestEntity = new HttpEntity<>(
                String.format(config.getApiParameter("unlimitedProductInfoDefaultParameters"), productCode, appId),
                headers);
        String productInfoApiUrl = config.getProperty("edt.api.productInfoApiUrl");

        return EDTRequestUtil.postToEdt(requestEntity, productInfoApiUrl, PuProductInfo.class);
    }
}
