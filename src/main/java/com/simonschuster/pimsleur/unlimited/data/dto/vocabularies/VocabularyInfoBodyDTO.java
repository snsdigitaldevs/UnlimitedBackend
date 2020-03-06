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


    public String getCustomerId() {
        return customerId;
    }

    public VocabularyInfoBodyDTO setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getSubUserId() {
        return subUserId;
    }

    public VocabularyInfoBodyDTO setSubUserId(String subUserId) {
        this.subUserId = subUserId;
        return this;
    }

    public String getProductCode() {
        return productCode;
    }

    public VocabularyInfoBodyDTO setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public VocabularyInfoBodyDTO setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public VocabularyInfoBodyDTO setTransliteration(String transliteration) {
        this.transliteration = transliteration;
        return this;
    }

    public String getTranslation() {
        return translation;
    }

    public VocabularyInfoBodyDTO setTranslation(String translation) {
        this.translation = translation;
        return this;
    }

    public String getMp3FileName() {
        return mp3FileName;
    }

    public VocabularyInfoBodyDTO setMp3FileName(String mp3FileName) {
        this.mp3FileName = mp3FileName;
        return this;
    }

    public Integer getLessonNumber() {
        return lessonNumber;
    }

    public VocabularyInfoBodyDTO setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
        return this;
    }

    public Integer getPackGroupNumber() {
        return packGroupNumber;
    }

    public VocabularyInfoBodyDTO setPackGroupNumber(Integer packGroupNumber) {
        this.packGroupNumber = packGroupNumber;
        return this;
    }
}