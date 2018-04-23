package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScreenInfo {
    @JsonProperty("SCREEN_TAB_READING_LESSON")
    private ScreenTabReadingLesson screenTabReadingLesson;

    public ScreenTabReadingLesson getScreenTabReadingLesson() {
        return screenTabReadingLesson;
    }

    public void setScreenTabReadingLesson(ScreenTabReadingLesson screenTabReadingLesson) {
        this.screenTabReadingLesson = screenTabReadingLesson;
    }
}
