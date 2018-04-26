package com.simonschuster.pimsleur.unlimited.data.dto.practices;

public class PracticesInUnit {

    private Integer unitNumber;

    private boolean hasFlashCard;
    private boolean hasQuickMatch;
    private boolean hasSpeakEasy;
    private boolean hasReading;
    private boolean hasSkills;

    public PracticesInUnit(Integer unitNumber) {
        this.unitNumber = unitNumber;
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

    public void setHasReading(boolean hasReading) {
        this.hasReading = hasReading;
    }
}
