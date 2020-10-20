
package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private Integer customersId;
    private String identityVerificationToken;
    private String pendingPurchasesFromStoreDomains;
    private List<CustomersOrder> customersOrders = new ArrayList<>();
    private List<AuthDescriptor> authDescriptors = new ArrayList<>();

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

    public List<String> getPUProductCodes() {
        return this.getCustomersOrders().stream()
                .flatMap(order -> order.getOrdersProducts()
                        .stream()
                        .filter(OrdersProduct::isNotHowToLearnForeignLang)
                        .filter(OrdersProduct::isPUProduct)
                        // we ignore "how to learn a foreign language"
                        .map(ordersProduct -> ordersProduct.getProduct().getProductCode()))
                .distinct()
                .collect(toList());
    }

    public List<String> getPCMProductCodes() {
        return this.getCustomersOrders().stream()
                .flatMap(order -> order.getOrdersProducts()
                        .stream()
                        .filter(OrdersProduct::isNotHowToLearnForeignLang)
                        .filter(OrdersProduct -> !OrdersProduct.isPUProduct())
                        // we ignore "how to learn a foreign language"
                        .map(product -> product.getProduct().getProductCode()))
                .distinct()
                .collect(toList());
    }

    public List<OrdersProduct> getAllOrdersProducts() {
        return getCustomersOrders()
                .stream()
                .peek(customersOrder -> {
                    String storeDomain = customersOrder.getStoreDomain();
                    customersOrder.setOrdersProducts(
                            customersOrder.getOrdersProducts().stream().map(ordersProduct -> {
                                ordersProduct.setStoreDomain(storeDomain);
                                return ordersProduct;
                            }).collect(toList()));
                })
                .flatMap(customersOrder -> customersOrder.getOrdersProducts().stream())
                .collect(toList());
    }


    public boolean hasPendingAndroid() {
        return this.pendingPurchasesFromStoreDomains != null && this.pendingPurchasesFromStoreDomains.contains("android");
    }

    public boolean hasPendingIos() {
        return this.pendingPurchasesFromStoreDomains != null && this.pendingPurchasesFromStoreDomains.contains("ios");
    }

    public Integer getCustomersId() {
        return customersId;
    }

    public void setCustomersId(Integer customersId) {
        this.customersId = customersId;
    }

    public String getIdentityVerificationToken() {
        return identityVerificationToken;
    }

    public void setIdentityVerificationToken(String identityVerificationToken) {
        this.identityVerificationToken = identityVerificationToken;
    }

    public String getPendingPurchasesFromStoreDomains() {
        return pendingPurchasesFromStoreDomains;
    }

    public void setPendingPurchasesFromStoreDomains(String pendingPurchasesFromStoreDomains) {
        this.pendingPurchasesFromStoreDomains = pendingPurchasesFromStoreDomains;
    }

    public List<CustomersOrder> getCustomersOrders() {
        return customersOrders;
    }

    public void setCustomersOrders(
        List<CustomersOrder> customersOrders) {
        this.customersOrders = customersOrders;
    }

    public List<AuthDescriptor> getAuthDescriptors() {
        return authDescriptors;
    }

    public void setAuthDescriptors(
        List<AuthDescriptor> authDescriptors) {
        this.authDescriptors = authDescriptors;
    }
}
