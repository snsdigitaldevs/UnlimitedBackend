package com.simonschuster.pimsleur.unlimited.data.dto.practices;

public class QuickMatchItem {
    String cue;
    String transliteration;
    String mp3FileName;

    public QuickMatchItem(String cue, String transliteration, String mp3FileName) {
        this.cue = cue;
        this.transliteration = transliteration;
        this.mp3FileName = mp3FileName;
    }
}
