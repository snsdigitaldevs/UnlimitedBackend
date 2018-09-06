package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;

import java.util.List;

public class PcmProduct {
    private Integer customersId;
    private String customerToken;
    private List<OrdersProduct> ordersProducts;

    public List<OrdersProduct> getOrdersProducts() {
        return ordersProducts;
    }

    public void setOrdersProducts(List<OrdersProduct> ordersProducts) {
        this.ordersProducts = ordersProducts;
    }

    public void setCustomersId(Integer customersId) {
        this.customersId = customersId;
    }

    public Integer getCustomersId() {
        return customersId;
    }

    public void setCustomerToken(String identityVerificationToken) {
        customerToken = identityVerificationToken;
    }

    public String getCustomerToken() {
        return customerToken;
    }
}
