package com.simonschuster.pimsleur.unlimited.data.dto.practices;

public class PracticesInUnit {

    private Integer unitNumber;

    private boolean hasFlashCard;
    private boolean hasQuickMatch;
    private boolean hasSpeakEasy;
    private boolean hasReading;
    private boolean hasSkills;

    public PracticesInUnit(Integer unitNumber, boolean hasFlashCard, boolean hasQuickMatch, boolean hasSpeakEasy, boolean hasReading, boolean hasSkills) {
        this.unitNumber = unitNumber;

        this.hasFlashCard = hasFlashCard;
        this.hasQuickMatch = hasQuickMatch;
        this.hasSpeakEasy = hasSpeakEasy;
        this.hasReading = hasReading;
        this.hasSkills = hasSkills;
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
}
