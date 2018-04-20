
package com.simonschuster.pimsleur.unlimited.data.domain.syncState;

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
    private Double value;
    @JsonProperty("stateChangeType")
    private String stateChangeType;

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("value")
    public Double getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Double value) {
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

}
