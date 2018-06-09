package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public class PcmAudioReqParams {

    private Integer customersId;

    private String customerToken;

    //<Level, <entitlementToken, mediaSetId>>
    private Map<String, Pair<String, Integer>>entitlementTokens;

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

    public Map<String, Pair<String, Integer>> getEntitlementTokens() {
        return entitlementTokens;
    }

    public void setEntitlementTokens(Map<String, Pair<String, Integer>>entitlementTokens) {
        this.entitlementTokens = entitlementTokens;
    }
}
