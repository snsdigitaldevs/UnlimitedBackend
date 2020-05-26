package com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result_code",
    "result_data"
})
public class PcmFreeCourseResponse extends EdtResponseCode {

    @JsonProperty("result_data")
    private PcmFreeCourseResultData pcmFreeCourseResultData;

    @JsonProperty("result_data")
    public Optional<PcmFreeCourseResultData> getResultData() {
        return Optional.ofNullable(pcmFreeCourseResultData);
    }

    @JsonProperty("result_data")
    public void setResultData(PcmFreeCourseResultData pcmFreeCourseResultData) {
        this.pcmFreeCourseResultData = pcmFreeCourseResultData;
    }

}
