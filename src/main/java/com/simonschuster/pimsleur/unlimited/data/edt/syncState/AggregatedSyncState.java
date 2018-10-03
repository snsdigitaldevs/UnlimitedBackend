package com.simonschuster.pimsleur.unlimited.data.edt.syncState;

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

    public SyncState getPcmSyncState() {
        return pcmSyncState;
    }

}
