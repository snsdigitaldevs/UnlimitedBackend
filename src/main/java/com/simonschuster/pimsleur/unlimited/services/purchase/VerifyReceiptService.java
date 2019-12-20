package com.simonschuster.pimsleur.unlimited.services.purchase;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptBody;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.verifyReceipt.VerifyReceipt;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.utils.EdtErrorCodeUtil;
import com.simonschuster.pimsleur.unlimited.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
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

    private String singleAction = "kljh";
    private String multipleAction = "gfds";

    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    public VerifyReceiptDTO verifyReceipt(VerifyReceiptBody verifyReceiptBody, String customerId)
        throws UnsupportedEncodingException {
        if (StringUtils.isBlank(verifyReceiptBody.getReceipt())) {
            LOG.error("receipt is empty, customerId is {}, verify body is {}", customerId,
                JsonUtils.toJsonString(verifyReceiptBody));
        }
        HttpEntity<String> entity = createPostBody(verifyReceiptBody, customerId);
        VerifyReceipt verifyReceiptResponse =
            postToEdt(entity, config.getProperty("edt.api.verifyReceipt.url"), VerifyReceipt.class);
        int resultCode = verifyReceiptResponse.getResultCode();
        if (resultCode != EdtResponseCode.RESULT_OK) {
            logError(resultCode, verifyReceiptBody, customerId);
            EdtErrorCodeUtil
                .throwError(verifyReceiptResponse.getResultCode(), "verify receipt failed!");
        }
        logSuccess(verifyReceiptBody, customerId);
        return verifyReceiptResponse.fomartToDOT();
    }

    private HttpEntity<String> createPostBody(VerifyReceiptBody verifyReceiptBody, String customerId)
            throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String appVersion = verifyReceiptBody.getAppVersion();
        String storeDomain = verifyReceiptBody.getStoreDomain();
        String transactionResult = encode(verifyReceiptBody.getTransactionResult(), "UTF-8");
        String receipt = encode(verifyReceiptBody.getReceipt(), "UTF-8");
        String activeAppVersion = config.getProperty("verifyReceipt.appVersion");
        String verifyReceiptProperty = config.getProperty("edt.api.verifyReceipt.parameters");
        //if app is in apple evaluation process, purchase is verified by apple test environment
        boolean verifyPurchaseInAppleTestEnv = (appVersion.equals(activeAppVersion)) && storeDomain.toLowerCase().contains("ios");

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

    private void logError(Integer resultCode, VerifyReceiptBody verifyReceiptBody,
        String customerId) {
        if (verifyReceiptBody.getIsMultiple()) {
            LOG.error(String
                .format(
                    "Restore failed! CustomerId is %s, resultCode is %s, and VerifyReceiptBody is %s",
                    customerId, resultCode, JsonUtils.toJsonString(verifyReceiptBody)));
        } else {
            LOG.error(String
                .format(
                    "Verify failed! CustomerId is %s,resultCode is %s, and VerifyReceiptBody is %s",
                    customerId, resultCode, JsonUtils.toJsonString(verifyReceiptBody)));
        }
    }

    private void logSuccess(VerifyReceiptBody verifyReceiptBody, String customerId) {
        if (verifyReceiptBody.getIsMultiple()) {
            LOG.info(String
                .format("Restore success! CustomerId is %s", customerId));
        } else {
            LOG.info(String
                .format("Verify success! CustomerId is %s", customerId));
        }
    }
}

