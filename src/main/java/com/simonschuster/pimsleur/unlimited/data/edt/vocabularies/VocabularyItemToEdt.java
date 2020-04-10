package com.simonschuster.pimsleur.unlimited.data.edt.vocabularies;

import com.simonschuster.pimsleur.unlimited.data.dto.vocabularies.VocabularyItemDTO;

public class VocabularyItemToEdt {

    private String key;
    private String trlitn;
    private String trln;
    private String fnm;
    private Integer pgn;
    private Integer kln;

    public VocabularyItemToEdt(VocabularyItemDTO vocabularyItemDTO) {
        this.key = vocabularyItemDTO.getLanguage();
        this.trlitn = vocabularyItemDTO.getTransliteration();
        this.trln = vocabularyItemDTO.getTranslation();
        this.fnm = vocabularyItemDTO.getMp3FileName();
        this.kln = vocabularyItemDTO.getLessonNumber();
        this.pgn = vocabularyItemDTO.getPackGroupNumber();
    }

    public String getKey() {
        return key;
    }

    public String getTrlitn() {
        return trlitn;
    }

    public String getTrln() {
        return trln;
    }

    public String getFnm() {
        return fnm;
    }

    public Integer getPgn() {
        return pgn;
    }

    public Integer getKln() {
        return kln;
    }
}
