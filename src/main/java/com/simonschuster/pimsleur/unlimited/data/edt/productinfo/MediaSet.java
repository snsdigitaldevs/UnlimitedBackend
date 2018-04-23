package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MediaSet {
    @JsonProperty("mediaSetDescription")
    private String mediaSetDescription;
    @JsonProperty("courseLanguageNameAbbr")
    private String courseLanguageNameAbbr;
    @JsonProperty("mediaSetTitle")
    private String mediaSetTitle;
    @JsonProperty("isEnabled")
    private Integer isEnabled;
    @JsonProperty("isbn13")
    private String isbn13; //same as isbn, previous isbn is the key of this mediaset info.
    @JsonProperty("productsId")
    private Integer productsId;
    @JsonProperty("courseLanguageName")
    private String courseLanguageName;
    @JsonProperty("courseLevel")
    private Integer courseLevel;
    @JsonProperty("mediaSetId")
    private Integer mediaSetId;
    @JsonProperty("isVisible")
    private Integer isVisible;
    @JsonProperty("mediaItems")
    private List<MediaItem> mediaItems;

    public String getMediaSetDescription() {
        return mediaSetDescription;
    }

    public void setMediaSetDescription(String mediaSetDescription) {
        this.mediaSetDescription = mediaSetDescription;
    }

    public String getCourseLanguageNameAbbr() {
        return courseLanguageNameAbbr;
    }

    public void setCourseLanguageNameAbbr(String courseLanguageNameAbbr) {
        this.courseLanguageNameAbbr = courseLanguageNameAbbr;
    }

    public String getMediaSetTitle() {
        return mediaSetTitle;
    }

    public void setMediaSetTitle(String mediaSetTitle) {
        this.mediaSetTitle = mediaSetTitle;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public Integer getProductsId() {
        return productsId;
    }

    public void setProductsId(Integer productsId) {
        this.productsId = productsId;
    }

    public String getCourseLanguageName() {
        return courseLanguageName;
    }

    public void setCourseLanguageName(String courseLanguageName) {
        this.courseLanguageName = courseLanguageName;
    }

    public Integer getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(Integer courseLevel) {
        this.courseLevel = courseLevel;
    }

    public Integer getMediaSetId() {
        return mediaSetId;
    }

    public void setMediaSetId(Integer mediaSetId) {
        this.mediaSetId = mediaSetId;
    }

    public Integer getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

    public List<MediaItem> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(List<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }
}
