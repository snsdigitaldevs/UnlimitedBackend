package com.simonschuster.pimsleur.unlimited.data.dto.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DemandwareShopInfo {
    @JsonProperty("currency")
    private String currency;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private int price;

    @JsonProperty("fault")
    private FaultMessage fault;

    public DemandwareShopInfo() {
        this.setPrice(0);
        this.setCurrency("");
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public FaultMessage getFault() {
        return fault;
    }

    public void setFault(FaultMessage fault) {
        this.fault = fault;
    }
}
