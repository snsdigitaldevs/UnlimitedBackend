package com.simonschuster.pimsleur.unlimited.data.dto.vocabularies;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.simonschuster.pimsleur.unlimited.data.edt.vocabularies.VocabularyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VocabularyInfoResponseDTO {

    public static final String SUCCESS = "SUCCESS";

    public static final String FAILED = "FAILED";


    private String status;

    private List<VocabularyItem> vocabularyItemList;

    public VocabularyInfoResponseDTO(String status) {
        this.status = status;
    }

    public VocabularyInfoResponseDTO(String status, List<VocabularyItem> vocabularyItemList) {
        this.status = status;
        this.vocabularyItemList = vocabularyItemList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VocabularyItem> getVocabularyItemList() {
        return vocabularyItemList;
    }

    public void setVocabularyItemList(List<VocabularyItem> vocabularyItemList) {
        this.vocabularyItemList = vocabularyItemList;
    }
}
