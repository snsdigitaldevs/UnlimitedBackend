
package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "productCode",
        "isRegisterable",
        "isVirtualProduct",
        "isDemoProduct",
        "isUpgradable",
        "productDescription",
        "audioEncryptionKey",
        "jsonEncryptionKey",
        "totalActivationsAllowed",
        "desktopActivationsAllowed",
        "mobileActivationsAllowed",
        "sortOrder",
        "audioCourseIsbnMapping",
        "productLanguageName",
        "osxInstallerFile",
        "osxDownloaderFile",
        "winInstallerFile",
        "winDownloaderFile",
        "currentAppVersion"
})
public class RegisterableProduct {

    @JsonProperty("productCode")
    private String productCode;
    @JsonProperty("isRegisterable")
    private Integer isRegisterable;
    @JsonProperty("isVirtualProduct")
    private Integer isVirtualProduct;
    @JsonProperty("isDemoProduct")
    private Integer isDemoProduct;
    @JsonProperty("isUpgradable")
    private Integer isUpgradable;
    @JsonProperty("productDescription")
    private String productDescription;
    @JsonProperty("audioEncryptionKey")
    private String audioEncryptionKey;
    @JsonProperty("jsonEncryptionKey")
    private String jsonEncryptionKey;
    @JsonProperty("totalActivationsAllowed")
    private Integer totalActivationsAllowed;
    @JsonProperty("desktopActivationsAllowed")
    private Integer desktopActivationsAllowed;
    @JsonProperty("mobileActivationsAllowed")
    private Integer mobileActivationsAllowed;
    @JsonProperty("sortOrder")
    private Integer sortOrder;
    @JsonProperty("audioCourseIsbnMapping")
    private String audioCourseIsbnMapping;
    @JsonProperty("productLanguageName")
    private String productLanguageName;
    @JsonProperty("osxInstallerFile")
    private String osxInstallerFile;
    @JsonProperty("osxDownloaderFile")
    private String osxDownloaderFile;
    @JsonProperty("winInstallerFile")
    private String winInstallerFile;
    @JsonProperty("winDownloaderFile")
    private String winDownloaderFile;
    @JsonProperty("currentAppVersion")
    private String currentAppVersion;

    @JsonProperty("productCode")
    public String getProductCode() {
        return productCode;
    }

    @JsonProperty("productCode")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @JsonProperty("isRegisterable")
    public Integer getIsRegisterable() {
        return isRegisterable;
    }

    @JsonProperty("isRegisterable")
    public void setIsRegisterable(Integer isRegisterable) {
        this.isRegisterable = isRegisterable;
    }

    @JsonProperty("isVirtualProduct")
    public Integer getIsVirtualProduct() {
        return isVirtualProduct;
    }

    @JsonProperty("isVirtualProduct")
    public void setIsVirtualProduct(Integer isVirtualProduct) {
        this.isVirtualProduct = isVirtualProduct;
    }

    @JsonProperty("isDemoProduct")
    public Integer getIsDemoProduct() {
        return isDemoProduct;
    }

    @JsonProperty("isDemoProduct")
    public void setIsDemoProduct(Integer isDemoProduct) {
        this.isDemoProduct = isDemoProduct;
    }

    @JsonProperty("isUpgradable")
    public Integer getIsUpgradable() {
        return isUpgradable;
    }

    @JsonProperty("isUpgradable")
    public void setIsUpgradable(Integer isUpgradable) {
        this.isUpgradable = isUpgradable;
    }

    @JsonProperty("productDescription")
    public String getProductDescription() {
        return productDescription;
    }

    @JsonProperty("productDescription")
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @JsonProperty("audioEncryptionKey")
    public String getAudioEncryptionKey() {
        return audioEncryptionKey;
    }

    @JsonProperty("audioEncryptionKey")
    public void setAudioEncryptionKey(String audioEncryptionKey) {
        this.audioEncryptionKey = audioEncryptionKey;
    }

    @JsonProperty("jsonEncryptionKey")
    public String getJsonEncryptionKey() {
        return jsonEncryptionKey;
    }

    @JsonProperty("jsonEncryptionKey")
    public void setJsonEncryptionKey(String jsonEncryptionKey) {
        this.jsonEncryptionKey = jsonEncryptionKey;
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

    @JsonProperty("sortOrder")
    public Integer getSortOrder() {
        return sortOrder;
    }

    @JsonProperty("sortOrder")
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @JsonProperty("audioCourseIsbnMapping")
    public String getAudioCourseIsbnMapping() {
        return audioCourseIsbnMapping;
    }

    @JsonProperty("audioCourseIsbnMapping")
    public void setAudioCourseIsbnMapping(String audioCourseIsbnMapping) {
        this.audioCourseIsbnMapping = audioCourseIsbnMapping;
    }

    @JsonProperty("productLanguageName")
    public String getProductLanguageName() {
        return productLanguageName;
    }

    @JsonProperty("productLanguageName")
    public void setProductLanguageName(String productLanguageName) {
        this.productLanguageName = productLanguageName;
    }

    @JsonProperty("osxInstallerFile")
    public String getOsxInstallerFile() {
        return osxInstallerFile;
    }

    @JsonProperty("osxInstallerFile")
    public void setOsxInstallerFile(String osxInstallerFile) {
        this.osxInstallerFile = osxInstallerFile;
    }

    @JsonProperty("osxDownloaderFile")
    public String getOsxDownloaderFile() {
        return osxDownloaderFile;
    }

    @JsonProperty("osxDownloaderFile")
    public void setOsxDownloaderFile(String osxDownloaderFile) {
        this.osxDownloaderFile = osxDownloaderFile;
    }

    @JsonProperty("winInstallerFile")
    public String getWinInstallerFile() {
        return winInstallerFile;
    }

    @JsonProperty("winInstallerFile")
    public void setWinInstallerFile(String winInstallerFile) {
        this.winInstallerFile = winInstallerFile;
    }

    @JsonProperty("winDownloaderFile")
    public String getWinDownloaderFile() {
        return winDownloaderFile;
    }

    @JsonProperty("winDownloaderFile")
    public void setWinDownloaderFile(String winDownloaderFile) {
        this.winDownloaderFile = winDownloaderFile;
    }

    @JsonProperty("currentAppVersion")
    public String getCurrentAppVersion() {
        return currentAppVersion;
    }

    @JsonProperty("currentAppVersion")
    public void setCurrentAppVersion(String currentAppVersion) {
        this.currentAppVersion = currentAppVersion;
    }

}
