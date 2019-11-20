package com.simonschuster.pimsleur.unlimited.services.purchase;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.utils.EdtErrorCodeUtil;
import com.simonschuster.pimsleur.unlimited.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class IntentionToBuyService {

    private static final Logger LOG = LoggerFactory.getLogger(IntentionToBuyService.class);
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

        if (intentionToBuyResponse.getResultCode() != EdtResponseCode.RESULT_OK) {
            LOG.error("intentionToBuy error, customerId is {}, isbn is {}, error is {}", customerId,
                isbn,
                JsonUtils.toJsonString(intentionToBuyResponse));
            EdtErrorCodeUtil
                .throwError(intentionToBuyResponse.getResultCode(), "intention to buy failed!");
        }
        LOG.info("intentionToBuy success, customerId is {}, isbn is {}", customerId, isbn);
    }
}
