
package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "appUserId",
        "customersId",
        "trkHash",
        "appUserName",
        "isRegisteredAppUser",
        "dateCreated"
})
public class AppUser {

    @JsonProperty("appUserId")
    private String appUserId;
    @JsonProperty("customersId")
    private Integer customersId;
    @JsonProperty("trkHash")
    private String trkHash;
    @JsonProperty("appUserName")
    private String appUserName;
    @JsonProperty("isRegisteredAppUser")
    private Integer isRegisteredAppUser;

    @JsonProperty("appUserId")
    public String getAppUserId() {
        return appUserId;
    }

    @JsonProperty("appUserId")
    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    @JsonProperty("customersId")
    public Integer getCustomersId() {
        return customersId;
    }

    @JsonProperty("customersId")
    public void setCustomersId(Integer customersId) {
        this.customersId = customersId;
    }

    @JsonProperty("trkHash")
    public String getTrkHash() {
        return trkHash;
    }

    @JsonProperty("trkHash")
    public void setTrkHash(String trkHash) {
        this.trkHash = trkHash;
    }

    @JsonProperty("appUserName")
    public String getAppUserName() {
        return appUserName;
    }

    @JsonProperty("appUserName")
    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    @JsonProperty("isRegisteredAppUser")
    public Integer getIsRegisteredAppUser() {
        return isRegisteredAppUser;
    }

    @JsonProperty("isRegisteredAppUser")
    public void setIsRegisteredAppUser(Integer isRegisteredAppUser) {
        this.isRegisteredAppUser = isRegisteredAppUser;
    }

    public boolean isRootSubUser() {
        return Objects.equals(this.getIsRegisteredAppUser(), 1);
    }

    public String getSubUserId() {
        return getAppUserId().split("_")[1];
    }
}
