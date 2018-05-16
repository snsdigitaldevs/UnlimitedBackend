package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

public class CultureContent {
    private String imageLocation;
    private String imageDescription;
    private String imageCredits;

    public CultureContent(String imageLocation, String imageDescription, String imageCredits) {
        this.imageLocation = imageLocation;
        this.imageDescription = imageDescription;
        this.imageCredits = imageCredits;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public String getImageDescription() {

        return imageDescription;
    }

    public String getImageCredits() {
        return imageCredits;
    }
}
