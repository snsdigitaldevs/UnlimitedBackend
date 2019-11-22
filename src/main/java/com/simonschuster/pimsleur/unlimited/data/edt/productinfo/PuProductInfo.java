package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;

public class PuProductInfo extends EdtResponseCode {

    //Mandatory field for correct response
    @JsonProperty("result_data")
    private ResultData resultData;

    @JsonProperty("result_data")
    public ResultData getResultData() {
        return resultData;
    }

    @JsonProperty("result_data")
    public void setResultData(ResultData resultData) {
        this.resultData = resultData;
    }
}
