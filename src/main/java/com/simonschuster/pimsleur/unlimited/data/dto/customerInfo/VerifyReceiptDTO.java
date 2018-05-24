package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

public class VerifyReceiptDTO {
    private Boolean purchaseStatusDidChange;

    public VerifyReceiptDTO(Boolean purchaseStatusDidChange) {
        this.purchaseStatusDidChange = purchaseStatusDidChange;
    }

    public Boolean getPurchaseStatusDidChange() {
        return purchaseStatusDidChange;
    }
}
