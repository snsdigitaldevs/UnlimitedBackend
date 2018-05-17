package com.simonschuster.pimsleur.unlimited.controllers;

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
}
