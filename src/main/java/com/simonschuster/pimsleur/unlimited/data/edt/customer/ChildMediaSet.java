package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChildMediaSet {

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

}
