
package com.simonschuster.pimsleur.unlimited.data.domain.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "saveId",
        "lastChangeTimestamp",
        "lastChangeDeviceId",
        "subCode",
        "customer",
        "registrant"
})
public class ResultData {

    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("customer")
    public Customer getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
