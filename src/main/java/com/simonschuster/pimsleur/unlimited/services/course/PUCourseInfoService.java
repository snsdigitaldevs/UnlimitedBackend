package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.aop.annotation.LogCostTime;
import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.PuProductInfo;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.services.InstallationFileService;
import com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil;
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
    @Autowired
    private AppIdService appIdService;
    @Autowired
    private InstallationFileService installationFileService;

    /**
     * Get product info for Pimsleur Unlimited.
     *
     * @param productCode
     * @param storeDomain
     */
    @LogCostTime
    public AggregatedProductInfo getPuProductInfo(String productCode, String storeDomain) {
        try {
            AggregatedProductInfo productInfo = new AggregatedProductInfo();
            PuProductInfo productInfoFromPu = getProductInfoFromPu(productCode, storeDomain);
            InstallationFileList installationFileList = installationFileService.getInstallationFileList(productCode, storeDomain);
            productInfo.setPuProductInfo(productInfoFromPu);
            productInfo.setInstallationFileList(installationFileList);
            return productInfo;
        } catch (Exception exception) {
            throw new PimsleurException(
                "Exception occurred when get product info with PU product code " + exception);
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
        String productInfoApiUrl = config.getProperty("edt.api.productInfoApiUrl");

        return EDTRequestUtil.postToEdt(
                createPostBody(productCode, storeDomain, "unlimitedProductInfoDefaultParameters"),
                productInfoApiUrl,
                PuProductInfo.class
        );
    }

    private HttpEntity<String> createPostBody(String productCode, String storeDomain, String propertyName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(storeDomain);
        return new HttpEntity<>(
                String.format(config.getApiParameter(propertyName), productCode, appId),
                headers);
    }
}
