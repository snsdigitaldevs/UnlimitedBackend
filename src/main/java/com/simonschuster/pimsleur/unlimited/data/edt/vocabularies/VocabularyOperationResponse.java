package com.simonschuster.pimsleur.unlimited.data.edt.vocabularies;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result_code",
    "result_data"
})
public class VocabularyOperationResponse extends EdtResponseCode {

    @JsonProperty("result_data")
    private VocabularyItemsResultData vocabularyItemsResultData;

    @JsonProperty("result_data")
    public VocabularyItemsResultData getVocabularyItemsResultData() {
        return vocabularyItemsResultData;
    }

    @JsonProperty("result_data")
    public void setVocabularyItemsResultData(VocabularyItemsResultData  vocabularyItemsResultData) {
        this.vocabularyItemsResultData = vocabularyItemsResultData;
    }
}
