package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.AggregatedCustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Customer;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import com.simonschuster.pimsleur.unlimited.services.syncState.EDTSyncStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.util.stream.Collectors.toList;

@Service
public class EDTCustomerInfoService {

    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private EDTSyncStateService syncStateService;
    @Autowired
    private AppIdService appIdService;

    public AggregatedCustomerInfo getCustomerInfos(String sub, String storeDomain, String email) {
        CompletableFuture<CustomerInfo> puCustomerInfoCompletableFuture = CompletableFuture.supplyAsync(() -> getPUCustomerInfo(sub, storeDomain, email));
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

    public CustomerInfo getPUCustomerInfo(String sub, String storeDomain, String email) {
        return getCustomerInfo(sub,
                config.getApiParameter("unlimitedCustomerAction"),
                storeDomain,
                email);
    }

    public CustomerInfo getPcmCustomerInfo(String sub, String storeDomain, String email) {
        return getCustomerInfo(sub,
                config.getApiParameter("pcmCustomerAction"),
                storeDomain,
                email);
    }

    public List<String> getBoughtIsbns(String sub, String email) {
        return Stream.concat(
                getPUCustomerInfo(sub, "", email).getResultData().getCustomer().getProductCodes().stream(),
                getPcmCustomerInfo(sub, "", email).getResultData().getCustomer().getProductCodes().stream())
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
