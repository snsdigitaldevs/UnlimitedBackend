package com.simonschuster.pimsleur.unlimited.services.purchase;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptBody;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.verifyReceipt.VerifyReceiptResponse;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.utils.JsonUtils;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.net.URLEncoder.encode;

@Service
public class VerifyReceiptService {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyReceiptService.class);
    private static final int MAX_TIME = 3;
    private static final long SLEEP_UNIT = 500;
    private static final String RESTORE_SUCCESS = "Restore success! CustomerId is {} and VerifyReceiptBody is {}";
    private static final String VERIFY_SUCCESS = "Verify success! CustomerId is {} and VerifyReceiptBody is {}";
    private static final String RESTORE_FAILED = "Restore failed! resultCode is {} CustomerId is {} and VerifyReceiptBody is {}";
    private static final String VERIFY_FAILED = "Verify failed! resultCode is {} CustomerId is {} and VerifyReceiptBody is {}";

    private String singleAction = "kljh";
    private String multipleAction = "gfds";

    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    public VerifyReceiptDTO verifyReceipt(VerifyReceiptBody verifyReceiptBody, String customerId)
        throws UnsupportedEncodingException {
        HttpEntity<String> entity = createPostBody(verifyReceiptBody, customerId);
        VerifyReceiptResponse verifyReceiptResponse =
            postToEdt(entity, config.getProperty("edt.api.verifyReceipt.url"), VerifyReceiptResponse.class);
        int resultCode = verifyReceiptResponse.getResultCode();
        int retryTimes = 0;
        logVerifyResult(resultCode, verifyReceiptBody, customerId, retryTimes);
        while (resultCode == EdtResponseCode.RESULT_APP_STORE_ERROR && retryTimes++ < MAX_TIME) {
            try {
                TimeUnit.MICROSECONDS.sleep(retryTimes * SLEEP_UNIT);
            } catch (InterruptedException e) {
                LOG.error("verify receipt sleep error", e);
            }
            verifyReceiptResponse = postToEdt(entity,
                config.getProperty("edt.api.verifyReceipt.url"), VerifyReceiptResponse.class);
            resultCode = verifyReceiptResponse.getResultCode();
            retryTimes++;
            logVerifyResult(resultCode, verifyReceiptBody, customerId, retryTimes);
        }
        return VerifyReceiptDTO.fromVerifyResponse(verifyReceiptResponse);
    }

    private HttpEntity<String> createPostBody(VerifyReceiptBody verifyReceiptBody,
        String customerId) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String appVersion = verifyReceiptBody.getAppVersion();
        String storeDomain = verifyReceiptBody.getStoreDomain();
        String transactionResult = encode(verifyReceiptBody.getTransactionResult(), "UTF-8");
        String receipt = encode(verifyReceiptBody.getReceipt(), "UTF-8");
        String activeAppVersion = config.getProperty("verifyReceipt.appVersion");
        String verifyReceiptProperty = config.getProperty("edt.api.verifyReceipt.parameters");
        //if app is in apple evaluation process, purchase is verified by apple test environment
        boolean verifyPurchaseInAppleTestEnv =
            (appVersion.equals(activeAppVersion)) && storeDomain.toLowerCase().contains("ios");

        if (verifyPurchaseInAppleTestEnv) {
            String format = String.format(verifyReceiptProperty,
                storeDomain,
                customerId,
                appIdService.getAppId(storeDomain),
                transactionResult,
                receipt,
                verifyReceiptBody.getIsMultiple() ? multipleAction : singleAction,
                1
            );
            return new HttpEntity<>(format, headers);
        }
        verifyReceiptProperty = verifyReceiptProperty.replace("&glft=%d", "");
        String format = String.format(verifyReceiptProperty,
            storeDomain,
            customerId,
            appIdService.getAppId(storeDomain),
            transactionResult,
            receipt,
            verifyReceiptBody.getIsMultiple() ? multipleAction : singleAction
        );
        return new HttpEntity<>(format, headers);
    }


    private void logVerifyResult(int resultCode, VerifyReceiptBody verifyReceiptBody,
        String customerId, int retryTimes) {
        LOG.info("verify receipt retry {} times", retryTimes);
        if (resultCode == EdtResponseCode.RESULT_OK) {
            if (verifyReceiptBody.getIsMultiple()) {
                LOG.info(RESTORE_SUCCESS, customerId, JsonUtils.toJsonString(verifyReceiptBody));
            } else {
                LOG.info(VERIFY_SUCCESS, customerId, JsonUtils.toJsonString(verifyReceiptBody));
            }
        } else {
            if (verifyReceiptBody.getIsMultiple()) {
                LOG.error(RESTORE_FAILED, resultCode, customerId,
                    JsonUtils.toJsonString(verifyReceiptBody));
            } else {
                LOG.error(VERIFY_FAILED, resultCode, customerId,
                    JsonUtils.toJsonString(verifyReceiptBody));
            }
        }
    }
}

