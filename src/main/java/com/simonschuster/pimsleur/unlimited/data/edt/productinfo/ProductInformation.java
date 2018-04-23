package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductInformation {
    @JsonProperty("result_code")
    private Integer resultCode;
    @JsonProperty("result_data")
    private ResultData resultData;

    @JsonProperty("result_code")
    public Integer getResultCode() {
        return resultCode;
    }

    @JsonProperty("result_code")
    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    @JsonProperty("result_data")
    public ResultData getResultData() {
        return resultData;
    }

    @JsonProperty("result_data")
    public void setResultData(ResultData resultData) {
        this.resultData = resultData;
    }
}
