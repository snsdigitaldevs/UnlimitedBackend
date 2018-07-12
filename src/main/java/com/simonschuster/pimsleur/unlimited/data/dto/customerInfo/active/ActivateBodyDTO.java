package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active;

import java.util.List;

public class ActivateBodyDTO {
    private String identityVerificationToken;
    private List<String> isbns;
    private String storeDomain;

    public String getIdentityVerificationToken() {
        return identityVerificationToken;
    }

    public void setIdentityVerificationToken(String identityVerificationToken) {
        this.identityVerificationToken = identityVerificationToken;
    }

    public List<String> getIsbns() {
        return isbns;
    }

    public void setIsbns(List<String> isbns) {
        this.isbns = isbns;
    }

    public String getStoreDomain() {
        return storeDomain;
    }

    public void setStoreDomain(String storeDomain) {
        this.storeDomain = storeDomain;
    }
}
