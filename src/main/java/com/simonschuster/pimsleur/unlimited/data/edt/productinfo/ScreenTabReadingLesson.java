package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScreenTabReadingLesson {
    @JsonProperty("title")
    private String title;

    @JsonProperty("audioQueKeyIntroFileName")
    private String audioQueKeyIntroFileName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudioQueKeyIntroFileName() {
        return audioQueKeyIntroFileName;
    }

    public void setAudioQueKeyIntroFileName(String audioQueKeyIntroFileName) {
        this.audioQueKeyIntroFileName = audioQueKeyIntroFileName;
    }
}
