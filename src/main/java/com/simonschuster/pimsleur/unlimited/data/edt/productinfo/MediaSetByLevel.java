package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

public class MediaSetByLevel {
    String level;
    String entitlementToken;
    Integer mediaSetId;

    public MediaSetByLevel(String level, String entitlementToken, Integer mediaSetId) {
        this.level = level;
        this.entitlementToken = entitlementToken;
        this.mediaSetId = mediaSetId;
    }

    public String getLevel() {
        return level;
    }

    public String getEntitlementToken() {
        return entitlementToken;
    }

    public Integer getMediaSetId() {
        return mediaSetId;
    }
}
