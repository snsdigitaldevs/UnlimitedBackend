package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptBody;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.verifyReceipt.VerifyReceipt;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.utils.EdtErrorCodeUtil;
import org.apache.commons.lang3.StringUtils;
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

    private String singleAction = "kljh";
    private String multipleAction = "gfds";

    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    public VerifyReceiptDTO verifyReceipt(VerifyReceiptBody verifyReceiptBody, String customerId)
            throws UnsupportedEncodingException {
        HttpEntity<String> entity = createPostBody(verifyReceiptBody, customerId);
        VerifyReceipt verifyReceiptResponse =
                postToEdt(entity, config.getProperty("edt.api.verifyReceipt.url"), VerifyReceipt.class);
        if (!verifyReceiptResponse.getResultCode().equals(1)) {
            EdtErrorCodeUtil.throwError(verifyReceiptResponse.getResultCode(), "verify receipt failed!");
        }
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
        boolean verifyPurchaseInAppleTestEnv = mainVersionEquals(appVersion, activeAppVersion) && storeDomain.toLowerCase().contains("ios");

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

    private boolean mainVersionEquals(String appVersion, String activeAppVersion) {
        int matchVersions = StringUtils.countMatches(appVersion, ".");
        if(matchVersions >= 2){
            int minVersionIndex = appVersion.indexOf(".", appVersion.indexOf(".") + 1);
            String mainVersion = appVersion.substring(0, minVersionIndex);
            return mainVersion.equals(activeAppVersion);
        }
        return appVersion.equals(activeAppVersion);
    }
}

