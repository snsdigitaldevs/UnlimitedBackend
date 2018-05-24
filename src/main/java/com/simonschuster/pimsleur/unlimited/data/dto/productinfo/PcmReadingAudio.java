package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class PcmReadingAudio {
    private int order;
    private String title;
    private String audioLink;
    private int startPage;
    private int endPage;

    public PcmReadingAudio(int order, String title, String audioLink, int startPage, int endPage) {
        this.order = order;
        this.title = title;
        this.audioLink = audioLink;
        this.startPage = startPage;
        this.endPage = endPage;
    }

    public int getOrder() {
        return order;
    }

    public String getTitle() {
        return title;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }
}
