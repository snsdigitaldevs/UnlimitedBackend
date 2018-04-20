
package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "customersId",
        "identityVerificationToken",
        "customersOrders"
})
public class Customer {

    @JsonProperty("customersId")
    private Integer customersId;

    @JsonProperty("identityVerificationToken")
    private String identityVerificationToken;

    @JsonProperty("customersOrders")
    private List<CustomersOrder> customersOrders = null;

    @JsonProperty("customersId")
    public Integer getCustomersId() {
        return customersId;
    }

    @JsonProperty("customersId")
    public void setCustomersId(Integer customersId) {
        this.customersId = customersId;
    }

    @JsonProperty("identityVerificationToken")
    public String getIdentityVerificationToken() {
        return identityVerificationToken;
    }

    @JsonProperty("identityVerificationToken")
    public void setIdentityVerificationToken(String identityVerificationToken) {
        this.identityVerificationToken = identityVerificationToken;
    }

    @JsonProperty("customersOrders")
    public List<CustomersOrder> getCustomersOrders() {
        return customersOrders;
    }

    @JsonProperty("customersOrders")
    public void setCustomersOrders(List<CustomersOrder> customersOrders) {
        this.customersOrders = customersOrders;
    }

}
