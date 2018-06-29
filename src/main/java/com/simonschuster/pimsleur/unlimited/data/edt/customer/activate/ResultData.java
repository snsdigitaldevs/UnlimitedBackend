package com.simonschuster.pimsleur.unlimited.data.edt.customer.activate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.ProductActivation;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Registrant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "registrant",
        "activation"
})
public class ResultData {
    @JsonProperty("registrant")
    private Registrant registrant;
    @JsonProperty("activation")
    private ProductActivation activation;

    @JsonProperty("registrant")
    public Registrant getRegistrant() {
        return registrant;
    }

    @JsonProperty("registrant")
    public void setRegistrant(Registrant registrant) {
        this.registrant = registrant;
    }

    @JsonProperty("activation")
    public ProductActivation getActivation() {
        return activation;
    }

    @JsonProperty("activation")
    public void setActivation(ProductActivation activation) {
        this.activation = activation;
    }
}
