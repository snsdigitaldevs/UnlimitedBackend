package com.simonschuster.pimsleur.unlimited.data.dto.vocabularies;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class VocabularyListInfoDTO {

    @NotNull
    private String customerId;
    @NotNull
    private String subUserId;
    @NotNull
    private String productCode;

    private List<VocabularyItemDTO> vocabularyItemList = new ArrayList<>();

    public String getCustomerId() {
        return customerId;
    }

    public String getSubUserId() {
        return subUserId;
    }

    public String getProductCode() {
        return productCode;
    }

    public List<VocabularyItemDTO> getVocabularyItemList() {
        return vocabularyItemList;
    }

    public VocabularyListInfoDTO setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public VocabularyListInfoDTO setSubUserId(String subUserId) {
        this.subUserId = subUserId;
        return this;
    }

    public VocabularyListInfoDTO setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public VocabularyListInfoDTO setVocabularyItemList(List<VocabularyItemDTO> vocabularyItemList) {
        this.vocabularyItemList = vocabularyItemList;
        return this;
    }
}
