package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

public class VerifyReceiptBody {
    private String storeDomain;
    private String transactionResult;
    private String receipt;

    public String getStoreDomain() {
        return storeDomain;
    }

    public String getTransactionResult() {
        return transactionResult;
    }

    public String getReceipt() {
        return receipt;
    }
}
