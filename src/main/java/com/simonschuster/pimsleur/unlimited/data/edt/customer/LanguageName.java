package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "languageNameId",
        "languageName",
        "displayName",
        "smallFlagIcon",
        "isEsl",
        "langInfo",
        "sortOrder",
        "countryCodes",
        "isEnabled",
        "name",
        "entitlementName",
        "productKey",
        "languageImageMetadata"
})
public class LanguageName {

    @JsonProperty("languageNameId")
    private Long languageNameId;
    @JsonProperty("languageName")
    private String languageName;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("smallFlagIcon")
    private String smallFlagIcon;
    @JsonProperty("isEsl")
    private Long isEsl;
    @JsonProperty("langInfo")
    private String langInfo;
    @JsonProperty("sortOrder")
    private Long sortOrder;
    @JsonProperty("countryCodes")
    private String countryCodes;
    @JsonProperty("isEnabled")
    private Long isEnabled;
    @JsonProperty("name")
    private String name;
    @JsonProperty("entitlementName")
    private String entitlementName;
    @JsonProperty("productKey")
    private String productKey;
    @JsonProperty("languageImageMetadata")
    private String languageImageMetadata;

    @JsonProperty("languageNameId")
    public Long getLanguageNameId() {
        return languageNameId;
    }

    @JsonProperty("languageNameId")
    public void setLanguageNameId(Long languageNameId) {
        this.languageNameId = languageNameId;
    }

    @JsonProperty("languageName")
    public String getLanguageName() {
        return languageName;
    }

    @JsonProperty("languageName")
    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("smallFlagIcon")
    public String getSmallFlagIcon() {
        return smallFlagIcon;
    }

    @JsonProperty("smallFlagIcon")
    public void setSmallFlagIcon(String smallFlagIcon) {
        this.smallFlagIcon = smallFlagIcon;
    }

    @JsonProperty("isEsl")
    public Long getIsEsl() {
        return isEsl;
    }

    @JsonProperty("isEsl")
    public void setIsEsl(Long isEsl) {
        this.isEsl = isEsl;
    }

    @JsonProperty("langInfo")
    public String getLangInfo() {
        return langInfo;
    }

    @JsonProperty("langInfo")
    public void setLangInfo(String langInfo) {
        this.langInfo = langInfo;
    }

    @JsonProperty("sortOrder")
    public Long getSortOrder() {
        return sortOrder;
    }

    @JsonProperty("sortOrder")
    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }

    @JsonProperty("countryCodes")
    public String getCountryCodes() {
        return countryCodes;
    }

    @JsonProperty("countryCodes")
    public void setCountryCodes(String countryCodes) {
        this.countryCodes = countryCodes;
    }

    @JsonProperty("isEnabled")
    public Long getIsEnabled() {
        return isEnabled;
    }

    @JsonProperty("isEnabled")
    public void setIsEnabled(Long isEnabled) {
        this.isEnabled = isEnabled;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("entitlementName")
    public String getEntitlementName() {
        return entitlementName;
    }

    @JsonProperty("entitlementName")
    public void setEntitlementName(String entitlementName) {
        this.entitlementName = entitlementName;
    }

    @JsonProperty("productKey")
    public String getProductKey() {
        return productKey;
    }

    @JsonProperty("productKey")
    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @JsonProperty("languageImageMetadata")
    public String getLanguageImageMetadata() {
        return languageImageMetadata;
    }

    @JsonProperty("languageImageMetadata")
    public void setLanguageImageMetadata(String languageImageMetadata) {
        this.languageImageMetadata = languageImageMetadata;
    }

}