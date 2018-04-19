
package com.simonschuster.pimsleur.unlimited.data.domain.customer;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "registrantId",
        "customersId",
        "registeredAppUserId",
        "optinNewProductInfo",
        "firstName",
        "acceptTermsAndConditions",
        "appUsers",
        "productActivations"
})
public class Registrant {

    @JsonProperty("registrantId")
    private Integer registrantId;
    @JsonProperty("customersId")
    private Integer customersId;
    @JsonProperty("registeredAppUserId")
    private String registeredAppUserId;
    @JsonProperty("optinNewProductInfo")
    private Integer optinNewProductInfo;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("acceptTermsAndConditions")
    private Integer acceptTermsAndConditions;
    @JsonProperty("appUsers")
    private List<AppUser> appUsers = null;
    @JsonProperty("productActivations")
    private List<ProductActivation> productActivations = null;

    @JsonProperty("registrantId")
    public Integer getRegistrantId() {
        return registrantId;
    }

    @JsonProperty("registrantId")
    public void setRegistrantId(Integer registrantId) {
        this.registrantId = registrantId;
    }

    @JsonProperty("customersId")
    public Integer getCustomersId() {
        return customersId;
    }

    @JsonProperty("customersId")
    public void setCustomersId(Integer customersId) {
        this.customersId = customersId;
    }

    @JsonProperty("registeredAppUserId")
    public String getRegisteredAppUserId() {
        return registeredAppUserId;
    }

    @JsonProperty("registeredAppUserId")
    public void setRegisteredAppUserId(String registeredAppUserId) {
        this.registeredAppUserId = registeredAppUserId;
    }

    @JsonProperty("optinNewProductInfo")
    public Integer getOptinNewProductInfo() {
        return optinNewProductInfo;
    }

    @JsonProperty("optinNewProductInfo")
    public void setOptinNewProductInfo(Integer optinNewProductInfo) {
        this.optinNewProductInfo = optinNewProductInfo;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("acceptTermsAndConditions")
    public Integer getAcceptTermsAndConditions() {
        return acceptTermsAndConditions;
    }

    @JsonProperty("acceptTermsAndConditions")
    public void setAcceptTermsAndConditions(Integer acceptTermsAndConditions) {
        this.acceptTermsAndConditions = acceptTermsAndConditions;
    }

    @JsonProperty("appUsers")
    public List<AppUser> getAppUsers() {
        return appUsers;
    }

    @JsonProperty("appUsers")
    public void setAppUsers(List<AppUser> appUsers) {
        this.appUsers = appUsers;
    }

    @JsonProperty("productActivations")
    public List<ProductActivation> getProductActivations() {
        return productActivations;
    }

    @JsonProperty("productActivations")
    public void setProductActivations(List<ProductActivation> productActivations) {
        this.productActivations = productActivations;
    }

}
