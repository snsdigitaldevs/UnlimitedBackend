package com.simonschuster.pimsleur.unlimited.services.bonusPacks;

public class BonusPacksUrls {

    private String bonusPacksFileUrl;

    private String reviewAudioBaseUrl;

    public BonusPacksUrls(String bonusPacksFileUrl, String reviewAudioBaseUrl) {
        this.bonusPacksFileUrl = bonusPacksFileUrl;
        this.reviewAudioBaseUrl = reviewAudioBaseUrl;
    }

    public String getBonusPacksFileUrl() {
        return bonusPacksFileUrl;
    }

    public void setBonusPacksFileUrl(String bonusPacksFileUrl) {
        this.bonusPacksFileUrl = bonusPacksFileUrl;
    }

    public String getReviewAudioBaseUrl() {
        return reviewAudioBaseUrl + "bp/";
    }

    public void setReviewAudioBaseUrl(String reviewAudioBaseUrl) {
        this.reviewAudioBaseUrl = reviewAudioBaseUrl;
    }
}
