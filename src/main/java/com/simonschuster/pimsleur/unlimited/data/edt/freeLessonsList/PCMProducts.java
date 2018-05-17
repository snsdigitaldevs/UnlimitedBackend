
package com.simonschuster.pimsleur.unlimited.data.edt.freeLessonsList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "result_data",
        "result_code"
})
public class PCMProducts {

    @JsonProperty("result_data")
    private ResultData resultData;
    @JsonProperty("result_code")
    private long resultCode;

    @JsonProperty("result_data")
    public ResultData getResultData() {
        return resultData;
    }

    @JsonProperty("result_data")
    public void setResultData(ResultData resultData) {
        this.resultData = resultData;
    }

    @JsonProperty("result_code")
    public long getResultCode() {
        return resultCode;
    }

    @JsonProperty("result_code")
    public void setResultCode(long resultCode) {
        this.resultCode = resultCode;
    }

}
