package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.ActivateDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.activate.Deactivate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class ActivateService {
    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    public ActivateDTO active(String registrantId, String identityVerificationToken, List<String> isbns) {
        return null;
    }

    public void deactivate(String customerId, String registrantId) {
        String url = applicationConfiguration.getProperty("edt.api.activate.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> body = new HttpEntity<>(String.format(
                applicationConfiguration.getProperty("edt.api.activate.parameters.deactivate"),
                registrantId, customerId), headers);
        Deactivate deactivateResponse = postToEdt(body, url, Deactivate.class);
        if (!deactivateResponse.getResultCode().equals(1)) {
            throw new PimsleurException("PU deactivate failed!");
        }
    }
}
