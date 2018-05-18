
package com.simonschuster.pimsleur.unlimited.data.edt.customer.SignUp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "customersId",
    "customersFirstname",
    "customersEmailAddress",
    "storeDomain",
    "customersAuth0UserId",
    "isProvisional"
})
public class Customer {

    @JsonProperty("customersId")
    private String customersId;
    @JsonProperty("customersFirstname")
    private String customersFirstname;
    @JsonProperty("customersEmailAddress")
    private String customersEmailAddress;
    @JsonProperty("storeDomain")
    private String storeDomain;
    @JsonProperty("customersAuth0UserId")
    private String customersAuth0UserId;
    @JsonProperty("isProvisional")
    private Integer isProvisional;

    @JsonProperty("customersId")
    public String getCustomersId() {
        return customersId;
    }

    @JsonProperty("customersId")
    public void setCustomersId(String customersId) {
        this.customersId = customersId;
    }

    @JsonProperty("customersFirstname")
    public String getCustomersFirstname() {
        return customersFirstname;
    }

    @JsonProperty("customersFirstname")
    public void setCustomersFirstname(String customersFirstname) {
        this.customersFirstname = customersFirstname;
    }

    @JsonProperty("customersEmailAddress")
    public String getCustomersEmailAddress() {
        return customersEmailAddress;
    }

    @JsonProperty("customersEmailAddress")
    public void setCustomersEmailAddress(String customersEmailAddress) {
        this.customersEmailAddress = customersEmailAddress;
    }

    @JsonProperty("storeDomain")
    public String getStoreDomain() {
        return storeDomain;
    }

    @JsonProperty("storeDomain")
    public void setStoreDomain(String storeDomain) {
        this.storeDomain = storeDomain;
    }

    @JsonProperty("customersAuth0UserId")
    public String getCustomersAuth0UserId() {
        return customersAuth0UserId;
    }

    @JsonProperty("customersAuth0UserId")
    public void setCustomersAuth0UserId(String customersAuth0UserId) {
        this.customersAuth0UserId = customersAuth0UserId;
    }

    @JsonProperty("isProvisional")
    public Integer getIsProvisional() {
        return isProvisional;
    }

    @JsonProperty("isProvisional")
    public void setIsProvisional(Integer isProvisional) {
        this.isProvisional = isProvisional;
    }

}
