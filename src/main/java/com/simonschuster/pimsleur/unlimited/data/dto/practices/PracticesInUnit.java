package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import java.util.List;

public class PracticesInUnit {

    private Integer unitNumber;

    private boolean hasFlashCard;
    private boolean hasQuickMatch;
    private boolean hasSpeakEasy;
    private boolean hasReading;
    private boolean hasSkills;

    private List<SpeakEasy> speakEasies;

    public PracticesInUnit(Integer unitNumber) {
        this.unitNumber = unitNumber;
    }

    // factory method
    public static PracticesInUnit onlyReadingAvailable(Integer unitNumber) {
        PracticesInUnit practicesInUnit = new PracticesInUnit(unitNumber);
        practicesInUnit.setHasReading(true);
        return practicesInUnit;
    }

    public boolean isHasFlashCard() {
        return hasFlashCard;
    }

    public boolean isHasQuickMatch() {
        return hasQuickMatch;
    }

    public boolean isHasSpeakEasy() {
        return hasSpeakEasy;
    }

    public boolean isHasReading() {
        return hasReading;
    }

    public boolean isHasSkills() {
        return hasSkills;
    }

    public Integer getUnitNumber() {
        return unitNumber;
    }

    public void setHasFlashCard(boolean hasFlashCard) {
        this.hasFlashCard = hasFlashCard;
    }

    public void setHasQuickMatch(boolean hasQuickMatch) {
        this.hasQuickMatch = hasQuickMatch;
    }

    public void setHasSpeakEasy(boolean hasSpeakEasy) {
        this.hasSpeakEasy = hasSpeakEasy;
    }

    public void setHasSkills(boolean hasSkills) {
        this.hasSkills = hasSkills;
    }

    public void setHasReading(boolean hasReading) {
        this.hasReading = hasReading;
    }

    public List<SpeakEasy> getSpeakEasies() {
        return speakEasies;
    }

    public PracticesInUnit mergeWith(PracticesInUnit that) {
        PracticesInUnit mergedResult = new PracticesInUnit(this.unitNumber);

        mergedResult.setHasReading(this.hasReading || that.hasReading);
        mergedResult.setHasFlashCard(this.hasFlashCard || that.hasFlashCard);
        mergedResult.setHasQuickMatch(this.hasQuickMatch || that.hasQuickMatch);
        mergedResult.setHasSpeakEasy(this.hasSpeakEasy || that.hasSpeakEasy);

        mergedResult.speakEasies = pickNotNullOrEmpty(this.speakEasies, that.speakEasies);

        return mergedResult;
    }

    private <T> List<T> pickNotNullOrEmpty(List<T> one, List<T> two) {
        if (one != null && one.size() > 0) {
            return one;
        }
        return two;
    }
}
