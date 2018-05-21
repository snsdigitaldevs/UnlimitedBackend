package com.simonschuster.pimsleur.unlimited.services.course;

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

    public void intentionToBuy(String customerId, String isbn, String storeDomain) {
        String url = config.getProperty("edt.api.intentionToBuyService.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(String.format(config.getProperty("edt.api.intentionToBuyService.parameters"),
                isbn, storeDomain, customerId), headers);
        CodeOnlyResposeEDT intentionToBuyResponse = postToEdt(entity, url, CodeOnlyResposeEDT.class);
        if (!intentionToBuyResponse.getResultCode().equals(1)) {
            throw new PimsleurException("intention to buy failed!");
        }
    }
}
