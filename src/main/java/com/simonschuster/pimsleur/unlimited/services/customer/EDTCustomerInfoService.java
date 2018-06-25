package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.AggregatedCustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Customer;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.services.syncState.EDTSyncStateService;
import com.simonschuster.pimsleur.unlimited.utils.InAppPurchaseUtil;
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

    public AggregatedCustomerInfo getCustomerInfos(String sub, String storeDomain) {
        CompletableFuture<CustomerInfo> puCustomerInfoCompletableFuture = CompletableFuture.supplyAsync(() -> getPUCustomerInfo(sub, storeDomain));
        CompletableFuture<CustomerInfo> pcmCustomerInfoCompletableFuture = CompletableFuture.supplyAsync(() -> getPcmCustomerInfo(sub,storeDomain));

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

    public CustomerInfo getPUCustomerInfo(String sub, String storeDomain) {
        return getCustomerInfo(sub,
                config.getApiParameter("unlimitedCustomerAction"),
                storeDomain);
    }

    public CustomerInfo getPcmCustomerInfo(String sub, String storeDomain) {
        return getCustomerInfo(sub,
                config.getApiParameter("pcmCustomerAction"),
                storeDomain);
    }

    public List<String> getBoughtIsbns(String sub) {
        return Stream.concat(
                getPUCustomerInfo(sub, "").getResultData().getCustomer().getProductCodes().stream(),
                getPcmCustomerInfo(sub, "").getResultData().getCustomer().getProductCodes().stream())
                .collect(toList());
    }

    private CustomerInfo getCustomerInfo(String sub, String action, String domain) {
        return postToEdt(
                createPostBody(sub, action, domain),
                config.getProperty("edt.api.customerInfoApiUrl"),
                CustomerInfo.class);
    }

    private HttpEntity<String> createPostBody(String sub, String action, String domain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(
                String.format(config.getApiParameter("customerInfoDefaultParameters"),
                        sub, action, domain, InAppPurchaseUtil.getAppId(domain)),
                headers);
    }
}
