package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;

import java.io.IOException;
import java.util.List;

import static com.simonschuster.pimsleur.unlimited.utils.PCMProgressConverter.pcmProgressToDto;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedProgressConverter.UnlimitedSyncStateToDTO;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class AggregatedCustomerInfo {
    private SyncState pcmSyncState;
    private SyncState unlimitedSyncState;

    private CustomerInfo pcmCustomerInfo;
    private CustomerInfo unlimitedCustomerInfo;

    public AggregatedCustomerInfo(CustomerInfo unlimited, CustomerInfo pcm, AggregatedSyncState aggregatedSyncState) {
        unlimitedCustomerInfo = unlimited;
        pcmCustomerInfo = pcm;

        unlimitedSyncState = aggregatedSyncState.getUnlimitedSyncState();
        pcmSyncState = aggregatedSyncState.getPcmSyncState();
    }

    public CustomerInfo getUnlimitedCustomerInfo() {
        return unlimitedCustomerInfo;
    }

    public CustomerInfo getPcmCustomerInfo() {
        return pcmCustomerInfo;
    }

    public SyncState getPcmSyncState() {
        return pcmSyncState;
    }

    public SyncState getUnlimitedSyncState() {
        return unlimitedSyncState;
    }

    public List<ProgressDTO> getProgressDTOS() throws IOException {
        //both
        if (pcmSyncState.hasResultData() && unlimitedSyncState.hasResultData()) {
            List<ProgressDTO> pcmProgressDTOs = pcmProgressToDto(this.pcmSyncState.getResultData().getUserAppStateData());
            List<ProgressDTO> unlimitedProgressDTOs = UnlimitedSyncStateToDTO(this.unlimitedSyncState.getResultData().getUserAppStateData());

            return concat(pcmProgressDTOs.stream(), unlimitedProgressDTOs.stream()).collect(toList());
        }
        //only pcm
        else if (pcmSyncState.hasResultData()) {
            return pcmProgressToDto(this.pcmSyncState.getResultData().getUserAppStateData());
        }
        //only unlimited
        else if (unlimitedSyncState.hasResultData()) {
            return UnlimitedSyncStateToDTO(this.unlimitedSyncState.getResultData().getUserAppStateData());
        }
        //neither
        return emptyList();
    }

}
