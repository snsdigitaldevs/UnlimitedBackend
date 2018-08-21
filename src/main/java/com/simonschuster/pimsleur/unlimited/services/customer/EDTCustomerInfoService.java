package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.CustomerInfoDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Customer;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Registrant;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.ResultData;
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
import java.util.*;
import java.util.stream.Collectors;

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
        customerInfoDTO.setRegistrantName(registrant.getFirstName());
        customerInfoDTO.setOptinNewProductInfo(registrant.getOptinNewProductInfo().toString());
        customerInfoDTO.setIdentityVerificationToken(customer.getIdentityVerificationToken());
        return customerInfoDTO;
    }

    private List<ProgressDTO> getProgressDTOS(SyncState pcmSyncState, SyncState unlimitedSyncState) throws IOException {
        //both
        if (pcmSyncState.hasResultData() && unlimitedSyncState.hasResultData()) {
            List<ProgressDTO> pcmCleaned = getCleanedPCMProgressDTOS(pcmSyncState);
            List<ProgressDTO> unlimitedProgressDTOs = UnlimitedSyncStateToDTO(unlimitedSyncState.getResultData().getUserAppStateData()).stream().distinct().collect(toList());
            return concat(pcmCleaned.stream(), unlimitedProgressDTOs.stream()).collect(toList());
        }
        //only pcm
        else if (pcmSyncState.hasResultData()) {
            return getCleanedPCMProgressDTOS(pcmSyncState);
        }
        //only unlimited
        else if (unlimitedSyncState.hasResultData()) {
            return UnlimitedSyncStateToDTO(unlimitedSyncState.getResultData().getUserAppStateData());
        }
        //neither
        return emptyList();
    }

    private List<ProgressDTO> getCleanedPCMProgressDTOS(SyncState pcmSyncState) throws IOException {
        List<ProgressDTO> pcmProgressDTOs = pcmProgressToDto(pcmSyncState.getResultData().getUserAppStateData()).stream().distinct().collect(toList());
        //map progress dtos by productCode + mediaItemId
        //filter completed = true, if so, skip all other items
        //if no completed = true, then filter current = true
        //if no current = true, then select from current = false, order by last play date, choose the one with max lastplaydate
        List<ProgressDTO> pcmCleaned = new ArrayList<>();
        Map<String, List<ProgressDTO>> map = pcmProgressDTOs.stream().collect(
                Collectors.groupingBy(ProgressDTO::computeIdentifier));
        for (Map.Entry<String, List<ProgressDTO>> entry : map.entrySet()) {
            List<ProgressDTO> value = entry.getValue();
            Optional<ProgressDTO> completed = value.stream().filter(item -> item.getCompleted()).findFirst();
            if(completed.isPresent()){
                pcmCleaned.add(completed.get());
            }
            else {
                Optional<ProgressDTO> current = value.stream().filter(item -> item.getCurrent()).findFirst();
                if(current.isPresent()){
                    pcmCleaned.add(current.get());
                }else {
                    Collections.sort(value, (b1, b2) -> {
                        if(b1.getLastPlayedDate() == null && b2.getLastPlayedDate() == null)
                            return 0;
                        if(b1.getLastPlayedDate() == null && b2.getLastPlayedDate() != null){
                            return -1;
                        }
                        if(b1.getLastPlayedDate() != null && b2.getLastPlayedDate() == null){
                            return 1;
                        }
                        return b1.getLastPlayedDate().compareTo(b2.getLastPlayedDate());
                    });
                    ProgressDTO progressDTO = value.get(value.size() - 1);
                    pcmCleaned.add(progressDTO);
                }
            }
        }
        return pcmCleaned;
    }

    public CustomerInfo getPuAndPCMCustomerInfos(String sub, String storeDomain, String email) {
        CustomerInfo puAndPCMCustomerInfo = getCustomerInfo(sub,
                config.getApiParameter("pcmAndUnlimitedCustomerAction"),
                storeDomain,
                email);
        return puAndPCMCustomerInfo;
    }

    public CustomerInfo getPcmCustomerInfo(String sub, String storeDomain, String email) {
        CustomerInfo pcmCustomerAction = getCustomerInfo(sub,
                config.getApiParameter("pcmCustomerAction"),
                storeDomain,
                email);
        return pcmCustomerAction;
    }

    public List<String> getBoughtIsbns(String sub, String email, String storeDomain) {
        CustomerInfo customerInfos = getPuAndPCMCustomerInfos(sub, storeDomain, email);
        List<String> productCodes = customerInfos.getResultData().getCustomer().getProductCodes();
        return productCodes;
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
