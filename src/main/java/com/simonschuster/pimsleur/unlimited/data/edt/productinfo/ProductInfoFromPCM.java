package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;

import java.util.List;

public class ProductInfoFromPCM {
    private List<OrdersProduct> ordersProductList;
    private OrdersProduct orderProduct;

    public List<OrdersProduct> getOrdersProductList() {
        return ordersProductList;
    }

    public void setOrdersProductList(List<OrdersProduct> ordersProductList) {
        this.ordersProductList = ordersProductList;
    }

    public void setOrderProduct(OrdersProduct orderProduct) {
        this.orderProduct = orderProduct;
    }

    public OrdersProduct getOrderProduct() {
        return orderProduct;
    }
}
