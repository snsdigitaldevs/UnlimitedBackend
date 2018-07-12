package com.simonschuster.pimsleur.unlimited.data.dto.sendEmailPermission;

public class EmailPermissionDto {
    String registrantId;
    Boolean allowSendEmail;
    String storeDomain;

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

    public Boolean getAllowSendEmail() {
        return allowSendEmail;
    }

    public void setAllowSendEmail(Boolean allowSendEmail) {
        this.allowSendEmail = allowSendEmail;
    }
}
