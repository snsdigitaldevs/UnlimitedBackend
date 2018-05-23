package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static com.simonschuster.pimsleur.unlimited.services.course.PUCourseInfoService.MP3_MEDIA_TYPE;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaItem {

    private int mediaItemId;
    private int mediaSetId;
    private int mediaItemTypeId;
    private int mediaItemSequenceNum;
    private int mediaItemFormatId;
    private int mediaItemFileSizeBytes;
    private int mediaItemRemoteAccessRestriction;
    private int mediaItemIsEncrypted;
    private String mediaItemTitle;
    private String mediaItemFilename;
    private String mediaItemRemoteUrl;
    private int mediaItemIsActive;
    private int mediaItemClassId;
    private String mediaItemIdMetadata;

    public int getMediaItemId() {
        return mediaItemId;
    }

    public void setMediaItemId(int mediaItemId) {
        this.mediaItemId = mediaItemId;
    }

    public int getMediaSetId() {
        return mediaSetId;
    }

    public void setMediaSetId(int mediaSetId) {
        this.mediaSetId = mediaSetId;
    }

    public int getMediaItemTypeId() {
        return mediaItemTypeId;
    }

    public void setMediaItemTypeId(int mediaItemTypeId) {
        this.mediaItemTypeId = mediaItemTypeId;
    }

    public int getMediaItemSequenceNum() {
        return mediaItemSequenceNum;
    }

    public void setMediaItemSequenceNum(int mediaItemSequenceNum) {
        this.mediaItemSequenceNum = mediaItemSequenceNum;
    }

    public int getMediaItemFormatId() {
        return mediaItemFormatId;
    }

    public void setMediaItemFormatId(int mediaItemFormatId) {
        this.mediaItemFormatId = mediaItemFormatId;
    }

    public int getMediaItemFileSizeBytes() {
        return mediaItemFileSizeBytes;
    }

    public void setMediaItemFileSizeBytes(int mediaItemFileSizeBytes) {
        this.mediaItemFileSizeBytes = mediaItemFileSizeBytes;
    }

    public int getMediaItemRemoteAccessRestriction() {
        return mediaItemRemoteAccessRestriction;
    }

    public void setMediaItemRemoteAccessRestriction(int mediaItemRemoteAccessRestriction) {
        this.mediaItemRemoteAccessRestriction = mediaItemRemoteAccessRestriction;
    }

    public int getMediaItemIsEncrypted() {
        return mediaItemIsEncrypted;
    }

    public void setMediaItemIsEncrypted(int mediaItemIsEncrypted) {
        this.mediaItemIsEncrypted = mediaItemIsEncrypted;
    }

    public String getMediaItemTitle() {
        return mediaItemTitle;
    }

    public void setMediaItemTitle(String mediaItemTitle) {
        this.mediaItemTitle = mediaItemTitle;
    }

    public String getMediaItemFilename() {
        return mediaItemFilename;
    }

    public void setMediaItemFilename(String mediaItemFilename) {
        this.mediaItemFilename = mediaItemFilename;
    }

    public String getMediaItemRemoteUrl() {
        return mediaItemRemoteUrl;
    }

    public void setMediaItemRemoteUrl(String mediaItemRemoteUrl) {
        this.mediaItemRemoteUrl = mediaItemRemoteUrl;
    }

    public int getMediaItemIsActive() {
        return mediaItemIsActive;
    }

    public void setMediaItemIsActive(int mediaItemIsActive) {
        this.mediaItemIsActive = mediaItemIsActive;
    }

    public int getMediaItemClassId() {
        return mediaItemClassId;
    }

    public void setMediaItemClassId(int mediaItemClassId) {
        this.mediaItemClassId = mediaItemClassId;
    }

    public String getMediaItemIdMetadata() {
        return mediaItemIdMetadata;
    }

    public void setMediaItemIdMetadata(String mediaItemIdMetadata) {
        this.mediaItemIdMetadata = mediaItemIdMetadata;
    }

    public boolean isLesson() {
        return this.getMediaItemTypeId() == MP3_MEDIA_TYPE
                && (this.getMediaItemTitle().startsWith("Unit") || this.getMediaItemTitle().startsWith("Lesson"));
    }
}
