package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class PcmReadings {
    private String pdf;
    private List<PcmReadingAudio> audios = new ArrayList<>();

    public String getPdf() {
        return pdf;
    }

    public List<PcmReadingAudio> getAudios() {
        return audios;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public void setAudios(List<PcmReadingAudio> audios) {
        this.audios = audios;
    }
}
