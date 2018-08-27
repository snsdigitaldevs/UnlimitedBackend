package com.simonschuster.pimsleur.unlimited.services.customer;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.ActivateDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.ActivateResultDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.activate.Activate;
import com.simonschuster.pimsleur.unlimited.data.edt.CodeOnlyResponseEDT;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
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
    @Autowired
    private AppIdService appIdService;

    public ActivateDTO active(String registrantId, String registrantName, String identityVerificationToken, List<String> isbns, String storeDomain) {
        String url = applicationConfiguration.getProperty("edt.api.activate.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new ActivateDTO(isbns.stream()
                // Do not use parallelStream because updating data in same time is note supported by EDT now.
                .map(isbn -> activateToEdt(registrantId, registrantName, identityVerificationToken, url, headers, isbn, storeDomain))
                .collect(Collectors.toList()));
    }

    private ActivateResultDTO activateToEdt(String registrantId,
                                            String registrantName,
                                            String identityVerificationToken, String url, HttpHeaders headers, String isbn, String storeDomain) {
        String appId = appIdService.getAppId(storeDomain);
        String format = String.format(
                applicationConfiguration.getProperty("edt.api.activate.parameters.activate"),
                identityVerificationToken, registrantId, isbn, appId, registrantName
        );
        if(registrantName == null || registrantName.equals("")){
            int index = format.indexOf("&nfua=");
            format = format.substring(0, index);
        }

        HttpEntity<String> entity = new HttpEntity<>(format, headers);
        Activate activateResponse = postToEdt(entity, url, Activate.class);
        Integer resultCode = activateResponse.getResultCode();
        Boolean isActivated = resultCode.equals(1);
        ActivateResultDTO activateDTO = new ActivateResultDTO(isbn, isActivated);
        if (isActivated) {
            activateDTO.setActivatedTime(activateResponse.getResultData().getActivation().getActivationCountMobile() +
                    activateResponse.getResultData().getActivation().getActivationCountDesktop());
        } else {
            //-7010 means RESULT_ACTIVATION_USE_LIMIT_REACHED
            activateDTO.setActivatedTime(resultCode.equals(-7010) ? 4 : -1);
        }
        return activateDTO;
    }

    public void deactivate(String customerId, String registrantId, String storeDomain) {
        String url = applicationConfiguration.getProperty("edt.api.activate.url");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(storeDomain);
        HttpEntity<String> entity = new HttpEntity<>(String.format(
                applicationConfiguration.getProperty("edt.api.activate.parameters.deactivate"),
                registrantId, customerId, appId), headers);
        CodeOnlyResponseEDT deactivateResponse = postToEdt(entity, url, CodeOnlyResponseEDT.class);
        if (!deactivateResponse.getResultCode().equals(1)) {
            throw new PimsleurException("PU deactivate failed!");
        }
    }
}
