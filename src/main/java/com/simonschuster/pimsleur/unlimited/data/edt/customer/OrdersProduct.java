
package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ordersProductsId",
        "ordersId",
        "productsId",
        "productsModel",
        "productsName",
        "finalPrice",
        "productsTax",
        "productsTaxLocalizedDesc",
        "productsTaxRate",
        "productsQuantity",
        "altProductSku",
        "productsOtherDiscount",
        "ordersProductsAttributes",
        "product"
})
public class OrdersProduct {

    @JsonProperty("ordersProductsId")
    private String ordersProductsId;
    @JsonProperty("ordersId")
    private String ordersId;
    @JsonProperty("productsId")
    private Integer productsId;
    @JsonProperty("productsModel")
    private String productsModel;
    @JsonProperty("productsName")
    private String productsName;
    @JsonProperty("finalPrice")
    private Integer finalPrice;
    @JsonProperty("productsTax")
    private Integer productsTax;
    @JsonProperty("productsTaxLocalizedDesc")
    private Integer productsTaxLocalizedDesc;
    @JsonProperty("productsTaxRate")
    private Integer productsTaxRate;
    @JsonProperty("productsQuantity")
    private Integer productsQuantity;
    @JsonProperty("altProductSku")
    private String altProductSku;
    @JsonProperty("productsOtherDiscount")
    private Integer productsOtherDiscount;
    @JsonProperty("ordersProductsAttributes")
    private List<OrdersProductAttribute> ordersProductsAttributes = null;
    @JsonProperty("product")
    private Product product;

    @JsonProperty("ordersProductsId")
    public String getOrdersProductsId() {
        return ordersProductsId;
    }

    @JsonProperty("ordersProductsId")
    public void setOrdersProductsId(String ordersProductsId) {
        this.ordersProductsId = ordersProductsId;
    }

    @JsonProperty("ordersId")
    public String getOrdersId() {
        return ordersId;
    }

    @JsonProperty("ordersId")
    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    @JsonProperty("productsId")
    public Integer getProductsId() {
        return productsId;
    }

    @JsonProperty("productsId")
    public void setProductsId(Integer productsId) {
        this.productsId = productsId;
    }

    @JsonProperty("productsModel")
    public String getProductsModel() {
        return productsModel;
    }

    @JsonProperty("productsModel")
    public void setProductsModel(String productsModel) {
        this.productsModel = productsModel;
    }

    @JsonProperty("productsName")
    public String getProductsName() {
        return productsName;
    }

    @JsonProperty("productsName")
    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    @JsonProperty("finalPrice")
    public Integer getFinalPrice() {
        return finalPrice;
    }

    @JsonProperty("finalPrice")
    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    @JsonProperty("productsTax")
    public Integer getProductsTax() {
        return productsTax;
    }

    @JsonProperty("productsTax")
    public void setProductsTax(Integer productsTax) {
        this.productsTax = productsTax;
    }

    @JsonProperty("productsTaxLocalizedDesc")
    public Integer getProductsTaxLocalizedDesc() {
        return productsTaxLocalizedDesc;
    }

    @JsonProperty("productsTaxLocalizedDesc")
    public void setProductsTaxLocalizedDesc(Integer productsTaxLocalizedDesc) {
        this.productsTaxLocalizedDesc = productsTaxLocalizedDesc;
    }

    @JsonProperty("productsTaxRate")
    public Integer getProductsTaxRate() {
        return productsTaxRate;
    }

    @JsonProperty("productsTaxRate")
    public void setProductsTaxRate(Integer productsTaxRate) {
        this.productsTaxRate = productsTaxRate;
    }

    @JsonProperty("productsQuantity")
    public Integer getProductsQuantity() {
        return productsQuantity;
    }

    @JsonProperty("productsQuantity")
    public void setProductsQuantity(Integer productsQuantity) {
        this.productsQuantity = productsQuantity;
    }

    @JsonProperty("altProductSku")
    public String getAltProductSku() {
        return altProductSku;
    }

    @JsonProperty("altProductSku")
    public void setAltProductSku(String altProductSku) {
        this.altProductSku = altProductSku;
    }

    @JsonProperty("productsOtherDiscount")
    public Integer getProductsOtherDiscount() {
        return productsOtherDiscount;
    }

    @JsonProperty("productsOtherDiscount")
    public void setProductsOtherDiscount(Integer productsOtherDiscount) {
        this.productsOtherDiscount = productsOtherDiscount;
    }

    @JsonProperty("ordersProductsAttributes")
    public List<OrdersProductAttribute> getOrdersProductsAttributes() {
        return ordersProductsAttributes;
    }

    @JsonProperty("ordersProductsAttributes")
    public void setOrdersProductsAttributes(List<OrdersProductAttribute> ordersProductsAttributes) {
        this.ordersProductsAttributes = ordersProductsAttributes;
    }

    @JsonProperty("product")
    public Product getProduct() {
        return product;
    }

    @JsonProperty("product")
    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isNotHowToLearnForeignLang() {
        // How to Learn a Foreign Language is a pcm course that you can get for free
        return getProduct().getProductsLevel() != 0;
    }
}
