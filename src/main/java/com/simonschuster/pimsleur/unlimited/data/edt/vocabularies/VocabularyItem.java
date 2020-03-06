package com.simonschuster.pimsleur.unlimited.data.edt.vocabularies;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VocabularyItem {
    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("subUserId")
    private String subUserId;

    @JsonProperty("productCode")
    @JsonAlias({"isbn"})
    private String productCode;

    @JsonProperty("language")
    private String language;

    @JsonProperty("transliteration")
    private String transliteration;

    @JsonProperty("translation")
    private String translation;

    @JsonProperty("mp3FileName")
    private String mp3FileName;

    @JsonProperty("lessonNumber")
    private Integer lessonNumber;

    @JsonProperty("packGroupNumber")
    private Integer packGroupNumber;

    @JsonProperty("savedTime")
    private long savedTime;

    @JsonProperty("customerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @JsonProperty("subUserId")
    public String getSubUserId() {
        return subUserId;
    }
    @JsonProperty("subUserId")
    public void setSubUserId(String subUserId) {
        this.subUserId = subUserId;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }
    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }
    @JsonProperty("transliteration")
    public String getTransliteration() {
        return transliteration;
    }
    @JsonProperty("transliteration")
    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }
    @JsonProperty("translation")
    public String getTranslation() {
        return translation;
    }
    @JsonProperty("translation")
    public void setTranslation(String translation) {
        this.translation = translation;
    }
    @JsonProperty("mp3FileName")
    public String getMp3FileName() {
        return mp3FileName;
    }
    @JsonProperty("mp3FileName")
    public void setMp3FileName(String mp3FileName) {
        this.mp3FileName = mp3FileName;
    }
    @JsonProperty("lessonNumber")
    public Integer getLessonNumber() {
        return lessonNumber;
    }
    @JsonProperty("lessonNumber")
    public void setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
    }
    @JsonProperty("packGroupNumber")
    public Integer getPackGroupNumber() {
        return packGroupNumber;
    }
    @JsonProperty("packGroupNumber")
    public void setPackGroupNumber(Integer packGroupNumber) {
        this.packGroupNumber = packGroupNumber;
    }
    @JsonProperty("savedTime")
    public long getSavedTime() {
        return savedTime;
    }
    @JsonProperty("savedTime")
    public void setSavedTime(long savedTime) {
        this.savedTime = savedTime;
    }
}
