package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.customerinfo.SubUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.lang.String.format;
import static java.net.URLEncoder.encode;

@Component
public class SubUserService {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    public SubUserInfo create(String customerId, String name, String token)
            throws UnsupportedEncodingException {
        String url = applicationConfiguration.getProperty("edt.api.customerInfo");
        String parameters = format(applicationConfiguration.getApiParameter("createCustomerParameters"),
                token, encode(name, "UTF-8"), customerId);

        HttpEntity<String> createSubUserBody = getStringHttpEntity(parameters);
        return postToEdt(createSubUserBody, url, SubUserInfo.class);
    }

    public SubUserInfo update(String customerId, String appUserId, String name, String token)
            throws UnsupportedEncodingException {
        String url = applicationConfiguration.getProperty("edt.api.customerInfo");
        String parameters = format(applicationConfiguration.getApiParameter("updateCustomerParameters"),
                token, encode(name, "UTF-8"), customerId + "_" + appUserId, customerId);

        HttpEntity<String> updateCustomerParameters = getStringHttpEntity(parameters);
        return postToEdt(updateCustomerParameters, url, SubUserInfo.class);

    }

    public SubUserInfo delete(String customerId, String appUserId, String token) {
        String url = applicationConfiguration.getProperty("edt.api.customerInfo");
        String parameters = format(applicationConfiguration.getApiParameter("deleteCustomerParameters"),
                token, customerId + "_" + appUserId, customerId);

        HttpEntity<String> updateCustomerParameters = getStringHttpEntity(parameters);
        return postToEdt(updateCustomerParameters, url, SubUserInfo.class);
    }

    private HttpEntity<String> getStringHttpEntity(String parameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(parameters, headers);
    }
}