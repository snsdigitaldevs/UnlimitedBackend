package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class FlashCard {
    private String transliteration;
    private String translation;
    private String language;
    private String mp3FileName;

    public FlashCard(String transliteration, String translation, String language, String mp3FileName) {
        this.transliteration = transliteration;
        this.translation = translation;
        this.language = language;
        this.mp3FileName = mp3FileName;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public String getTranslation() {
        return translation;
    }

    public String getLanguage() {
        return language;
    }

    public String getMp3FileName() {
        return mp3FileName;
    }
}
