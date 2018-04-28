package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;

public class ProductInfoFromPCM {
    private OrdersProduct orderProduct;
    private Integer customersId;

    private String customerToken;

    public void setOrderProduct(OrdersProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    public OrdersProduct getOrderProduct() {
        return orderProduct;
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
