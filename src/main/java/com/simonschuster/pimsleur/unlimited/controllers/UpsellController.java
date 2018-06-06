package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.promotions.UpsellDto;
import com.simonschuster.pimsleur.unlimited.services.promotions.UpsellService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UpsellController {

    @Autowired
    private UpsellService upsellService;

    @ApiOperation(value = "Upsell info for user's current learning course")
    @RequestMapping(value = "/product/{productCode}/upsell", method = RequestMethod.GET)
    public UpsellDto getUpsellInfo(@PathVariable("productCode") String isbn,
                                   @RequestParam(value = "sub") String sub) {
        return upsellService.getUpsellInfoFor(isbn, sub);
    }
}
