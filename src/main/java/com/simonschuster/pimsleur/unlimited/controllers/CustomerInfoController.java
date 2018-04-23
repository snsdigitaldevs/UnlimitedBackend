package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.CustomerInfoDTO;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CustomerInfoController {

    @Autowired
    private EDTCustomerInfoService edtCustomerInfoService;

    @RequestMapping(value = "/customerInfo", method = RequestMethod.GET)
    public CustomerInfoDTO getCustomerInfo(@RequestParam(value = "sub") String sub)
            throws IOException {
        return edtCustomerInfoService.getCustomerInfos(sub).toDto();
    }
}
