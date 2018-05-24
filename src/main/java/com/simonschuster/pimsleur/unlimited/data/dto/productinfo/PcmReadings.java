package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class PcmReadings {
    private String pdf;
    private List<PcmReadingAudio> audios;

    public PcmReadings(String pdf, List<PcmReadingAudio> audios) {
        this.pdf = pdf;
        this.audios = audios;
    }

    public String getPdf() {
        return pdf;
    }

    public List<PcmReadingAudio> getAudios() {
        return audios;
    }
}
