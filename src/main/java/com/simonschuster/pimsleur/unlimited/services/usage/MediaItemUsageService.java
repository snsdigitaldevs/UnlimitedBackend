package com.simonschuster.pimsleur.unlimited.services.usage;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.CodeOnlyResponseEDT;
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

    public void reportMediaItemUsage(String customerId, String mediaItemId,
                                     String identityVerificationToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        String parameters = format(config.getApiParameter("reportUsage"),
                customerId, mediaItemId, identityVerificationToken);

        CodeOnlyResponseEDT response = postToEdt(new HttpEntity<>(parameters, headers),
                config.getProperty("edt.api.reportUsage.url"), CodeOnlyResponseEDT.class);

        if (response.getResultCode() != 1) {
            throw new PimsleurException("report media item usage failed");
        }
    }
}
