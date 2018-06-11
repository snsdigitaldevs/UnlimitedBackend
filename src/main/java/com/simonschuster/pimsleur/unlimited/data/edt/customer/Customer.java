
package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

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

    @JsonProperty("pendingPurchasesFromStoreDomains")
    private String pendingPurchasesFromStoreDomains;

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

    public String getPendingPurchasesFromStoreDomains() {
        return pendingPurchasesFromStoreDomains;
    }

    public void setPendingPurchasesFromStoreDomains(String pendingPurchasesFromStoreDomains) {
        this.pendingPurchasesFromStoreDomains = pendingPurchasesFromStoreDomains;
    }

    public List<String> getProductCodes() {
        return this.getCustomersOrders().stream()
                .flatMap(order -> order.getOrdersProducts()
                        .stream()
                        .filter(OrdersProduct::isNotHowToLearnForeignLang)
                        // we ignore "how to learn a foreign language"
                        .map(product -> product.getProduct().getProductCode()))
                .distinct()
                .collect(toList());

    }

    public List<String> getSubscriptionProductCodes() {
        return this.getCustomersOrders().stream()
                .filter(order -> Objects.equals(order.getStoreDomain(), "pimsleur.com.mg2"))
                .flatMap(order -> order.getOrdersProducts().stream())
                .map(ordersProduct -> ordersProduct.getProduct().getProductCode())
                .collect(toList());
    }

    public List<OrdersProduct> getAllOrdersProducts() {
        return getCustomersOrders()
                .stream()
                .flatMap(customersOrder -> customersOrder.getOrdersProducts().stream())
                .collect(toList());
    }

    public boolean hasPendingAndroid() {
        return this.pendingPurchasesFromStoreDomains != null && this.pendingPurchasesFromStoreDomains.contains("android");
    }

    public boolean hasPendingIos() {
        return this.pendingPurchasesFromStoreDomains != null && this.pendingPurchasesFromStoreDomains.contains("ios");
    }
}
