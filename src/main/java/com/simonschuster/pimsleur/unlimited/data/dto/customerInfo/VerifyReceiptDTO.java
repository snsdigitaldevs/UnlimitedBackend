package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.verifyReceipt.VerifyReceiptResponse;

import java.util.HashMap;

public class VerifyReceiptDTO {
    private boolean shouldUpdateReceipt;
    private boolean purchaseStatusDidChange;
    private HashMap<String, Object> purchaseRecord;

    public boolean isShouldUpdateReceipt() {
        return shouldUpdateReceipt;
    }

    public void setShouldUpdateReceipt(boolean shouldUpdateReceipt) {
        this.shouldUpdateReceipt = shouldUpdateReceipt;
    }

    public boolean isPurchaseStatusDidChange() {
        return purchaseStatusDidChange;
    }

    public void setPurchaseStatusDidChange(boolean purchaseStatusDidChange) {
        this.purchaseStatusDidChange = purchaseStatusDidChange;
    }

    public HashMap<String, Object> getPurchaseRecord() {
        return purchaseRecord;
    }

    public void setPurchaseRecord(HashMap<String, Object> purchaseRecord) {
        this.purchaseRecord = purchaseRecord;
    }

    public static VerifyReceiptDTO fromVerifyResponse(VerifyReceiptResponse response) {
        VerifyReceiptDTO verifyReceiptDTO = new VerifyReceiptDTO();
        if (response.getResultCode() == EdtResponseCode.RESULT_OK) {
            verifyReceiptDTO
                .setPurchaseStatusDidChange(response.getResultData().getPurchaseStatusDidChange());
            verifyReceiptDTO.setPurchaseRecord(response.getResultData().getPurchaseRecord());
        } else if (response.getResultCode() == EdtResponseCode.RESULT_APPSTORE_RECEIPT_DATA_MISSING) {
            verifyReceiptDTO.setShouldUpdateReceipt(true);
        }
        return verifyReceiptDTO;
    }
}
