package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppDefines {
    @JsonProperty("defaultTransliterationMode")
    private Integer defaultTransliterationMode;

    public Integer getDefaultTransliterationMode() {
        return defaultTransliterationMode;
    }

    public void setDefaultTransliterationMode(Integer defaultTransliterationMode) {
        this.defaultTransliterationMode = defaultTransliterationMode;
    }
}
