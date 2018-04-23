
package com.simonschuster.pimsleur.unlimited.data.edt.syncState;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "key",
        "value",
        "stateChangeType"
})
public class UserAppStateDatum {

    @JsonProperty("key")
    private String key;
    @JsonProperty("value")
    private Object value;
    @JsonProperty("stateChangeType")
    private String stateChangeType;

    public UserAppStateDatum() {
    }

    public UserAppStateDatum(String key, Object value, String stateChangeType) {
        this.key = key;
        this.value = value;
        this.stateChangeType = stateChangeType;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("value")
    public Object getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Object value) {
        this.value = value;
    }

    @JsonProperty("stateChangeType")
    public String getStateChangeType() {
        return stateChangeType;
    }

    @JsonProperty("stateChangeType")
    public void setStateChangeType(String stateChangeType) {
        this.stateChangeType = stateChangeType;
    }

    public String idPartOfKey() {
        return this.getKey().split("#")[0];
    }
}
