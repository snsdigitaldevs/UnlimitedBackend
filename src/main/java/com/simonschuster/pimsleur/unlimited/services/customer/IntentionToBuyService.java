package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.utils.EdtErrorCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class IntentionToBuyService {
    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    public void intentionToBuy(String customerId, String isbn, String storeDomain) {
        String url = config.getProperty("edt.api.intentionToBuy.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(String.format(config.getProperty("edt.api.intentionToBuy.parameters"),
                isbn, storeDomain, customerId, appIdService.getAppId(storeDomain)), headers);
        EdtResponseCode intentionToBuyResponse = postToEdt(entity, url, EdtResponseCode.class);

        if (!intentionToBuyResponse.getResultCode().equals(1)) {
            EdtErrorCodeUtil.throwError(intentionToBuyResponse.getResultCode(), "intention to buy failed!");
        }
    }
}
