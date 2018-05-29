package com.simonschuster.pimsleur.unlimited.services.practices;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Paths;

import static java.net.URLEncoder.encode;

public class PracticesUrls {
    private String flashCardUrl;
    private String quickMatchUrl;
    private String speakEasyUrl;
    private String readingUrl;

    private String reviewAudioBaseUrl;

    public PracticesUrls() {
    }

    public PracticesUrls(String flashCardUrl, String quickMatchUrl, String readingUrl, String speakEasyUrl) {
        this.flashCardUrl = flashCardUrl;
        this.quickMatchUrl = quickMatchUrl;
        this.readingUrl = readingUrl;
        this.speakEasyUrl = speakEasyUrl;
    }

    public void setFlashCardUrl(String flashCardUrl) {
        this.flashCardUrl = flashCardUrl;
    }

    public void setQuickMatchUrl(String quickMatchUrl) {
        this.quickMatchUrl = quickMatchUrl;
    }

    public void setSpeakEasyUrl(String speakEasyUrl) {
        this.speakEasyUrl = speakEasyUrl;
    }

    public void setReadingUrl(String readingUrl) {
        this.readingUrl = readingUrl;
    }

    public String getFlashCardUrl() {
        return flashCardUrl;
    }

    public String getQuickMatchUrl() {
        return quickMatchUrl;
    }

    public String getSpeakEasyUrl() {
        return speakEasyUrl;
    }

    public String getReadingUrl() {
        return readingUrl;
    }

    public String getReviewAudioBaseUrl() {
        return reviewAudioBaseUrl;
    }

    public void setReviewAudioBaseUrl(String reviewAudioBaseUrl) {
        this.reviewAudioBaseUrl = reviewAudioBaseUrl;
    }

    public String getFlashCardAudioBaseUrl() {
        return this.reviewAudioBaseUrl + "fc/";
    }

    public String getFlashCardAudioBaseFileName() {
        try {
            String csvFileName = Paths.get(new URI(encode(this.flashCardUrl, "UTF-8"))
                    .getPath()).getFileName().toString();
            return csvFileName.split(".csv")[0];
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            return "";
        }
    }

    public String getQuickMatchAudioBaseUrl() {
        return this.reviewAudioBaseUrl + "quiz/";
    }
}
