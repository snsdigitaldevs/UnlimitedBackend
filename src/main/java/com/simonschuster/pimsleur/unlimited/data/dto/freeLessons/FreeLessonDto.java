package com.simonschuster.pimsleur.unlimited.data.dto.freeLessons;

public class FreeLessonDto {
    private String languageName;
    private String productCode;
    private String audioLink;
    private String imageUrl;

    public FreeLessonDto(String languageName, String productCode, String audioLink, String imageUrl) {
        this.languageName = languageName;
        this.productCode = productCode;
        this.audioLink = audioLink;
        this.imageUrl = imageUrl;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}
