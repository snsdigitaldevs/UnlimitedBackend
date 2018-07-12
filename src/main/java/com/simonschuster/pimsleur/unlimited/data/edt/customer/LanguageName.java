package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LanguageName {
    private String languageNameId;
    private String languageName;
    private String displayName;
    private String smallFlagIcon;
    private String langInfo;
    private String sortOrder;
    private String countryCodes;
    private String isEnabled;
    private String name;
    private String entitlementName;
    private String productKey;
    private LanguageImageMetadata languageImageMetadata;

    public String getLanguageNameId() {
        return languageNameId;
    }

    public void setLanguageNameId(String languageNameId) {
        this.languageNameId = languageNameId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSmallFlagIcon() {
        return smallFlagIcon;
    }

    public void setSmallFlagIcon(String smallFlagIcon) {
        this.smallFlagIcon = smallFlagIcon;
    }

    public String getLangInfo() {
        return langInfo;
    }

    public void setLangInfo(String langInfo) {
        this.langInfo = langInfo;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCountryCodes() {
        return countryCodes;
    }

    public void setCountryCodes(String countryCodes) {
        this.countryCodes = countryCodes;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntitlementName() {
        return entitlementName;
    }

    public void setEntitlementName(String entitlementName) {
        this.entitlementName = entitlementName;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public LanguageImageMetadata getLanguageImageMetadata() {
        return languageImageMetadata;
    }

    public void setLanguageImageMetadata(LanguageImageMetadata languageImageMetadata) {
        this.languageImageMetadata = languageImageMetadata;
    }
}
