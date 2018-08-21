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

    public void setSubUserID(String subUserID) {
        this.subUserID = subUserID;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ProgressDTO) {
            ProgressDTO other1 = (ProgressDTO) other;
            return isEquals(other1.getCurrent(), getCurrent()) &&
                    isEquals(other1.getCompleted(), getCompleted()) &&
                    isEquals(other1.getLastPlayedDate(), getLastPlayedDate()) &&
                    isEquals(other1.getLastPlayHeadLocation(), getLastPlayHeadLocation()) &&
                    isEquals(other1.getProductCode(), getProductCode()) &&
                    isEquals(other1.getMediaItemId(), getMediaItemId());
        }
        return false;
    }

    private boolean isEquals(Object left, Object right) {
        if(left == null && right == null)
            return true;
        if(left != null && right != null){
            return left.equals(right);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int completed_hashno = (getCompleted() == null ? 0 : getCompleted().hashCode());
        int mediaItemId_hashno = (getMediaItemId() == null ? 0 : getMediaItemId().hashCode());
        int current_hashno = (getCurrent() == null ? 0 : getCurrent().hashCode());
        int lastPlayDate_hashno = (getLastPlayedDate() == null ? 0 : getLastPlayedDate().hashCode());
        int productCode_hashno = (getProductCode() == null ? 0 : getProductCode().hashCode());
        int playHead_hashno = (getLastPlayHeadLocation() == null ? 0 : getLastPlayHeadLocation().hashCode());
        int hashno = completed_hashno +
                mediaItemId_hashno +
                current_hashno +
                lastPlayDate_hashno +
                productCode_hashno +
                playHead_hashno;
        return hashno;
    }
}
