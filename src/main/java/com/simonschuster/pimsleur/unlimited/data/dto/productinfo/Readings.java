package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class Readings {

    private String pdf;
    private String puReadingAlphabetPdf;
    private String puReadingIntroPdf;
    private List<ReadingAudio> audios = new ArrayList<>();

    public String getPdf() {
        return pdf;
    }

    public List<ReadingAudio> getAudios() {
        return audios;
    }

    public void setPdf(String pcmReadingPdf) {
        this.pdf = pcmReadingPdf;
    }

    public String getPuReadingAlphabetPdf() {
        return puReadingAlphabetPdf;
    }

    public void setPuReadingAlphabetPdf(String puReadingAlphabetPdf) {
        this.puReadingAlphabetPdf = puReadingAlphabetPdf;
    }

    public String getPuReadingIntroPdf() {
        return puReadingIntroPdf;
    }

    public void setPuReadingIntroPdf(String puReadingIntroPdf) {
        this.puReadingIntroPdf = puReadingIntroPdf;
    }

    public void setAudios(List<ReadingAudio> audios) {
        this.audios = audios;
    }
}
