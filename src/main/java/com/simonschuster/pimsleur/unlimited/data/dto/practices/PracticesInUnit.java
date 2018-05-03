package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import java.util.ArrayList;
import java.util.List;

public class PracticesInUnit {

    private Integer unitNumber;

    private boolean hasFlashCard;
    private boolean hasQuickMatch;
    private boolean hasSpeakEasy;
    private boolean hasReading;
    private boolean hasSkills;

    private List<QuickMatch> quickMatches;
    private List<SpeakEasy> speakEasies;
    private List<FlashCard> flashCards;

    public PracticesInUnit(Integer unitNumber) {
        this.unitNumber = unitNumber;
        this.quickMatches = new ArrayList<>();
    }

    // factory method
    public static PracticesInUnit onlyReadingAvailable(Integer unitNumber) {
        PracticesInUnit practicesInUnit = new PracticesInUnit(unitNumber);
        practicesInUnit.setHasReading(true);
        return practicesInUnit;
    }

    public List<QuickMatch> getQuickMatches() {
        return quickMatches;
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

    public List<FlashCard> getFlashCards() {
        return flashCards;
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
        mergedResult.flashCards = pickNotNullOrEmpty(this.flashCards, that.flashCards);
        mergedResult.quickMatches = pickNotNullOrEmpty(this.quickMatches, that.quickMatches);

        return mergedResult;
    }

    private <T> List<T> pickNotNullOrEmpty(List<T> one, List<T> two) {
        if (one != null && one.size() > 0) {
            return one;
        }
        return two;
    }

    public static PracticesInUnit createWithSpeakEasies(int unitNumber, List<SpeakEasy> speakEasies) {
        PracticesInUnit practicesInUnit = new PracticesInUnit(unitNumber);
        practicesInUnit.speakEasies = speakEasies;
        practicesInUnit.setHasSpeakEasy(speakEasies.size() > 0);
        return practicesInUnit;
    }

    public static PracticesInUnit createWithFlashCards(int unitNumber, List<FlashCard> flashCards) {
        PracticesInUnit practicesInUnit = new PracticesInUnit(unitNumber);
        practicesInUnit.flashCards = flashCards;
        practicesInUnit.setHasFlashCard(flashCards.size() > 0);
        return practicesInUnit;
    }
}
