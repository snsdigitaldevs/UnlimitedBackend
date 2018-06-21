package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.sendEmailPermission.EmailPermissionDto;
import com.simonschuster.pimsleur.unlimited.services.sendEmailPermission.SendEmailPermissionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class SendEmailPermissionController {

    @Autowired
    SendEmailPermissionService sendEmailPermissionService;

    @ApiOperation(value = "Set if users allow backend send email to them", notes = "")
    @RequestMapping(value = "/acceptReceiveEmail", method = RequestMethod.POST)
    public HttpEntity setEmailPermission(@RequestBody EmailPermissionDto emailPermissionDto)
            throws IOException {
        return sendEmailPermissionService.setSendEmailPermission(emailPermissionDto);
    }
}
