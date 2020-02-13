package com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BonusCard {
    private String transliteration;
    private String translation;
    private String language;
    private String mp3FileName;

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMp3FileName() {
        return mp3FileName;
    }

    public void setMp3FileName(String mp3FileName) {
        this.mp3FileName = mp3FileName;
    }
}
