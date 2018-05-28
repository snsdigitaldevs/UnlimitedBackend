package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptBody;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.verifyReceipt.VerifyReceipt;
import com.simonschuster.pimsleur.unlimited.utils.EdtErrorCodeUtil;
import com.simonschuster.pimsleur.unlimited.utils.InAppPurchaseUtil;
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
    @Autowired
    private ApplicationConfiguration config;

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

        String storeDomain = verifyReceiptBody.getStoreDomain();
        String transactionResult = encode(verifyReceiptBody.getTransactionResult(), "UTF-8");
        String receipt = encode(verifyReceiptBody.getReceipt(), "UTF-8");
        return new HttpEntity<>(String.format(config.getProperty("edt.api.verifyReceipt.parameters"),
                storeDomain, customerId, InAppPurchaseUtil.getAppId(storeDomain), transactionResult, receipt
        ), headers);
    }
}
