
package com.simonschuster.pimsleur.unlimited.data.edt.syncState;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.emptyList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "subCode",
        "lastSaveId",
        "userAppStateData"
})
public class ResultData {

    @JsonProperty("subCode")
    private Integer subCode;
    @JsonProperty("lastSaveId")
    private Long lastSaveId;

    @JsonProperty("userAppStateData")
    private String userAppStateData = null;

    @JsonProperty("subCode")
    public Integer getSubCode() {
        return subCode;
    }

    @JsonProperty("subCode")
    public void setSubCode(Integer subCode) {
        this.subCode = subCode;
    }

    @JsonProperty("lastSaveId")
    public Long getLastSaveId() {
        return lastSaveId;
    }

    @JsonProperty("lastSaveId")
    public void setLastSaveId(Long lastSaveId) {
        this.lastSaveId = lastSaveId;
    }

    @JsonProperty("userAppStateData")
    public List<UserAppStateDatum> getUserAppStateData() throws IOException {
        if (userAppStateData != null) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(userAppStateData, new TypeReference<List<UserAppStateDatum>>() {
            });
        }
        return emptyList();
    }

}
