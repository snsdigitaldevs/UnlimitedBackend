package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active;

public class DeactivateBodyDTO {
    private String registrantId;
    private String storeDomain;

    public String getStoreDomain() {
        return storeDomain;
    }

    public void setStoreDomain(String storeDomain) {
        this.storeDomain = storeDomain;
    }

    public String getRegistrantId() {
        return registrantId;
    }

    public void setRegistrantId(String registrantId) {
        this.registrantId = registrantId;
    }
}
