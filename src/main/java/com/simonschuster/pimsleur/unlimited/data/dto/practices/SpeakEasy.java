package com.simonschuster.pimsleur.unlimited.data.dto.practices;

public class SpeakEasy {
    private long start;
    private long stop;
    private String speaker;
    private String text;
    private String nativeText;
    private int order;

    public SpeakEasy(long start, long stop, String speaker, String text, String nativeText, int order) {
        this.start = start;
        this.stop = stop;
        this.speaker = speaker;
        this.text = text;
        this.nativeText = nativeText;
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
}
