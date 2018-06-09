package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import java.util.List;

public class PcmAudioReqParams {

    private Integer customersId;

    private String customerToken;

    private List<MediaSetByLevel> entitlementTokens;

    private List<MediaItemsByLevel> mediaItemIds;

    public String getCustomerToken() {
        return customerToken;
    }

    public void setCustomerToken(String customerToken) {
        this.customerToken = customerToken;
    }

    public Integer getCustomersId() {
        return customersId;
    }

    public void setCustomersId(Integer customersId) {
        this.customersId = customersId;
    }

    public void setMediaItemIds(List<MediaItemsByLevel> mediaItemIds) {
        this.mediaItemIds = mediaItemIds;
    }

    public List<MediaItemsByLevel> getMediaItemIds() {
        return mediaItemIds;
    }

    public List<MediaSetByLevel> getEntitlementTokens() {
        return entitlementTokens;
    }

    public void setEntitlementTokens(List<MediaSetByLevel> entitlementTokens) {
        this.entitlementTokens = entitlementTokens;
    }
}
