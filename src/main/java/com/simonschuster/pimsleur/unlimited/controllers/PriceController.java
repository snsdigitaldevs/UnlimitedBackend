package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.price.PriceInfoDTO;
import com.simonschuster.pimsleur.unlimited.services.price.PriceService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {

    @Autowired
    private PriceService priceService;

    @ApiOperation(value = "Get price from MG2/Demandware")
    @GetMapping("/price")
    public PriceInfoDTO getPrice(@RequestParam("isSubscription") Boolean isSubscription,
                                 @RequestParam("productCode") String productCode) {
        return isSubscription ?
                priceService.getMG2ShopInfo(productCode) : priceService.getDemandwareShopInfo(productCode);
    }
}
