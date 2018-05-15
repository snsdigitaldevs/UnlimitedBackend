package com.simonschuster.pimsleur.unlimited.data.edt.customerinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerInfo {

    private ResultData result_data;
    private int result_code;

    public ResultData getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultData result_data) {
        this.result_data = result_data;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

}
