package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppDefinesOfCourseLevelDef {
    @JsonProperty("SCREEN_INFO")
    private ScreenInfo screenInfo;

    public ScreenInfo getScreenInfo() {
        return screenInfo;
    }

    public void setScreenInfo(ScreenInfo screenInfo) {
        this.screenInfo = screenInfo;
    }
}
