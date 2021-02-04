package com.simonschuster.pimsleur.unlimited.data.dto.update;

public class CheckUpdateDto {
    private String releaseNote;
    private boolean hasUpdate;
    private boolean forceUpdate;
    private double latestVersion;

    public CheckUpdateDto(String releaseNote, boolean hasUpdate, boolean forceUpdate, double latestVersion) {
        this.releaseNote = releaseNote;
        this.hasUpdate = hasUpdate;
        this.forceUpdate = forceUpdate;
        this.latestVersion = latestVersion;
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

    public double getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(double latestVersion) {
        this.latestVersion = latestVersion;
    }
}
