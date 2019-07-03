package com.simonschuster.pimsleur.unlimited.data.dto.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MG2Offer {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("DryRunAmount")
    private Float dryRunAmount;

    @JsonProperty("SubscriptionLevel")
    private Integer subscriptionLevel;

    @JsonProperty("Products")
    private List<MG2Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getDryRunAmount() {
        return dryRunAmount;
    }

    public void setDryRunAmount(Float dryRunAmount) {
        this.dryRunAmount = dryRunAmount;
    }

    public Integer getSubscriptionLevel() {
        return subscriptionLevel;
    }

    public void setSubscriptionLevel(Integer subscriptionLevel) {
        this.subscriptionLevel = subscriptionLevel;
    }

    public List<MG2Product> getProducts() {
        return products;
    }

    public void setProducts(List<MG2Product> products) {
        this.products = products;
    }
}
