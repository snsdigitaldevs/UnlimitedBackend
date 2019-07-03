package com.simonschuster.pimsleur.unlimited.data.dto.price;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class PriceInfoDTO {
    private Float price;

    private String currency;

    private String name;

    private Integer subscriptionLevel;

    private String code;

    public PriceInfoDTO() {
    }

    public PriceInfoDTO(Float price, String currency, String name, String code) {
        this.price = price;
        this.currency = currency;
        this.name = name;
        this.code = code;
    }

    public PriceInfoDTO(Float price, String currency, String name, Integer subscriptionLevel, String code) {
        this.price = price;
        this.currency = currency;
        this.name = name;
        this.subscriptionLevel = subscriptionLevel;
        this.code = code;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSubscriptionLevel() {
        return subscriptionLevel;
    }

    public void setSubscriptionLevel(Integer subscriptionLevel) {
        this.subscriptionLevel = subscriptionLevel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
