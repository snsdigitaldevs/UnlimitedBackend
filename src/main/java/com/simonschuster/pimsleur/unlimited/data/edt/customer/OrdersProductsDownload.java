package com.simonschuster.pimsleur.unlimited.data.edt.customer;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdersProductsDownload {

    private String ordersProductsDownloadId;
    private String ordersId;
    private int customersId;
    private String ordersProductsId;
    private int productsId;
    private String ordersProductsAttributesId;
    private String ordersProductsFileName;
    private String downloadName;
    private String downloadMaxDays;
    private String downloadCount;
    private String downloadTries;
    private int mediaSetId;
    private MediaSet mediaSet;
    private String entitlementToken;

    public String getOrdersProductsDownloadId() {
        return ordersProductsDownloadId;
    }

    public void setOrdersProductsDownloadId(String ordersProductsDownloadId) {
        this.ordersProductsDownloadId = ordersProductsDownloadId;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public int getCustomersId() {
        return customersId;
    }

    public void setCustomersId(int customersId) {
        this.customersId = customersId;
    }

    public String getOrdersProductsId() {
        return ordersProductsId;
    }

    public void setOrdersProductsId(String ordersProductsId) {
        this.ordersProductsId = ordersProductsId;
    }

    public int getProductsId() {
        return productsId;
    }

    public void setProductsId(int productsId) {
        this.productsId = productsId;
    }

    public String getOrdersProductsAttributesId() {
        return ordersProductsAttributesId;
    }

    public void setOrdersProductsAttributesId(String ordersProductsAttributesId) {
        this.ordersProductsAttributesId = ordersProductsAttributesId;
    }

    public String getOrdersProductsFileName() {
        return ordersProductsFileName;
    }

    public void setOrdersProductsFileName(String ordersProductsFileName) {
        this.ordersProductsFileName = ordersProductsFileName;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    public String getDownloadMaxDays() {
        return downloadMaxDays;
    }

    public void setDownloadMaxDays(String downloadMaxDays) {
        this.downloadMaxDays = downloadMaxDays;
    }

    public String getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(String downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getDownloadTries() {
        return downloadTries;
    }

    public void setDownloadTries(String downloadTries) {
        this.downloadTries = downloadTries;
    }

    public int getMediaSetId() {
        return mediaSetId;
    }

    public void setMediaSetId(int mediaSetId) {
        this.mediaSetId = mediaSetId;
    }

    public MediaSet getMediaSet() {
        return mediaSet;
    }

    public void setMediaSet(MediaSet mediaSet) {
        this.mediaSet = mediaSet;
    }

    public String getEntitlementToken() {
        return entitlementToken;
    }

    public void setEntitlementToken(String entitlementToken) {
        this.entitlementToken = entitlementToken;
    }

}