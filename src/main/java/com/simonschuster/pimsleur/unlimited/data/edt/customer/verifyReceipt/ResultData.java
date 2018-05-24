
package com.simonschuster.pimsleur.unlimited.data.edt.customer.verifyReceipt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "purchaseStatusDidChange"
})
public class ResultData {

    @JsonProperty("purchaseStatusDidChange")
    private Boolean purchaseStatusDidChange;

    @JsonProperty("purchaseStatusDidChange")
    public Boolean getPurchaseStatusDidChange() {
        return purchaseStatusDidChange;
    }

    @JsonProperty("purchaseStatusDidChange")
    public void setPurchaseStatusDidChange(Boolean purchaseStatusDidChange) {
        this.purchaseStatusDidChange = purchaseStatusDidChange;
    }

}
