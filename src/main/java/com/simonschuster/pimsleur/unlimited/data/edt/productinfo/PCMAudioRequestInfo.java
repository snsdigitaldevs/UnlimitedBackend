package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import java.util.Map;

public class PCMAudioRequestInfo {


    //<Level, entitlementToken>
    private Map<String, String> entitlementTokens;

    //<level, <lesson title, lesson media item id>>
    private Map<String, Map<String, Integer>> mediaItemIds;

    public void setMediaItemIds(Map<String, Map<String, Integer>> mediaItemIds) {
        this.mediaItemIds = mediaItemIds;
    }

    public Map<String, Map<String, Integer>> getMediaItemIds() {
        return mediaItemIds;
    }

    public Map<String, String> getEntitlementTokens() {
        return entitlementTokens;
    }

    public void setEntitlementTokens(Map<String, String> entitlementTokens) {
        this.entitlementTokens = entitlementTokens;
    }
}
