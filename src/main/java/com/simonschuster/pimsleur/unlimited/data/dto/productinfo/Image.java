package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

public class Image {
    private String fullImageAddress;
    private String thumbImageAddress;
    private String credits;

    public String getFullImageAddress() {
        return fullImageAddress;
    }

    public void setFullImageAddress(String fullImageAddress) {
        this.fullImageAddress = fullImageAddress;
    }

    public String getThumbImageAddress() {
        return thumbImageAddress;
    }

    public void setThumbImageAddress(String thumbImageAddress) {
        this.thumbImageAddress = thumbImageAddress;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }
}
