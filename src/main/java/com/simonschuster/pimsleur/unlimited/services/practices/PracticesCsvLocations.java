package com.simonschuster.pimsleur.unlimited.services.practices;

public class PracticesCsvLocations {
    private String flashCardUrl;
    private String quickMatchUrl;
    private String speakEasyUrl;
    private String readingUrl;

    public PracticesCsvLocations(String flashCardUrl, String quickMatchUrl, String readingUrl , String  speakEasyUrl) {
        this.flashCardUrl = flashCardUrl;
        this.quickMatchUrl = quickMatchUrl;
        this.speakEasyUrl = speakEasyUrl;
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
}
