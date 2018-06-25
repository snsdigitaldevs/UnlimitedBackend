package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.CodeOnlyResponseEDT;
import com.simonschuster.pimsleur.unlimited.utils.EdtErrorCodeUtil;
import com.simonschuster.pimsleur.unlimited.utils.InAppPurchaseUtil;
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

    public void intentionToBuy(String customerId, String isbn, String storeDomain) {
        String url = config.getProperty("edt.api.intentionToBuy.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(String.format(config.getProperty("edt.api.intentionToBuy.parameters"),
                isbn, storeDomain, customerId, InAppPurchaseUtil.getAppId(storeDomain)), headers);
        CodeOnlyResponseEDT intentionToBuyResponse = postToEdt(entity, url, CodeOnlyResponseEDT.class);

        if (!intentionToBuyResponse.getResultCode().equals(1)) {
            EdtErrorCodeUtil.throwError(intentionToBuyResponse.getResultCode(), "intention to buy failed!");
        }
    }
}
