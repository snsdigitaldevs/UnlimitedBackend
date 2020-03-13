package com.simonschuster.pimsleur.unlimited.data.edt.vocabularies;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "vocabularyItemsList"
})
public class VocabularyItemsResultData {

    @JsonProperty("vocabularyItemsList")
    private List<VocabularyItemFromEdt> vocabularyItemFromEdtList = null;

    public List<VocabularyItemFromEdt> getVocabularyItemFromEdtList() {
        return vocabularyItemFromEdtList;
    }

    public void setVocabularyItemFromEdtList(List<VocabularyItemFromEdt> vocabularyItemFromEdtList) {
        this.vocabularyItemFromEdtList = vocabularyItemFromEdtList;
    }
}
