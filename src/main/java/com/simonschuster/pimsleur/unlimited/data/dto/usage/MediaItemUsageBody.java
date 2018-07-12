package com.simonschuster.pimsleur.unlimited.data.dto.usage;

public class MediaItemUsageBody {
    private String identityVerificationToken;
    private String storeDomain;

    public String getStoreDomain() {
        return storeDomain;
    }

    public void setStoreDomain(String storeDomain) {
        this.storeDomain = storeDomain;
    }

    public String getIdentityVerificationToken() {
        return identityVerificationToken;
    }

    public void setIdentityVerificationToken(String identityVerificationToken) {
        this.identityVerificationToken = identityVerificationToken;
    }
}
