package com.simonschuster.pimsleur.unlimited.services.usage;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.usage.MediaItemUsageBody;
import com.simonschuster.pimsleur.unlimited.data.edt.CodeOnlyResponseEDT;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class MediaItemUsageService {
    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    public void reportMediaItemUsage(String customerId, String mediaItemId, MediaItemUsageBody mediaItemUsageBody) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(mediaItemUsageBody.getStoreDomain());
        String parameters = format(config.getApiParameter("reportUsage"),
                customerId, mediaItemId, mediaItemUsageBody.getIdentityVerificationToken(), appId);

        CodeOnlyResponseEDT response = postToEdt(new HttpEntity<>(parameters, headers),
                config.getProperty("edt.api.reportUsage.url"), CodeOnlyResponseEDT.class);

        if (response.getResultCode() != 1) {
            throw new PimsleurException("report media item usage failed");
        }
    }
}
