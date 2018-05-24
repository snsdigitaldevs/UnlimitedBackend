package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptBody;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.verifyReceipt.VerifyReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class VerifyReceiptService {
    @Autowired
    private ApplicationConfiguration config;

    public VerifyReceiptDTO verifyReceipt(VerifyReceiptBody verifyReceiptBody, String customerId) {
        String url = config.getProperty("edt.api.verifyReceipt.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(String.format(config.getProperty("edt.api.verifyReceipt.parameters"),
                verifyReceiptBody.getStoreDomain(), customerId, verifyReceiptBody.getTransactionResult()), headers);
        VerifyReceipt verifyReceiptResponse = postToEdt(entity, url, VerifyReceipt.class);
        if (!verifyReceiptResponse.getResultCode().equals(1)) {
            throw new PimsleurException("verify receipt failed!");
        }
        return verifyReceiptResponse.fomartToDOT();
    }
}
