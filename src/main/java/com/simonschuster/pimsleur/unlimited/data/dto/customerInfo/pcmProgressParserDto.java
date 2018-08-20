package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

public class pcmProgressParserDto {
    private String customerId;
    private String productCode;
    private String mediaItem;


    public pcmProgressParserDto(String customerId, String productCode, String mediaItem) {
        this.customerId = customerId;
        this.productCode = productCode;
        this.mediaItem = mediaItem;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getMediaItem() {
        return mediaItem;
    }
}
