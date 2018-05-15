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
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwaW1zbGV1cmRpZ2l0YWwuY29tIiwiYXVkIjoiY291cnNlbWFuYWdlciIsImlhdCI6MTUyNjM0NzI0OSwibmJmIjoxNTI2MzQ3MjQ5LCJjb250ZXh0Ijp7ImN1c3RvbWVyc0lkIjoxNjIyNTJ9fQ.hwUbqvCGwU9bieXT7QkGM89OcKPcCCXINI2VZ3vuASI",
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
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwaW1zbGV1cmRpZ2l0YWwuY29tIiwiYXVkIjoiY291cnNlbWFuYWdlciIsImlhdCI6MTUyNjM0NzI0OSwibmJmIjoxNTI2MzQ3MjQ5LCJjb250ZXh0Ijp7ImN1c3RvbWVyc0lkIjoxNjIyNTJ9fQ.hwUbqvCGwU9bieXT7QkGM89OcKPcCCXINI2VZ3vuASI",
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
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwaW1zbGV1cmRpZ2l0YWwuY29tIiwiYXVkIjoiY291cnNlbWFuYWdlciIsImlhdCI6MTUyNjM0NzI0OSwibmJmIjoxNTI2MzQ3MjQ5LCJjb250ZXh0Ijp7ImN1c3RvbWVyc0lkIjoxNjIyNTJ9fQ.hwUbqvCGwU9bieXT7QkGM89OcKPcCCXINI2VZ3vuASI",
                appUserId,
                customerId);
        HttpEntity<String> updateCustomerParameters = new HttpEntity<>(
                parameters,
                headers);
        return postToEdt(updateCustomerParameters, url, CustomerInfo.class);
    }
}
