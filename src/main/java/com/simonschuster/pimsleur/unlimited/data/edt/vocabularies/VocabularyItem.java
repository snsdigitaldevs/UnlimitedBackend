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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(String subUserId) {
        this.subUserId = subUserId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getMp3FileName() {
        return mp3FileName;
    }

    public void setMp3FileName(String mp3FileName) {
        this.mp3FileName = mp3FileName;
    }

    public Integer getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public Integer getPackGroupNumber() {
        return packGroupNumber;
    }

    public void setPackGroupNumber(Integer packGroupNumber) {
        this.packGroupNumber = packGroupNumber;
    }

    public long getSavedTime() {
        return savedTime;
    }

    public void setSavedTime(long savedTime) {
        this.savedTime = savedTime;
    }
}
