package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import static com.simonschuster.pimsleur.unlimited.data.dto.productinfo.ReadingAudio.CULTURE_NOTES;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChildMediaSet {

    private static final String READING_LESSONS = "Reading Lessons";
    private static final String KEY_LESSONS = "Lessons";
    private static final String KEY_UNITS = "Units";
    private static final String ESL_SPANISH_PCM_LESSONS = "Lecciónes";
    private static final String ESL_SPANISH_PCM_READING_LESSONS = "Lectura lecciónes";

    private int mediaSetId;
    private int mediaSetParentId;
    private String mediaSetDescription;
    private String mediaSetTitle;
    private int mediaSetIsActive;
    private int mediaSetQualifier;
    private int mediaSetSortOrder;
    private List<Object> childMediaSets;
    private List<MediaItem> mediaItems;

    public int getMediaSetId() {
        return mediaSetId;
    }

    public void setMediaSetId(int mediaSetId) {
        this.mediaSetId = mediaSetId;
    }

    public int getMediaSetParentId() {
        return mediaSetParentId;
    }

    public void setMediaSetParentId(int mediaSetParentId) {
        this.mediaSetParentId = mediaSetParentId;
    }

    public String getMediaSetDescription() {
        return mediaSetDescription;
    }

    public void setMediaSetDescription(String mediaSetDescription) {
        this.mediaSetDescription = mediaSetDescription;
    }

    public String getMediaSetTitle() {
        return mediaSetTitle;
    }

    public void setMediaSetTitle(String mediaSetTitle) {
        this.mediaSetTitle = mediaSetTitle;
    }

    public int getMediaSetIsActive() {
        return mediaSetIsActive;
    }

    public void setMediaSetIsActive(int mediaSetIsActive) {
        this.mediaSetIsActive = mediaSetIsActive;
    }

    public int getMediaSetQualifier() {
        return mediaSetQualifier;
    }

    public void setMediaSetQualifier(int mediaSetQualifier) {
        this.mediaSetQualifier = mediaSetQualifier;
    }

    public int getMediaSetSortOrder() {
        return mediaSetSortOrder;
    }

    public void setMediaSetSortOrder(int mediaSetSortOrder) {
        this.mediaSetSortOrder = mediaSetSortOrder;
    }

    public List<?> getChildMediaSets() {
        return childMediaSets;
    }

    public void setChildMediaSets(List<Object> childMediaSets) {
        this.childMediaSets = childMediaSets;
    }

    public List<MediaItem> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(List<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    public boolean isReading() {
        String title = getMediaSetTitle();
        return title.contains(READING_LESSONS) ||
                title.contains(CULTURE_NOTES) ||
                title.contains(ESL_SPANISH_PCM_READING_LESSONS);
    }

    public boolean isLesson() {
        String title = getMediaSetTitle();
        return title.contains(KEY_UNITS) ||
                title.contains(KEY_LESSONS) ||
                title.contains(ESL_SPANISH_PCM_LESSONS);
    }
}
