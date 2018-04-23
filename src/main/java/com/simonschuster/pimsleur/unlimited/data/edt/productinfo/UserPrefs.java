package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPrefs {
    @JsonProperty("PREF_READING_INTRO_PLAYED")
    private PrefReadingIntroPlayed prefReadingIntroPlayed;

    public PrefReadingIntroPlayed getPrefReadingIntroPlayed() {
        return prefReadingIntroPlayed;
    }

    public void setPrefReadingIntroPlayed(PrefReadingIntroPlayed prefReadingIntroPlayed) {
        this.prefReadingIntroPlayed = prefReadingIntroPlayed;
    }
}
