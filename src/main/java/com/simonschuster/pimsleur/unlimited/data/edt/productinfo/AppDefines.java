package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppDefines {
    @JsonProperty("defaultTransliterationMode")
    private Integer defaultTransliterationMode;

    @JsonProperty("russianAppDefineTest")
    private Object russianAppDefineTest;

    public Object getRussianAppDefineTest() {
        return russianAppDefineTest;
    }

    public void setRussianAppDefineTest(Object russianAppDefineTest) {
        this.russianAppDefineTest = russianAppDefineTest;
    }

    public Integer getDefaultTransliterationMode() {
        return defaultTransliterationMode;
    }

    public void setDefaultTransliterationMode(Integer defaultTransliterationMode) {
        this.defaultTransliterationMode = defaultTransliterationMode;
    }
}
