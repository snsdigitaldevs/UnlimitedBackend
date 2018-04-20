package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;

public class AggregatedCustomerInfo {
    private SyncState pcmSyncState;
    private SyncState unlimitedSyncState;
    private CustomerInfo unlimitedCustomerInfo;
    private CustomerInfo pcmCustomerInfo;

    public AggregatedCustomerInfo(CustomerInfo unlimited, CustomerInfo pcm, AggregatedSyncState aggregatedSyncState) {
        unlimitedCustomerInfo = unlimited;
        pcmCustomerInfo = pcm;

        unlimitedSyncState = aggregatedSyncState.getUnlimitedSyncState();
        pcmSyncState = aggregatedSyncState.getPcmSyncState();
    }

    public CustomerInfo getUnlimitedCustomerInfo() {
        return unlimitedCustomerInfo;
    }

    public void setUnlimitedCustomerInfo(CustomerInfo unlimitedCustomerInfo) {
        this.unlimitedCustomerInfo = unlimitedCustomerInfo;
    }

    public CustomerInfo getPcmCustomerInfo() {
        return pcmCustomerInfo;
    }

    public void setPcmCustomerInfo(CustomerInfo pcmCustomerInfo) {
        this.pcmCustomerInfo = pcmCustomerInfo;
    }

    public SyncState getPcmSyncState() {
        return pcmSyncState;
    }

    public void setPcmSyncState(SyncState pcmSyncState) {
        this.pcmSyncState = pcmSyncState;
    }

    public SyncState getUnlimitedSyncState() {
        return unlimitedSyncState;
    }

    public void setUnlimitedSyncState(SyncState unlimitedSyncState) {
        this.unlimitedSyncState = unlimitedSyncState;
    }
}
