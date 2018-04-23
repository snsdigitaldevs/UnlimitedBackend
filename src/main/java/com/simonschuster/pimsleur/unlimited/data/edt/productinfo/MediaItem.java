package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaItem {
    //This is for practices info, like flash card info
    @JsonProperty("mediaItemLicenseFileName")
    @JsonIgnoreProperties
    private String mediaItemLicenseFileName;

    @JsonProperty("remoteUrl")
    @JsonIgnoreProperties
    private String remoteUrl;

    @JsonIgnoreProperties
    @JsonProperty("idMetadata")
    private Integer idMetadata;

    @JsonProperty("remoteAccessRestriction")
    @JsonIgnoreProperties
    private String remoteAccessRestriction;

    @JsonProperty("sortOrder")
    @JsonIgnoreProperties
    private Integer sortOrder;

    @JsonProperty("imageURL")
    @JsonIgnoreProperties
    private String imageURL;
    //todo: support different type, like String

    @JsonProperty("isActive")
    @JsonIgnoreProperties
    private Integer isActive; //This field for image media is String

    @JsonProperty("lengthSeconds")
    @JsonIgnoreProperties
    private Integer lengthSeconds;

    @JsonProperty("filename")
    private String filename;

    @JsonProperty("licenseFileName")
    @JsonIgnoreProperties
    private String licenseFileName;

    @JsonProperty("fileSizeBytes")
    @JsonIgnoreProperties
    private String fileSizeBytes;  //This field for image media is Integer

    @JsonProperty("isVisible")
    @JsonIgnoreProperties
    private Integer isVisible;  //This field for image media is Integer

    @JsonProperty("mediaSetId")
    private Integer mediaSetId;

    @JsonProperty("isEncrypted")
    private Integer isEncrypted;  //This field for image media is String

    @JsonProperty("description")
    @JsonIgnoreProperties
    private String description;

    @JsonProperty("refId")
    @JsonIgnoreProperties
    private String refId;  //This field for image media is Integer

    @JsonProperty("mediaItemId")
    private Integer mediaItemId;

    @JsonProperty("typeId")
    @JsonIgnoreProperties
    private String typeId;

    @JsonProperty("formatId")
    @JsonIgnoreProperties
    private String formatId;

    @JsonProperty("classId")
    private Integer classId;

    @JsonProperty("title")
    private String title;

    //The following is special fields for image media information
    @JsonProperty("imageDescription")
    @JsonIgnoreProperties
    private String imageDescription;

    @JsonProperty("imageLocation")
    @JsonIgnoreProperties
    private String imageLocation;

    @JsonProperty("imageCredits")
    @JsonIgnoreProperties
    private String imageCredits;

    @JsonProperty("unit")
    @JsonIgnoreProperties
    private String unit;

    public String getMediaItemLicenseFileName() {
        return mediaItemLicenseFileName;
    }

    public void setMediaItemLicenseFileName(String mediaItemLicenseFileName) {
        this.mediaItemLicenseFileName = mediaItemLicenseFileName;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public Integer getIdMetadata() {
        return idMetadata;
    }

    public void setIdMetadata(Integer idMetadata) {
        this.idMetadata = idMetadata;
    }

    public String getRemoteAccessRestriction() {
        return remoteAccessRestriction;
    }

    public void setRemoteAccessRestriction(String remoteAccessRestriction) {
        this.remoteAccessRestriction = remoteAccessRestriction;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getLengthSeconds() {
        return lengthSeconds;
    }

    public void setLengthSeconds(Integer lengthSeconds) {
        this.lengthSeconds = lengthSeconds;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLicenseFileName() {
        return licenseFileName;
    }

    public void setLicenseFileName(String licenseFileName) {
        this.licenseFileName = licenseFileName;
    }

    public String getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(String fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public Integer getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

    public Integer getMediaSetId() {
        return mediaSetId;
    }

    public void setMediaSetId(Integer mediaSetId) {
        this.mediaSetId = mediaSetId;
    }

    public Integer getIsEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(Integer isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public Integer getMediaItemId() {
        return mediaItemId;
    }

    public void setMediaItemId(Integer mediaItemId) {
        this.mediaItemId = mediaItemId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getFormatId() {
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getImageCredits() {
        return imageCredits;
    }

    public void setImageCredits(String imageCredits) {
        this.imageCredits = imageCredits;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
