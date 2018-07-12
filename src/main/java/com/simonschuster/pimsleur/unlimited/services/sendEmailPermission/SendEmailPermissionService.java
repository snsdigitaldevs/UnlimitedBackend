package com.simonschuster.pimsleur.unlimited.services.sendEmailPermission;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.sendEmailPermission.EmailPermissionDto;
import com.simonschuster.pimsleur.unlimited.data.edt.sendEmailPermission.SendEmailPermissionDto;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class SendEmailPermissionService {
    @Autowired
    ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    public HttpEntity setSendEmailPermission(EmailPermissionDto emailPermissionDto) {
        Integer allowSendEmail = emailPermissionDto.getAllowSendEmail() ? 1 : 0;
        String registrantId = emailPermissionDto.getRegistrantId();

        String requestUrl = config.getProperty("edt.api.allowSendEmail.url");
        String requestParams = config.getProperty("edt.api.allowSendEmail.parameters");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(emailPermissionDto.getStoreDomain());
        SendEmailPermissionDto sendEmailPermissionDto = postToEdt(
                new HttpEntity<>(
                        String.format(requestParams, registrantId, allowSendEmail, allowSendEmail, appId),
                        headers),
                requestUrl, SendEmailPermissionDto.class);

        if (sendEmailPermissionDto.getResult_code().equals("1")) {
            return new ResponseEntity<>(OK);
        } else {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
