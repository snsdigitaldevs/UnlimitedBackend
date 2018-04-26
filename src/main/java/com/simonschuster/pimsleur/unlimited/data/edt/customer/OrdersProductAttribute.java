package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersProductAttribute {

    private String ordersProductsAttributesId;
    private String ordersProductsId;
    private String ordersId;
    private String productsOptions;
    private String productsOptionsValues;
    private List<OrdersProductsDownload> ordersProductsDownloads;

    public String getOrdersProductsAttributesId() {
        return ordersProductsAttributesId;
    }

    public void setOrdersProductsAttributesId(String ordersProductsAttributesId) {
        this.ordersProductsAttributesId = ordersProductsAttributesId;
    }

    public String getOrdersProductsId() {
        return ordersProductsId;
    }

    public void setOrdersProductsId(String ordersProductsId) {
        this.ordersProductsId = ordersProductsId;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getProductsOptions() {
        return productsOptions;
    }

    public void setProductsOptions(String productsOptions) {
        this.productsOptions = productsOptions;
    }

    public String getProductsOptionsValues() {
        return productsOptionsValues;
    }

    public void setProductsOptionsValues(String productsOptionsValues) {
        this.productsOptionsValues = productsOptionsValues;
    }

    public List<OrdersProductsDownload> getOrdersProductsDownloads() {
        return ordersProductsDownloads;
    }

    public void setOrdersProductsDownloads(List<OrdersProductsDownload> ordersProductsDownloads) {
        this.ordersProductsDownloads = ordersProductsDownloads;
    }

}
