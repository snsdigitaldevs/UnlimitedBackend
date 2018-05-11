package com.simonschuster.pimsleur.unlimited.data.dto.syncUp;

public class SyncUpProgressDto {
    Long lastPlayHeadLocation;
    Long lastPlayedDate;
    Long furthestPlayHeadLocation;
    Long lastChangeTimestamp;
    Boolean isCompleted;
    Long lastCompletionDate;

    public Long getLastPlayHeadLocation() {
        return lastPlayHeadLocation;
    }

    public void setLastPlayHeadLocation(Long lastPlayHeadLocation) {
        this.lastPlayHeadLocation = lastPlayHeadLocation;
    }

    public Long getLastPlayedDate() {
        return lastPlayedDate;
    }

    public void setLastPlayedDate(Long lastPlayedDate) {
        this.lastPlayedDate = lastPlayedDate;
    }

    public Long getFurthestPlayHeadLocation() {
        return furthestPlayHeadLocation;
    }

    public void setFurthestPlayHeadLocation(Long furthestPlayHeadLocation) {
        this.furthestPlayHeadLocation = furthestPlayHeadLocation;
    }

    public Long getLastChangeTimestamp() {
        return lastChangeTimestamp;
    }

    public void setLastChangeTimestamp(Long lastChangeTimestamp) {
        this.lastChangeTimestamp = lastChangeTimestamp;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Long getLastCompletionDate() {
        return lastCompletionDate;
    }

    public void setLastCompletionDate(Long lastCompletionDate) {
        this.lastCompletionDate = lastCompletionDate;
    }
}
