package com.simonschuster.pimsleur.unlimited.data.dto.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MG2Result {
    @JsonProperty("Offers")
    private List<MG2Offer> offers;

    public List<MG2Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<MG2Offer> offers) {
        this.offers = offers;
    }
}
