package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.ActivateDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.ActivateResultDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.activate.Activate;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.activate.Deactivate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class ActivateService {
    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    public ActivateDTO active(String registrantId, String identityVerificationToken, List<String> isbns) {
        String url = applicationConfiguration.getProperty("edt.api.activate.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new ActivateDTO(isbns.parallelStream()
                .map(isbn -> {
                    HttpEntity<String> entity = new HttpEntity<>(String.format(
                            applicationConfiguration.getProperty("edt.api.activate.parameters.activate"),
                            identityVerificationToken, registrantId, isbn
                    ), headers);
                    Activate activateResponse = postToEdt(entity, url, Activate.class);
                    Integer resultCode = activateResponse.getResultCode();
                    Boolean isActivated = resultCode.equals(1);
                    ActivateResultDTO activateDTO = new ActivateResultDTO(isbn, isActivated);
                    if (isActivated) {
                        activateDTO.setActivatedTime(activateResponse.getResultData().getActivation().getActivationCountMobile());
                    } else {
                        activateDTO.setActivatedTime(resultCode.equals(-7010) ? 4 : -1);
                    }
                    return activateDTO;
                })
                .collect(Collectors.toList()));
    }

    public void deactivate(String customerId, String registrantId) {
        String url = applicationConfiguration.getProperty("edt.api.activate.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(String.format(
                applicationConfiguration.getProperty("edt.api.activate.parameters.deactivate"),
                registrantId, customerId), headers);
        Deactivate deactivateResponse = postToEdt(entity, url, Deactivate.class);
        if (!deactivateResponse.getResultCode().equals(1)) {
            throw new PimsleurException("PU deactivate failed!");
        }
    }
}