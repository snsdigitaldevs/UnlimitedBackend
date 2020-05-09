package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

// speak easy and reading has same data structure
@JsonInclude(NON_EMPTY)
public class SpeakEasyOrReading {
    private long start;
    private long stop;

    private String speaker;
    private String text;
    private String nativeText;
    private String optionalText;
    private String helpText;

    private int order;

    public SpeakEasyOrReading(long start, long stop,
                              String speaker, String text,
                              String nativeText, String optionalText,
                              String helpText, int order) {
        this.start = start;
        this.stop = stop;
        this.speaker = speaker;
        this.text = text;
        this.nativeText = nativeText;
        this.optionalText = optionalText;
        this.helpText = helpText;
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

    public String getOptionalText() {
        return optionalText;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setStop(long stop) {
        this.stop = stop;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNativeText(String nativeText) {
        this.nativeText = nativeText;
    }

    public void setOptionalText(String optionalText) {
        this.optionalText = optionalText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
