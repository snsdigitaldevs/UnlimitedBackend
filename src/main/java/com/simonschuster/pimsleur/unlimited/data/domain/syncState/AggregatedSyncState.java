package com.simonschuster.pimsleur.unlimited.data.domain.syncState;

public class AggregatedSyncState {
    private SyncState unlimitedSyncState;
    private SyncState pcmSyncState;

    public AggregatedSyncState(SyncState unlimitedSyncState, SyncState pcmSyncState) {
        this.unlimitedSyncState = unlimitedSyncState;
        this.pcmSyncState = pcmSyncState;
    }

    public SyncState getUnlimitedSyncState() {
        return unlimitedSyncState;
    }

    public void setUnlimitedSyncState(SyncState unlimitedSyncState) {
        this.unlimitedSyncState = unlimitedSyncState;
    }

    public SyncState getPcmSyncState() {
        return pcmSyncState;
    }

    public void setPcmSyncState(SyncState pcmSyncState) {
        this.pcmSyncState = pcmSyncState;
    }
}
