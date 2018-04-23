package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrefReadingIntroPlayed {
    @JsonProperty("key")
    private String key;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("hasDefaultAppDefine")
    private String hasDefaultAppDefine;
    @JsonProperty("valueType")
    private String valueType;
    @JsonProperty("resetValue")
    private String resetValue;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHasDefaultAppDefine() {
        return hasDefaultAppDefine;
    }

    public void setHasDefaultAppDefine(String hasDefaultAppDefine) {
        this.hasDefaultAppDefine = hasDefaultAppDefine;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getResetValue() {
        return resetValue;
    }

    public void setResetValue(String resetValue) {
        this.resetValue = resetValue;
    }
}
