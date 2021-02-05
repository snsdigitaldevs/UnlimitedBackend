package com.simonschuster.pimsleur.unlimited.data.dto.update;

public class CheckUpdateDto {
    private String releaseNote;
    private boolean hasUpdate;
    private boolean forceUpdate;
    private String latestVersion;
    private String updateURL;

    public CheckUpdateDto(String releaseNote, boolean hasUpdate, boolean forceUpdate, String latestVersion, String updateURL) {
        this.releaseNote = releaseNote;
        this.hasUpdate = hasUpdate;
        this.forceUpdate = forceUpdate;
        this.latestVersion = latestVersion;
        this.updateURL = updateURL;
    }

    public String getUpdateURL() {
        return updateURL;
    }

    public void setUpdateURL(String updateURL) {
        this.updateURL = updateURL;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }
}
