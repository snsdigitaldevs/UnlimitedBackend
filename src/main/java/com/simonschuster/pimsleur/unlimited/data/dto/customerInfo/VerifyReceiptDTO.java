package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.verifyReceipt.VerifyReceiptResponse;

public class VerifyReceiptDTO {
    private boolean shouldUpdateReceipt;
    private boolean purchaseStatusDidChange;

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

    public static VerifyReceiptDTO fromVerifyResponse(VerifyReceiptResponse response) {
        VerifyReceiptDTO verifyReceiptDTO = new VerifyReceiptDTO();
        if (response.getResultCode() == EdtResponseCode.RESULT_OK) {
            verifyReceiptDTO
                .setPurchaseStatusDidChange(response.getResultData().getPurchaseStatusDidChange());
        } else if (response.getResultCode() == EdtResponseCode.RESULT_APPSTORE_RECEIPT_DATA_MISSING
            || response.getResultCode() == EdtResponseCode.RESULT_APPSTORE_VALIDATION_ERROR_RECEIPT_TOO_OLD) {
            verifyReceiptDTO.setShouldUpdateReceipt(true);
        }
        return verifyReceiptDTO;
    }
}
