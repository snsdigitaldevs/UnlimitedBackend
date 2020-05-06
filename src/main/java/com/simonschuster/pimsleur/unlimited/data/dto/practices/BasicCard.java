package com.simonschuster.pimsleur.unlimited.data.dto.practices;

public class BasicCard {

    private String transliteration;
    private String translation;
    private String language;
    private String mp3FileName;

    public BasicCard(String transliteration, String translation, String language,
        String mp3FileName) {
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

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setMp3FileName(String mp3FileName) {
        this.mp3FileName = mp3FileName;
    }
}
