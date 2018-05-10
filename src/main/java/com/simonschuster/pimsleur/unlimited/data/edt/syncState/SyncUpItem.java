package com.simonschuster.pimsleur.unlimited.data.edt.syncState;

public class SyncUpItem {
    private String value;
    private String parentId;
    private String stateChangeType;
    private long lastChangeTimestamp;

    public SyncUpItem(String value, String parentId, String stateChangeType, long lastChangeTimestamp) {
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

    public String getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = Long.toString(value);
    }

    public void setValue(String value) {
        this.value = value;
    }
}
