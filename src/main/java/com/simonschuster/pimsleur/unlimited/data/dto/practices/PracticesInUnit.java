package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class PracticesInUnit {

    private Integer unitNumber;

    private boolean hasFlashCard;
    private boolean hasQuickMatch;
    private boolean hasSpeakEasy;
    private boolean hasReading;
    private boolean hasSkills;

    private List<QuickMatch> quickMatches;
    private List<SpeakEasyOrReading> speakEasies = new ArrayList<>();
    private List<FlashCard> flashCards = new ArrayList<>();
    private List<SpeakEasyOrReading> readings = new ArrayList<>();

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

    public void setFlashCards(List<FlashCard> cards) {
        this.flashCards = cards;
    }

    public void setQuickMatches(List<QuickMatch> matches) {
        this.quickMatches = matches;
    }

    public List<SpeakEasyOrReading> getSpeakEasies() {
        return speakEasies;
    }

    public List<SpeakEasyOrReading> getReadings() {
        return readings;
    }

    public PracticesInUnit mergeWith(PracticesInUnit that) {
        PracticesInUnit mergedResult = new PracticesInUnit(this.unitNumber);

        mergedResult.setHasReading(this.hasReading || that.hasReading);
        mergedResult.setHasFlashCard(this.hasFlashCard || that.hasFlashCard);
        mergedResult.setHasQuickMatch(this.hasQuickMatch || that.hasQuickMatch);
        mergedResult.setHasSpeakEasy(this.hasSpeakEasy || that.hasSpeakEasy);
        mergedResult.setHasSkills(this.hasSkills || that.hasSkills);

        mergedResult.speakEasies = pickNotNullOrEmpty(this.speakEasies, that.speakEasies);
        mergedResult.flashCards = pickNotNullOrEmpty(this.flashCards, that.flashCards);
        mergedResult.quickMatches = pickNotNullOrEmpty(this.quickMatches, that.quickMatches);
        mergedResult.readings = pickNotNullOrEmpty(this.readings, that.readings);

        return mergedResult;
    }

    private <T> List<T> pickNotNullOrEmpty(List<T> one, List<T> two) {
        if (one != null && one.size() > 0) {
            return one;
        }
        return two;
    }

    public static PracticesInUnit createWithSpeakEasies(int unitNumber, List<SpeakEasyOrReading> speakEasies) {
        PracticesInUnit practicesInUnit = new PracticesInUnit(unitNumber);
        practicesInUnit.speakEasies = speakEasies;
        practicesInUnit.setHasSpeakEasy(speakEasies.size() > 0);
        return practicesInUnit;
    }

    public static PracticesInUnit createWithReadings(int unitNumber, List<SpeakEasyOrReading> speakEasies) {
        PracticesInUnit practicesInUnit = new PracticesInUnit(unitNumber);
        practicesInUnit.readings = speakEasies;
        practicesInUnit.setHasReading(speakEasies.size() > 0);
        return practicesInUnit;
    }

    public static PracticesInUnit createWithFlashCards(int unitNumber, List<FlashCard> flashCards) {
        PracticesInUnit practicesInUnit = new PracticesInUnit(unitNumber);
        practicesInUnit.flashCards = flashCards;
        practicesInUnit.setHasFlashCard(flashCards.size() > 0);
        return practicesInUnit;
    }

}
