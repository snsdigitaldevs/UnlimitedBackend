package com.simonschuster.pimsleur.unlimited.data.dto.vocabularies;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VocabularyInfoBodyDTO {

    @NotNull
    private String customerId;
    @NotNull
    private String subUserId;
    @NotNull
    private String productCode;
    @NotBlank
    private String language;
    private String transliteration;
    private String translation;
    private String mp3FileName;
    private Integer lessonNumber;
    private Integer packGroupNumber;

    public VocabularyInfoBodyDTO() {
    }

    public VocabularyInfoBodyDTO(String customerId, String subUserId, String productCode, String language, String transliteration, String translation, String mp3FileName, Integer lessonNumber, Integer packGroupNumber) {
        this.customerId = customerId;
        this.subUserId = subUserId;
        this.productCode = productCode;
        this.language = language;
        this.transliteration = transliteration;
        this.translation = translation;
        this.mp3FileName = mp3FileName;
        this.lessonNumber = lessonNumber;
        this.packGroupNumber = packGroupNumber;
    }

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
}
