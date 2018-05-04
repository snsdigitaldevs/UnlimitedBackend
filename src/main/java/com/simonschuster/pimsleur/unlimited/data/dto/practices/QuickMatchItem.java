package com.simonschuster.pimsleur.unlimited.data.dto.practices;

public class QuickMatchItem {
    private String cue;
    private String transliteration;
    private String mp3FileName;

    public QuickMatchItem(String cue, String transliteration, String mp3FileName) {
        this.cue = cue;
        this.transliteration = transliteration;
        this.mp3FileName = mp3FileName;
    }

    public String getCue() {
        return cue;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public String getMp3FileName() {
        return mp3FileName;
    }
}
