package com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.MediaSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "productsId",
        "productsAudioUrl",
        "productsImage",
        "productsName",
        "productsLanguageName",
        "productsLevel",
        "productsMedia",
        "productsTotalLessons",
        "productsLessonRange",
        "productsNextLevel",
        "productsHighestLevelAvail",
        "productsIsEsl",
        "productsPackageName",
        "productsNumMedia",
        "productsInclReadings",
        "productsMediaSize",
        "productsTitle",
        "productsGuideType",
        "mediaSet"
})
public class PcmFreeCourseResultData {

    @JsonProperty("productsId")
    private long productsId;
    @JsonProperty("productsAudioUrl")
    private String productsAudioUrl;
    @JsonProperty("productsImage")
    private String productsImage;
    @JsonProperty("productsName")
    private String productsName;
    @JsonProperty("productsLanguageName")
    private String productsLanguageName;
    @JsonProperty("productsLevel")
    private long productsLevel;
    @JsonProperty("productsMedia")
    private String productsMedia;
    @JsonProperty("productsTotalLessons")
    private long productsTotalLessons;
    @JsonProperty("productsLessonRange")
    private String productsLessonRange;
    @JsonProperty("productsNextLevel")
    private long productsNextLevel;
    @JsonProperty("productsHighestLevelAvail")
    private long productsHighestLevelAvail;
    @JsonProperty("productsIsEsl")
    private long productsIsEsl;
    @JsonProperty("productsPackageName")
    private String productsPackageName;
    @JsonProperty("productsNumMedia")
    private long productsNumMedia;
    @JsonProperty("productsInclReadings")
    private long productsInclReadings;
    @JsonProperty("productsMediaSize")
    private String productsMediaSize;
    @JsonProperty("productsTitle")
    private String productsTitle;
    @JsonProperty("productsGuideType")
    private String productsGuideType;
    @JsonProperty("mediaSet")
    private MediaSet mediaSet;

    @JsonProperty("productsId")
    public long getProductsId() {
        return productsId;
    }

    @JsonProperty("productsId")
    public void setProductsId(long productsId) {
        this.productsId = productsId;
    }

    @JsonProperty("productsAudioUrl")
    public String getProductsAudioUrl() {
        return productsAudioUrl;
    }

    @JsonProperty("productsAudioUrl")
    public void setProductsAudioUrl(String productsAudioUrl) {
        this.productsAudioUrl = productsAudioUrl;
    }

    @JsonProperty("productsImage")
    public String getProductsImage() {
        return productsImage;
    }

    @JsonProperty("productsImage")
    public void setProductsImage(String productsImage) {
        this.productsImage = productsImage;
    }

    @JsonProperty("productsName")
    public String getProductsName() {
        return productsName;
    }

    @JsonProperty("productsName")
    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    @JsonProperty("productsLanguageName")
    public String getProductsLanguageName() {
        return productsLanguageName;
    }

    @JsonProperty("productsLanguageName")
    public void setProductsLanguageName(String productsLanguageName) {
        this.productsLanguageName = productsLanguageName;
    }

    @JsonProperty("productsLevel")
    public long getProductsLevel() {
        return productsLevel;
    }

    @JsonProperty("productsLevel")
    public void setProductsLevel(long productsLevel) {
        this.productsLevel = productsLevel;
    }

    @JsonProperty("productsMedia")
    public String getProductsMedia() {
        return productsMedia;
    }

    @JsonProperty("productsMedia")
    public void setProductsMedia(String productsMedia) {
        this.productsMedia = productsMedia;
    }

    @JsonProperty("productsTotalLessons")
    public long getProductsTotalLessons() {
        return productsTotalLessons;
    }

    @JsonProperty("productsTotalLessons")
    public void setProductsTotalLessons(long productsTotalLessons) {
        this.productsTotalLessons = productsTotalLessons;
    }

    @JsonProperty("productsLessonRange")
    public String getProductsLessonRange() {
        return productsLessonRange;
    }

    @JsonProperty("productsLessonRange")
    public void setProductsLessonRange(String productsLessonRange) {
        this.productsLessonRange = productsLessonRange;
    }

    @JsonProperty("productsNextLevel")
    public long getProductsNextLevel() {
        return productsNextLevel;
    }

    @JsonProperty("productsNextLevel")
    public void setProductsNextLevel(long productsNextLevel) {
        this.productsNextLevel = productsNextLevel;
    }

    @JsonProperty("productsHighestLevelAvail")
    public long getProductsHighestLevelAvail() {
        return productsHighestLevelAvail;
    }

    @JsonProperty("productsHighestLevelAvail")
    public void setProductsHighestLevelAvail(long productsHighestLevelAvail) {
        this.productsHighestLevelAvail = productsHighestLevelAvail;
    }

    @JsonProperty("productsIsEsl")
    public long getProductsIsEsl() {
        return productsIsEsl;
    }

    @JsonProperty("productsIsEsl")
    public void setProductsIsEsl(long productsIsEsl) {
        this.productsIsEsl = productsIsEsl;
    }

    @JsonProperty("productsPackageName")
    public String getProductsPackageName() {
        return productsPackageName;
    }

    @JsonProperty("productsPackageName")
    public void setProductsPackageName(String productsPackageName) {
        this.productsPackageName = productsPackageName;
    }

    @JsonProperty("productsNumMedia")
    public long getProductsNumMedia() {
        return productsNumMedia;
    }

    @JsonProperty("productsNumMedia")
    public void setProductsNumMedia(long productsNumMedia) {
        this.productsNumMedia = productsNumMedia;
    }

    @JsonProperty("productsInclReadings")
    public long getProductsInclReadings() {
        return productsInclReadings;
    }

    @JsonProperty("productsInclReadings")
    public void setProductsInclReadings(long productsInclReadings) {
        this.productsInclReadings = productsInclReadings;
    }

    @JsonProperty("productsMediaSize")
    public String getProductsMediaSize() {
        return productsMediaSize;
    }

    @JsonProperty("productsMediaSize")
    public void setProductsMediaSize(String productsMediaSize) {
        this.productsMediaSize = productsMediaSize;
    }

    @JsonProperty("productsTitle")
    public String getProductsTitle() {
        return productsTitle;
    }

    @JsonProperty("productsTitle")
    public void setProductsTitle(String productsTitle) {
        this.productsTitle = productsTitle;
    }

    @JsonProperty("productsGuideType")
    public String getProductsGuideType() {
        return productsGuideType;
    }

    @JsonProperty("productsGuideType")
    public void setProductsGuideType(String productsGuideType) {
        this.productsGuideType = productsGuideType;
    }

    @JsonProperty("mediaSet")
    public MediaSet getMediaSet() {
        return mediaSet;
    }

    @JsonProperty("mediaSet")
    public void setMediaSet(MediaSet mediaSet) {
        this.mediaSet = mediaSet;
    }

}
