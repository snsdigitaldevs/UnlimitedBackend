
package com.simonschuster.pimsleur.unlimited.data.domain.customer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ordersId",
    "customersId",
    "storeDomain",
    "ordersProducts"
})
public class CustomersOrder {

    @JsonProperty("ordersId")
    private String ordersId;
    @JsonProperty("customersId")
    private Integer customersId;

    @JsonProperty("storeDomain")
    private String storeDomain;
    @JsonProperty("ordersProducts")
    private List<OrdersProduct> ordersProducts = null;

    @JsonProperty("ordersId")
    public String getOrdersId() {
        return ordersId;
    }

    @JsonProperty("ordersId")
    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    @JsonProperty("customersId")
    public Integer getCustomersId() {
        return customersId;
    }

    @JsonProperty("customersId")
    public void setCustomersId(Integer customersId) {
        this.customersId = customersId;
    }

    @JsonProperty("storeDomain")
    public String getStoreDomain() {
        return storeDomain;
    }

    @JsonProperty("storeDomain")
    public void setStoreDomain(String storeDomain) {
        this.storeDomain = storeDomain;
    }

    @JsonProperty("ordersProducts")
    public List<OrdersProduct> getOrdersProducts() {
        return ordersProducts;
    }

    @JsonProperty("ordersProducts")
    public void setOrdersProducts(List<OrdersProduct> ordersProducts) {
        this.ordersProducts = ordersProducts;
    }

}
