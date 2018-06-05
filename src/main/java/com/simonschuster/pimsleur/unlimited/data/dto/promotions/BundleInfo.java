package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Bundle ISBN",
        "Bundle Title",
        "30-Lesson ISBN",
        "30-Lesson Title"
})
public class BundleInfo {

    @JsonProperty("Bundle ISBN")
    private String bundleISBN;
    @JsonProperty("Bundle Title")
    private String bundleTitle;
    @JsonProperty("30-Lesson ISBN")
    private String _30LessonISBN;
    @JsonProperty("30-Lesson Title")
    private String _30LessonTitle;

    @JsonProperty("Bundle ISBN")
    public String getBundleISBN() {
        return bundleISBN;
    }

    @JsonProperty("Bundle ISBN")
    public void setBundleISBN(String bundleISBN) {
        this.bundleISBN = bundleISBN;
    }

    @JsonProperty("Bundle Title")
    public String getBundleTitle() {
        return bundleTitle;
    }

    @JsonProperty("Bundle Title")
    public void setBundleTitle(String bundleTitle) {
        this.bundleTitle = bundleTitle;
    }

    @JsonProperty("30-Lesson ISBN")
    public String get30LessonISBN() {
        return _30LessonISBN;
    }

    @JsonProperty("30-Lesson ISBN")
    public void set30LessonISBN(String _30LessonISBN) {
        this._30LessonISBN = _30LessonISBN;
    }

    @JsonProperty("30-Lesson Title")
    public String get30LessonTitle() {
        return _30LessonTitle;
    }

    @JsonProperty("30-Lesson Title")
    public void set30LessonTitle(String _30LessonTitle) {
        this._30LessonTitle = _30LessonTitle;
    }

    public boolean isBundle() {
        return !this.bundleISBN.equals("");
    }

}