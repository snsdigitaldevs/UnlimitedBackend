package com.simonschuster.pimsleur.unlimited.services.sendEmailPermission;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.sendEmailPermission.EmailPermissionDto;
import com.simonschuster.pimsleur.unlimited.data.edt.sendEmailPermission.SendEmailPermissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class SendEmailPermissionService {
    @Autowired
    ApplicationConfiguration config;

    public HttpStatus setSendEmailPermission(EmailPermissionDto emailPermissionDto) {
        Integer allowSendEmail = emailPermissionDto.getAllowSendEmail() ? 1 : 0;
        String registrantId = emailPermissionDto.getRegistrantId();

        String requestUrl = config.getProperty("edt.api.allowSendEmail.url");
        String requestParams = config.getProperty("edt.api.allowSendEmail.parameters");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        SendEmailPermissionDto sendEmailPermissionDto = postToEdt(
                new HttpEntity<>(
                        String.format(requestParams, registrantId, allowSendEmail, allowSendEmail),
                        headers),
                requestUrl,
                SendEmailPermissionDto.class);

        return sendEmailPermissionDto.getResult_code().equals("1") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }
}
