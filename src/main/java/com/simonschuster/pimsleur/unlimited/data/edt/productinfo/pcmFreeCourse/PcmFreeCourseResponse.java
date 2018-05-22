package com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "result_code",
        "result_data"
})
public class PcmFreeCourseResponse {

    @JsonProperty("result_code")
    private long resultCode;
    @JsonProperty("result_data")
    private PcmFreeCourseResultData pcmFreeCourseResultData;

    @JsonProperty("result_code")
    public long getResultCode() {
        return resultCode;
    }

    @JsonProperty("result_code")
    public void setResultCode(long resultCode) {
        this.resultCode = resultCode;
    }

    @JsonProperty("result_data")
    public PcmFreeCourseResultData getResultData() {
        return pcmFreeCourseResultData;
    }

    @JsonProperty("result_data")
    public void setResultData(PcmFreeCourseResultData pcmFreeCourseResultData) {
        this.pcmFreeCourseResultData = pcmFreeCourseResultData;
    }

}
