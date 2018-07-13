package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "imageFileName",
        "credits",
        "imageFilePath"
})
public class LanguageImageMetadata {

    @JsonProperty("imageFileName")
    private String imageFileName;
    @JsonProperty("credits")
    private String credits;
    @JsonProperty("imageFilePath")
    private String imageFilePath;

    @JsonProperty("imageFileName")
    public String getImageFileName() {
        return imageFileName;
    }

    @JsonProperty("imageFileName")
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @JsonProperty("credits")
    public String getCredits() {
        return credits;
    }

    @JsonProperty("credits")
    public void setCredits(String credits) {
        this.credits = credits;
    }

    @JsonProperty("imageFilePath")
    public String getImageFilePath() {
        return imageFilePath;
    }

    @JsonProperty("imageFilePath")
    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

}