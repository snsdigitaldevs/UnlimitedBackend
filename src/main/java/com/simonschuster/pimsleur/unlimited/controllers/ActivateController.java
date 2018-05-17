package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.ActivateBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.ActivateDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.active.DeactivateBodyDTO;
import com.simonschuster.pimsleur.unlimited.services.customer.ActivateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ActivateController {
    @Autowired
    private ActivateService activateService;

    @RequestMapping(value = "/account/{customerId}/allProducts/activation", method = RequestMethod.DELETE)
    public void deactivate(@PathVariable("customerId") String customerId,
                           @RequestBody DeactivateBodyDTO deactivateBodyDTO) {
        activateService.deactivate(customerId, deactivateBodyDTO.getRegistrantId());
    }

    @RequestMapping(value = "/registrant/{registrantId}/products/activation", method = RequestMethod.POST)
    public ActivateDTO activate(@PathVariable("registrantId") String registrantId,
                                @RequestBody ActivateBodyDTO activateBodyDTO) {
        return activateService.active(registrantId, activateBodyDTO.getIdentityVerificationToken(), activateBodyDTO.getIsbns());
    }
}
