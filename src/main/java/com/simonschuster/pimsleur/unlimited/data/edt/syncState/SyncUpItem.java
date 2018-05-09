package com.simonschuster.pimsleur.unlimited.data.edt.syncState;

public class SyncUpItem {
    private long value;
    private String parentId;
    private String stateChangeType;
    private long lastChangeTimestamp;

    public SyncUpItem(long value, String parentId, String stateChangeType, long lastChangeTimestamp) {
        this.value = value;
        this.parentId = parentId;
        this.stateChangeType = stateChangeType;
        this.lastChangeTimestamp = lastChangeTimestamp;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStateChangeType() {
        return stateChangeType;
    }

    public void setStateChangeType(String stateChangeType) {
        this.stateChangeType = stateChangeType;
    }

    public long getLastChangeTimestamp() {
        return lastChangeTimestamp;
    }

    public void setLastChangeTimestamp(long lastChangeTimestamp) {
        this.lastChangeTimestamp = lastChangeTimestamp;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
