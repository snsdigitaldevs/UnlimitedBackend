
package com.simonschuster.pimsleur.unlimited.data.domain.customer;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "activationId",
        "registrantId",
        "activationDate",
        "productCode",
        "activationCountDesktop",
        "activationCountMobile",
        "totalActivationsAllowed",
        "desktopActivationsAllowed",
        "mobileActivationsAllowed",
        "optinProductUpdateInfo",
        "isDemoActivation",
        "registerableProduct"
})
public class ProductActivation {

    @JsonProperty("activationId")
    private Integer activationId;
    @JsonProperty("registrantId")
    private Integer registrantId;
    @JsonProperty("activationDate")
    private String activationDate;
    @JsonProperty("productCode")
    private String productCode;
    @JsonProperty("activationCountDesktop")
    private Integer activationCountDesktop;
    @JsonProperty("activationCountMobile")
    private Integer activationCountMobile;
    @JsonProperty("totalActivationsAllowed")
    private Integer totalActivationsAllowed;
    @JsonProperty("desktopActivationsAllowed")
    private Integer desktopActivationsAllowed;
    @JsonProperty("mobileActivationsAllowed")
    private Integer mobileActivationsAllowed;
    @JsonProperty("optinProductUpdateInfo")
    private Integer optinProductUpdateInfo;
    @JsonProperty("isDemoActivation")
    private Integer isDemoActivation;
    @JsonProperty("registerableProduct")
    private RegisterableProduct registerableProduct;

    @JsonProperty("activationId")
    public Integer getActivationId() {
        return activationId;
    }

    @JsonProperty("activationId")
    public void setActivationId(Integer activationId) {
        this.activationId = activationId;
    }

    @JsonProperty("registrantId")
    public Integer getRegistrantId() {
        return registrantId;
    }

    @JsonProperty("registrantId")
    public void setRegistrantId(Integer registrantId) {
        this.registrantId = registrantId;
    }

    @JsonProperty("activationDate")
    public String getActivationDate() {
        return activationDate;
    }

    @JsonProperty("activationDate")
    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    @JsonProperty("productCode")
    public String getProductCode() {
        return productCode;
    }

    @JsonProperty("productCode")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @JsonProperty("activationCountDesktop")
    public Integer getActivationCountDesktop() {
        return activationCountDesktop;
    }

    @JsonProperty("activationCountDesktop")
    public void setActivationCountDesktop(Integer activationCountDesktop) {
        this.activationCountDesktop = activationCountDesktop;
    }

    @JsonProperty("activationCountMobile")
    public Integer getActivationCountMobile() {
        return activationCountMobile;
    }

    @JsonProperty("activationCountMobile")
    public void setActivationCountMobile(Integer activationCountMobile) {
        this.activationCountMobile = activationCountMobile;
    }

    @JsonProperty("totalActivationsAllowed")
    public Integer getTotalActivationsAllowed() {
        return totalActivationsAllowed;
    }

    @JsonProperty("totalActivationsAllowed")
    public void setTotalActivationsAllowed(Integer totalActivationsAllowed) {
        this.totalActivationsAllowed = totalActivationsAllowed;
    }

    @JsonProperty("desktopActivationsAllowed")
    public Integer getDesktopActivationsAllowed() {
        return desktopActivationsAllowed;
    }

    @JsonProperty("desktopActivationsAllowed")
    public void setDesktopActivationsAllowed(Integer desktopActivationsAllowed) {
        this.desktopActivationsAllowed = desktopActivationsAllowed;
    }

    @JsonProperty("mobileActivationsAllowed")
    public Integer getMobileActivationsAllowed() {
        return mobileActivationsAllowed;
    }

    @JsonProperty("mobileActivationsAllowed")
    public void setMobileActivationsAllowed(Integer mobileActivationsAllowed) {
        this.mobileActivationsAllowed = mobileActivationsAllowed;
    }

    @JsonProperty("optinProductUpdateInfo")
    public Integer getOptinProductUpdateInfo() {
        return optinProductUpdateInfo;
    }

    @JsonProperty("optinProductUpdateInfo")
    public void setOptinProductUpdateInfo(Integer optinProductUpdateInfo) {
        this.optinProductUpdateInfo = optinProductUpdateInfo;
    }

    @JsonProperty("isDemoActivation")
    public Integer getIsDemoActivation() {
        return isDemoActivation;
    }

    @JsonProperty("isDemoActivation")
    public void setIsDemoActivation(Integer isDemoActivation) {
        this.isDemoActivation = isDemoActivation;
    }

    @JsonProperty("registerableProduct")
    public RegisterableProduct getRegisterableProduct() {
        return registerableProduct;
    }

    @JsonProperty("registerableProduct")
    public void setRegisterableProduct(RegisterableProduct registerableProduct) {
        this.registerableProduct = registerableProduct;
    }
}
