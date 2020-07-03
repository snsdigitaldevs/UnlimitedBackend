package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class Readings {

    private String pdf;
    private String pdfName;
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

    public String getPdfName() {
        return pdfName;
    }

    @JsonIgnore
    public void setPdfName() {
        boolean hasCultureNotes = this.getAudios().stream().anyMatch(ReadingAudio::isCultureNotes);
        boolean hasReadingLesson = this.getAudios().stream().anyMatch(ReadingAudio::isReadingLesson);
        if (hasReadingLesson && hasCultureNotes) {
            this.pdfName = "Reading Booklet & Culture Notes";
        } else if (hasCultureNotes) {
            this.pdfName = "Culture Notes";
        } else if (hasReadingLesson) {
            this.pdfName = "Reading Booklet";
        } else {
            this.pdfName = StringUtils.EMPTY;
        }
    }
}
