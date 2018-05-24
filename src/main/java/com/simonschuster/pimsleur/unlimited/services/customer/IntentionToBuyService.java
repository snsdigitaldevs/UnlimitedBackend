package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.CodeOnlyResposeEDT;
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

    private static final String APP_ID_IOS = "com.thoughtworks.pimsleur.unlimited.inapppurchase";
    private static final String APP_ID_ANDROID = "com.thoughtworks.pimsleur.unlimited.inapppurchase";
    // TODO: make sure each app_id in iOS and Android

    public void intentionToBuy(String customerId, String isbn, String storeDomain) {
        String url = config.getProperty("edt.api.intentionToBuy.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String appId = "";
        if (storeDomain.contains("android")) {
            appId = APP_ID_ANDROID;
        } else if (storeDomain.contains("ios")) {
            appId = APP_ID_IOS;
        }

        HttpEntity<String> entity = new HttpEntity<>(String.format(config.getProperty("edt.api.intentionToBuy.parameters"),
                isbn, storeDomain, customerId, appId), headers);
        CodeOnlyResposeEDT intentionToBuyResponse = postToEdt(entity, url, CodeOnlyResposeEDT.class);
        if (!intentionToBuyResponse.getResultCode().equals(1)) {
            throw new PimsleurException("intention to buy failed!");
        }
    }
}
