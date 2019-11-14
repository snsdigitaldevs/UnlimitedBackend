package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.constants.StoreDomainConstants;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.CustomerInfoDTO;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class CustomerInfoForAlexaController {

    @Autowired
    private EDTCustomerInfoService edtCustomerInfoService;

    @ApiOperation(value = "Get customer info for Alexa",
            notes = "Customer info includes isbns of purchased and expanded pcm and pu products, " +
                    "exclude pu free lessons in order, kitted product code is replaced with child product codes, " +
                    "activation information(total and used activation times), " +
                    "progress of both pcm and pu(pcm progress does not have sub user), " +
                    "sub users info, customerId, registrantId, identityVerificationToken " +
                    "and last save id of pcm and pu")

    @RequestMapping(value = "alexa/customerInfo", method = RequestMethod.GET)
    public CustomerInfoDTO getCustomerInfo(@RequestParam(value = "sub") String sub)
            throws IOException {
        //Kelly K will handle email missing for Alexa.
        return edtCustomerInfoService.getCustomerInfoDTO(sub, StoreDomainConstants.ALEXA_STORE_DOMAIN, "");
    }
}
