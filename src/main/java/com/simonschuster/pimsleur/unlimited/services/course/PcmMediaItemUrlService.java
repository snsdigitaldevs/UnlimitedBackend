package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.BatchedMediaItemUrls;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.MediaItemUrl;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class PcmMediaItemUrlService {

    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    public MediaItemUrl getMediaItemUrl(Integer mediaItemId, String customerToken,
                                        String entitlementToken, Integer customersId, String storeDomain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(storeDomain);
        String parameters = format(config.getApiParameter("pCMMp3Parameters"),
                mediaItemId, customerToken, entitlementToken, customersId, appId);

        return postToEdt(new HttpEntity<>(parameters, headers),
                config.getProperty("edt.api.pCMMp3ApiUrl"),
                MediaItemUrl.class);
    }

    public BatchedMediaItemUrls getBatchedMediaItemUrls(Integer mediaSetId, String customerToken,
                                                        String entitlementToken, Integer customersId, String storeDomain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(storeDomain);
        String parameters = format(config.getApiParameter("pCMBatchMp3Parameters"),
                mediaSetId, customerToken,
                entitlementToken, customersId, appId);

        return postToEdt(new HttpEntity<>(parameters, headers),
                config.getProperty("edt.api.pcmBatchMp3ApiUrl"),
                BatchedMediaItemUrls.class);
    }
}
