
package com.simonschuster.pimsleur.unlimited.data.edt.syncState.syncUp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "subCode",
        "lastSaveId"
})
public class ResultData {

    @JsonProperty("subCode")
    private long subCode;
    @JsonProperty("lastSaveId")
    private long lastSaveId;

    @JsonProperty("subCode")
    public long getSubCode() {
        return subCode;
    }

    @JsonProperty("subCode")
    public void setSubCode(long subCode) {
        this.subCode = subCode;
    }

    @JsonProperty("lastSaveId")
    public long getLastSaveId() {
        return lastSaveId;
    }

    @JsonProperty("lastSaveId")
    public void setLastSaveId(long lastSaveId) {
        this.lastSaveId = lastSaveId;
    }

}
