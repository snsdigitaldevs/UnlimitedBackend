package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.CustomerInfoDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.*;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.services.syncState.EDTSyncStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static com.simonschuster.pimsleur.unlimited.utils.PCMProgressConverter.pcmProgressToDto;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedProgressConverter.UnlimitedSyncStateToDTO;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@Service
public class EDTCustomerInfoService {

    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private EDTSyncStateService syncStateService;
    @Autowired
    private AppIdService appIdService;

    public AggregatedCustomerInfo getCustomerInfos(String sub, String storeDomain, String email) {
        CompletableFuture<CustomerInfo> puCustomerInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            CustomerInfo puCustomerInfo = getPUCustomerInfo(sub, storeDomain, email);
            return puCustomerInfo;
        });
        CompletableFuture<CustomerInfo> pcmCustomerInfoCompletableFuture = CompletableFuture.supplyAsync(() -> getPcmCustomerInfo(sub, storeDomain, email));

        AggregatedSyncState aggregatedSyncState = CompletableFuture.anyOf(puCustomerInfoCompletableFuture, pcmCustomerInfoCompletableFuture)
                .thenApplyAsync(puOrPcmCustomerInfo -> {
                    Customer customer = ((CustomerInfo) puOrPcmCustomerInfo).getResultData().getCustomer();
                    return syncStateService
                            .getSyncStates(customer.getCustomersId(), customer.getIdentityVerificationToken());
                })
                .join();

        CustomerInfo pcCustomerInfo = puCustomerInfoCompletableFuture.join();
        CustomerInfo pcmCustomerInfo = pcmCustomerInfoCompletableFuture.join();
        return new AggregatedCustomerInfo(pcCustomerInfo, pcmCustomerInfo, aggregatedSyncState);
    }

    public CustomerInfoDTO getCustomerInfoDTO(String sub, String storeDomain, String email) throws IOException {
        CustomerInfo puAndPCMCustomerInfo = getPuAndPCMCustomerInfos(sub, storeDomain, email);
        ResultData resultData = puAndPCMCustomerInfo.getResultData();
        Customer customer = resultData.getCustomer();
        Registrant registrant = resultData.getRegistrant();

        AggregatedSyncState syncStates = syncStateService.getSyncStates(customer.getCustomersId(), customer.getIdentityVerificationToken());
        SyncState pcmSyncState = syncStates.getPcmSyncState();
        SyncState unlimitedSyncState = syncStates.getUnlimitedSyncState();
        List<ProgressDTO> progresses = getProgressDTOS(pcmSyncState, unlimitedSyncState);

        CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO(
                customer.getPUProductCodes(),
                customer.getPCMProductCodes(),
                registrant.getProductActivations(),
                progresses,
                registrant.getSubUsers());
        customerInfoDTO.setPcmLastSaveId(pcmSyncState.hasResultData() ? pcmSyncState.getResultData().getLastSaveId() : 0L);
        customerInfoDTO.setUnlimitedLastSaveId(unlimitedSyncState.hasResultData() ? unlimitedSyncState.getResultData().getLastSaveId() : 0L);
        customerInfoDTO.setCustomerId(customer.getCustomersId().toString());
        customerInfoDTO.setHasPendingAndroid(customer.hasPendingAndroid());
        customerInfoDTO.setHasPendingIos(customer.hasPendingIos());
        customerInfoDTO.setRegistrantId(registrant.getRegistrantId().toString());
        customerInfoDTO.setOptinNewProductInfo(registrant.getOptinNewProductInfo().toString());
        customerInfoDTO.setIdentityVerificationToken(customer.getIdentityVerificationToken());
        return customerInfoDTO;
    }

    private List<ProgressDTO> getProgressDTOS(SyncState pcmSyncState, SyncState unlimitedSyncState) throws IOException {
        //both
        if (pcmSyncState.hasResultData() && unlimitedSyncState.hasResultData()) {
            List<ProgressDTO> pcmProgressDTOs = pcmProgressToDto(pcmSyncState.getResultData().getUserAppStateData());
            List<ProgressDTO> unlimitedProgressDTOs = UnlimitedSyncStateToDTO(unlimitedSyncState.getResultData().getUserAppStateData());

            return concat(pcmProgressDTOs.stream(), unlimitedProgressDTOs.stream()).collect(toList());
        }
        //only pcm
        else if (pcmSyncState.hasResultData()) {
            return pcmProgressToDto(pcmSyncState.getResultData().getUserAppStateData());
        }
        //only unlimited
        else if (unlimitedSyncState.hasResultData()) {
            return UnlimitedSyncStateToDTO(unlimitedSyncState.getResultData().getUserAppStateData());
        }
        //neither
        return emptyList();
    }

    public CustomerInfo getPuAndPCMCustomerInfos(String sub, String storeDomain, String email) {
        CustomerInfo puAndPCMCustomerInfo = getCustomerInfo(sub,
                config.getApiParameter("pcmAndUnlimitedCustomerAction"),
                storeDomain,
                email);
        return puAndPCMCustomerInfo;
    }

    public CustomerInfo getPUCustomerInfo(String sub, String storeDomain, String email) {
        CustomerInfo unlimitedCustomerAction = getCustomerInfo(sub,
                config.getApiParameter("unlimitedCustomerAction"),
                storeDomain,
                email);
        return unlimitedCustomerAction;
    }

    public CustomerInfo getPcmCustomerInfo(String sub, String storeDomain, String email) {
        CustomerInfo pcmCustomerAction = getCustomerInfo(sub,
                config.getApiParameter("pcmCustomerAction"),
                storeDomain,
                email);
        return pcmCustomerAction;
    }

    public List<String> getBoughtIsbns(String sub, String email, String storeDomain) {
        return Stream.concat(
                getPUCustomerInfo(sub, storeDomain, email).getResultData().getCustomer().getProductCodes().stream(),
                getPcmCustomerInfo(sub, storeDomain, email).getResultData().getCustomer().getProductCodes().stream())
                .collect(toList());
    }

    private CustomerInfo getCustomerInfo(String sub, String action, String domain, String email) {
        return postToEdt(
                createPostBody(sub, action, domain, email),
                config.getProperty("edt.api.customerInfoApiUrl"),
                CustomerInfo.class);
    }

    private HttpEntity<String> createPostBody(String sub, String action, String domain, String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(
                String.format(config.getApiParameter("customerInfoDefaultParameters"),
                        sub, email, action, domain, appIdService.getAppId(domain)),
                headers);
    }
}
