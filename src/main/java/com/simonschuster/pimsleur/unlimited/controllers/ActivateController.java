package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.ActivateBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.ActivateDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.DeactivateBodyDTO;
import com.simonschuster.pimsleur.unlimited.services.customer.ActivateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ActivateController {
    @Autowired
    private ActivateService activateService;

    @ApiOperation(value = "Deactivation",
            notes = "deactivate all pu products of an account")
    @RequestMapping(value = "/account/{customerId}/allProducts/activation", method = RequestMethod.DELETE)
    public void deactivate(
            @ApiParam(value = "you can find customer id in customerInfo api")
            @PathVariable("customerId") String customerId,

            @ApiParam(value = "you can find registrant id in customerInfo api")
            @RequestBody DeactivateBodyDTO deactivateBodyDTO) {
        activateService.deactivate(customerId, deactivateBodyDTO.getRegistrantId());
    }

    @ApiOperation(value = "Activation",
            notes = "activate pu products given in the isbns array")
    @RequestMapping(value = "/registrant/{registrantId}/products/activation", method = RequestMethod.POST)
    public ActivateDTO activate(
            @ApiParam(value = "you can find customer id in customerInfo api")
            @PathVariable("registrantId") String registrantId,

            @ApiParam(value = "you can find identityVerificationToken in customerInfo api")
            @RequestBody ActivateBodyDTO activateBodyDTO) {
        return activateService.active(registrantId, activateBodyDTO.getIdentityVerificationToken(), activateBodyDTO.getIsbns());
    }
}
