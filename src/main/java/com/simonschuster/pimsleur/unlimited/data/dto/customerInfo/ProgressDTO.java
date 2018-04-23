package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

public class ProgressDTO {
    private String productCode;
    private Integer mediaItemId;

    private Boolean isCompleted;
    private Long lastPlayedDate;
    private Double lastPlayHeadLocation;

    private Boolean isCurrent = false;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getMediaItemId() {
        return mediaItemId;
    }

    public void setMediaItemId(Integer mediaItemId) {
        this.mediaItemId = mediaItemId;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Long getLastPlayedDate() {
        return lastPlayedDate;
    }

    public void setLastPlayedDate(Long lastPlayedDate) {
        this.lastPlayedDate = lastPlayedDate;
    }

    public Double getLastPlayHeadLocation() {
        return lastPlayHeadLocation;
    }

    public void setLastPlayHeadLocation(Double lastPlayHeadLocation) {
        this.lastPlayHeadLocation = lastPlayHeadLocation;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }
}
