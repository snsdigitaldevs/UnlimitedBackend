
package com.simonschuster.pimsleur.unlimited.data.edt.customer.verifyReceipt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "result_data",
        "result_code"
})
public class VerifyReceipt extends EdtResponseCode {

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

    public VerifyReceiptDTO fomartToDOT() {
        return new VerifyReceiptDTO(this.getResultData().getPurchaseStatusDidChange());
    }
}
