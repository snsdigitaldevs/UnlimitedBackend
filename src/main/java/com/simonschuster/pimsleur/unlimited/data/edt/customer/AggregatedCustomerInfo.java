package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.CustomerInfoDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    public CustomerInfoDTO toDto() throws IOException {
        Customer puCustomer = unlimitedCustomerInfo.getResultData().getCustomer();

        CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO(
                puCustomer.getProductCodes(),
                pcmCustomerInfo.getResultData().getCustomer().getProductCodes(),
                unlimitedCustomerInfo.getResultData().getRegistrant().getProductActivations(),
                getProgressDTOS(),
                unlimitedCustomerInfo.getResultData().getRegistrant().getSubUsers());
        if (pcmSyncState.hasResultData()) {
            customerInfoDTO.setPcmLastSaveId(pcmSyncState.getResultData().getLastSaveId());
        }
        if (unlimitedSyncState.hasResultData()) {
            customerInfoDTO.setUnlimitedLastSaveId(unlimitedSyncState.getResultData().getLastSaveId());
        }
        customerInfoDTO.setCustomerId(puCustomer.getCustomersId().toString());
        customerInfoDTO.setHasPendingAndroid(puCustomer.hasPendingAndroid());
        customerInfoDTO.setHasPendingIos(puCustomer.hasPendingIos());
        customerInfoDTO.setRegistrantId(unlimitedCustomerInfo.getResultData().getRegistrant().getRegistrantId().toString());
        customerInfoDTO.setOptinNewProductInfo(unlimitedCustomerInfo.getResultData().getRegistrant().getOptinNewProductInfo().toString());
        customerInfoDTO.setIdentityVerificationToken(puCustomer.getIdentityVerificationToken());
        return customerInfoDTO;
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

    private void pickCurrentOfTwoGroups(List<ProgressDTO> pcmProgressDTOs, List<ProgressDTO> unlimitedProgressDTOs) {
        Optional<ProgressDTO> firstPcm = getFirst(pcmProgressDTOs);
        Optional<ProgressDTO> firstUnlimited = getFirst(unlimitedProgressDTOs);
        if (firstPcm.isPresent() && firstUnlimited.isPresent()) {
            if (firstPcm.get().getLastPlayedDate() > firstUnlimited.get().getLastPlayedDate()) {
                firstUnlimited.get().setCurrent(false);
            } else {
                firstPcm.get().setCurrent(false);
            }
        }
    }

    private Optional<ProgressDTO> getFirst(List<ProgressDTO> progressDTOs) {
        return progressDTOs.stream().filter(dto -> dto.getCurrent()).findFirst();
    }

}
