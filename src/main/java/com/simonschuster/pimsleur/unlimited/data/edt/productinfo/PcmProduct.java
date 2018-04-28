package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;

import java.util.Map;

public class PcmProduct {
    private OrdersProduct orderProduct;
    private Integer customersId;

    private String customerToken;

    //<orderproductcode, infoForAllLevels>
    private Map<String, OrdersProduct> ordersProductList;

    public Map<String, OrdersProduct> getOrdersProductList() {
        return ordersProductList;
    }

    public void setOrdersProductList(Map<String, OrdersProduct> ordersProductList) {
        this.ordersProductList = ordersProductList;
    }

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
