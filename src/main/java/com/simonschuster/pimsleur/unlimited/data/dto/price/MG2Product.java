package com.simonschuster.pimsleur.unlimited.data.dto.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MG2Product {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Price")
    private Float price;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("SubscriptionLevel")
    private Integer subscriptionLevel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getSubscriptionLevel() {
        return subscriptionLevel;
    }

    public void setSubscriptionLevel(Integer subscriptionLevel) {
        this.subscriptionLevel = subscriptionLevel;
    }
}
