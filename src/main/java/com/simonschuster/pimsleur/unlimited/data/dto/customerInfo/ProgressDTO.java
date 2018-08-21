package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

public class ProgressDTO {
    private String productCode;
    private Integer mediaItemId;

    private Boolean isCompleted = false;
    private Boolean isCurrent = false;

    private Long lastPlayedDate;
    private Double lastPlayHeadLocation;

    private String subUserID;

    public ProgressDTO() {
    }

    public ProgressDTO(Integer mediaItemId, String productCode, String subUserID, Boolean completed, Boolean current) {
        this.mediaItemId = mediaItemId;
        this.productCode = productCode;
        this.subUserID = subUserID;
        this.isCompleted = completed;
        this.isCurrent = current;
    }

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

    public String computeIdentifier(){
        return productCode + "_" + mediaItemId;
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

    public String getSubUserID() {
        return subUserID;
    }
}
