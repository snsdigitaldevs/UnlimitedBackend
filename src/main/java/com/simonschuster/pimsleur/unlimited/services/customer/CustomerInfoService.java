package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.customerinfo.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Component
public class CustomerInfoService {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    public CustomerInfo update(String customerId, String appUserId, String name, String token) {
        String url = String.format(applicationConfiguration.getProperty("edt.api.customerInfo"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String parameters = String.format(applicationConfiguration.getApiParameter("updateCustomerParameters"),
                token,
                name,
                appUserId,
                customerId);
        HttpEntity<String> updateCustomerParameters = new HttpEntity<>(
                parameters,
                headers);
        return postToEdt(updateCustomerParameters, url, CustomerInfo.class);

    }

    public CustomerInfo create(String customerId, String name, String token) {
        String url = String.format(applicationConfiguration.getProperty("edt.api.customerInfo"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String parameters = String.format(applicationConfiguration.getApiParameter("createCustomerParameters"),
                token,
                name,
                customerId);
        HttpEntity<String> updateCustomerParameters = new HttpEntity<>(
                parameters,
                headers);
        return postToEdt(updateCustomerParameters, url, CustomerInfo.class);
    }

    public CustomerInfo delete(String customerId, String appUserId, String token) {
        String url = String.format(applicationConfiguration.getProperty("edt.api.customerInfo"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String parameters = String.format(applicationConfiguration.getApiParameter("deleteCustomerParameters"),
                token,
                appUserId,
                customerId);
        HttpEntity<String> updateCustomerParameters = new HttpEntity<>(
                parameters,
                headers);
        return postToEdt(updateCustomerParameters, url, CustomerInfo.class);
    }
}
