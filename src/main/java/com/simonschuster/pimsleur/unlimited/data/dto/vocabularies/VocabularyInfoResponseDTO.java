package com.simonschuster.pimsleur.unlimited.data.dto.vocabularies;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.simonschuster.pimsleur.unlimited.data.edt.vocabularies.VocabularyItemFromEdt;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VocabularyInfoResponseDTO {

    public static final String SUCCESS = "SUCCESS";

    public static final String FAILED = "FAILED";

    private String status;

    private List<VocabularyItemFromEdt> vocabularyItemFromEdtList;

    public VocabularyInfoResponseDTO(String status) {
        this.status = status;
    }

    public VocabularyInfoResponseDTO(String status, List<VocabularyItemFromEdt> vocabularyItemFromEdtList) {
        this.status = status;
        this.vocabularyItemFromEdtList = vocabularyItemFromEdtList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VocabularyItemFromEdt> getVocabularyItemFromEdtList() {
        return vocabularyItemFromEdtList;
    }

    public void setVocabularyItemFromEdtList(List<VocabularyItemFromEdt> vocabularyItemFromEdtList) {
        this.vocabularyItemFromEdtList = vocabularyItemFromEdtList;
    }
}
