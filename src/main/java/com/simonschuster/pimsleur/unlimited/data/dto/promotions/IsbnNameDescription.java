package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ISBN13",
        "In App Display Name",
        "In App Description"
})
public class IsbnNameDescription {

    @JsonProperty("ISBN13")
    private Long iSBN13;
    @JsonProperty("In App Display Name")
    private String inAppDisplayName;
    @JsonProperty("In App Description")
    private String inAppDescription;

    @JsonProperty("ISBN13")
    public Long getISBN13() {
        return iSBN13;
    }

    @JsonProperty("ISBN13")
    public void setISBN13(Long iSBN13) {
        this.iSBN13 = iSBN13;
    }

    @JsonProperty("In App Display Name")
    public String getInAppDisplayName() {
        return inAppDisplayName;
    }

    @JsonProperty("In App Display Name")
    public void setInAppDisplayName(String inAppDisplayName) {
        this.inAppDisplayName = inAppDisplayName;
    }

    @JsonProperty("In App Description")
    public String getInAppDescription() {
        return inAppDescription;
    }

    @JsonProperty("In App Description")
    public void setInAppDescription(String inAppDescription) {
        this.inAppDescription = inAppDescription;
    }

}