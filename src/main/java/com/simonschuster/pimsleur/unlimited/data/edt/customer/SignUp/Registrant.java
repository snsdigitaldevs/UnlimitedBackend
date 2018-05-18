
package com.simonschuster.pimsleur.unlimited.data.edt.customer.SignUp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "registrantId",
    "customersId",
    "registeredAppUserId",
    "optinNewProductInfo",
    "firstName",
    "acceptTermsAndConditions"
})
public class Registrant {

    @JsonProperty("registrantId")
    private String registrantId;
    @JsonProperty("customersId")
    private String customersId;
    @JsonProperty("registeredAppUserId")
    private String registeredAppUserId;
    @JsonProperty("optinNewProductInfo")
    private Boolean optinNewProductInfo;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("acceptTermsAndConditions")
    private Boolean acceptTermsAndConditions;

    @JsonProperty("registrantId")
    public String getRegistrantId() {
        return registrantId;
    }

    @JsonProperty("registrantId")
    public void setRegistrantId(String registrantId) {
        this.registrantId = registrantId;
    }

    @JsonProperty("customersId")
    public String getCustomersId() {
        return customersId;
    }

    @JsonProperty("customersId")
    public void setCustomersId(String customersId) {
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
    public Boolean getOptinNewProductInfo() {
        return optinNewProductInfo;
    }

    @JsonProperty("optinNewProductInfo")
    public void setOptinNewProductInfo(Boolean optinNewProductInfo) {
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
    public Boolean getAcceptTermsAndConditions() {
        return acceptTermsAndConditions;
    }

    @JsonProperty("acceptTermsAndConditions")
    public void setAcceptTermsAndConditions(Boolean acceptTermsAndConditions) {
        this.acceptTermsAndConditions = acceptTermsAndConditions;
    }

}
