package com.simonschuster.pimsleur.unlimited.data.dto.vocabularies;

import javax.validation.constraints.NotEmpty;

public class VocabularyItemDTO {

    @NotEmpty
    private String language;
    private String transliteration;
    private String translation;
    private String mp3FileName;
    private Integer lessonNumber;
    private Integer packGroupNumber;

    public String getLanguage() {
        return language;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public String getTranslation() {
        return translation;
    }

    public String getMp3FileName() {
        return mp3FileName;
    }

    public Integer getLessonNumber() {
        return lessonNumber;
    }

    public Integer getPackGroupNumber() {
        return packGroupNumber;
    }

    public VocabularyItemDTO setLanguage(String language) {
        this.language = language;
        return this;
    }

    public VocabularyItemDTO setTransliteration(String transliteration) {
        this.transliteration = transliteration;
        return this;
    }

    public VocabularyItemDTO setTranslation(String translation) {
        this.translation = translation;
        return this;
    }

    public VocabularyItemDTO setMp3FileName(String mp3FileName) {
        this.mp3FileName = mp3FileName;
        return this;
    }

    public VocabularyItemDTO setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
        return this;
    }

    public VocabularyItemDTO setPackGroupNumber(Integer packGroupNumber) {
        this.packGroupNumber = packGroupNumber;
        return this;
    }
}
