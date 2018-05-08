package com.simonschuster.pimsleur.unlimited.data.dto.syncUp;

public class SyncUpDto {
    private String deviceName;
    private String identityVerificationToken;
    private Long lastSaveId;
    private SyncUpProgressDto progress;

    public String getDeviceName() {
        return deviceName;
    }

    public String getIdentityVerificationToken() {
        return identityVerificationToken;
    }

    public Long getLastSaveId() {
        return lastSaveId;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setIdentityVerificationToken(String identityVerificationToken) {
        this.identityVerificationToken = identityVerificationToken;
    }

    public void setLastSaveId(Long lastSaveId) {
        this.lastSaveId = lastSaveId;
    }

    public SyncUpProgressDto getProgress() {
        return progress;
    }

    public void setProgress(SyncUpProgressDto progress) {
        this.progress = progress;
    }
}
