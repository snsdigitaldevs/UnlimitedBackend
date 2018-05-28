package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.availableProducts.AvailableProductsDto;
import com.simonschuster.pimsleur.unlimited.services.availableProducts.AvailableProductsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvailableProductsController {

    @Autowired
    private AvailableProductsService availableProductsService;

    @ApiOperation(value = "All products a customer can choose to learn(includes purchased and free)")
    @RequestMapping(value = "/availableProducts", method = RequestMethod.GET)
    public AvailableProductsDto getPuAvailablePractices(@RequestParam(value = "sub") String sub) {
        return availableProductsService.getAvailableProducts(sub);
    }

}
