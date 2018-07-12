package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LanguageImageMetadata {
    private String imageFileName;
    private String credits;
    private String imageFilePath;

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
}
