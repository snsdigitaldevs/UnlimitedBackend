package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import com.fasterxml.jackson.annotation.JsonInclude;

// speak easy and reading has same data structure
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SpeakEasyOrReading {
    private long start;
    private long stop;

    private String speaker;
    private String text;
    private String nativeText;
    private String transliteration;

    private int order;

    public SpeakEasyOrReading(long start, long stop, String speaker, String text, String nativeText, String transliteration, int order) {
        this.start = start;
        this.stop = stop;
        this.speaker = speaker;
        this.text = text;
        this.nativeText = nativeText;
        this.transliteration = transliteration;
        this.order = order;
    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getText() {
        return text;
    }

    public String getNativeText() {
        return nativeText;
    }

    public int getOrder() {
        return order;
    }

    public String getTransliteration() {
        return transliteration;
    }
}
