package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class Readings {

    // pdf only exists for pcm
    private String pdf;
    private List<ReadingAudio> audios = new ArrayList<>();

    public String getPdf() {
        return pdf;
    }

    public List<ReadingAudio> getAudios() {
        return audios;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public void setAudios(List<ReadingAudio> audios) {
        this.audios = audios;
    }
}
