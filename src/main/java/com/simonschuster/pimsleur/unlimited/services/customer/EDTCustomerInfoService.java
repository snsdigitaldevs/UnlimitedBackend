package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.AggregatedCustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.Customer;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.services.syncState.EDTSyncStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class EDTCustomerInfoService {

    @Autowired
    private ApplicationConfiguration config;

    @Autowired
    private EDTSyncStateService syncStateService;

    public AggregatedCustomerInfo getCustomerInfos(String sub) {
        CustomerInfo unlimitedCustInfo = getCustomerInfo(sub,
                config.getApiParameter("unlimitedCustomerAction"),
                config.getApiParameter("unlimitedDomain"));
        CustomerInfo pcmCustInfo = getPcmCustomerInfo(sub);

        Customer customer = unlimitedCustInfo.getResultData().getCustomer();
        AggregatedSyncState aggregatedSyncState = syncStateService
                .getSyncStates(customer.getCustomersId(), customer.getIdentityVerificationToken());

        return new AggregatedCustomerInfo(unlimitedCustInfo, pcmCustInfo, aggregatedSyncState);
    }

    public CustomerInfo getPcmCustomerInfo(String sub) {
        return getCustomerInfo(sub,
                    config.getApiParameter("pcmCustomerAction"),
                    config.getApiParameter("pcmDomain"));
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
                String.format(config.getApiParameter("customerInfoDefaultParameters"), sub, action, domain),
                headers);
    }
}
