package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.availableProducts.AvailableProductsDto;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.IsbnNameDescription;
import com.simonschuster.pimsleur.unlimited.services.availableProducts.AvailableProductsService;
import com.simonschuster.pimsleur.unlimited.services.promotions.IsbnNameDescriptionService;
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

    @Autowired
    private IsbnNameDescriptionService isbnNameDescriptionService;

    @ApiOperation(value = "All products a customer can choose to learn(includes purchased and free)")
    @RequestMapping(value = "/availableProducts", method = RequestMethod.GET)
    public AvailableProductsDto getAvailableProducts(@RequestParam(value = "sub", required = false) String sub,
                                                     @RequestParam(value = "email", required = false) String email,
                                                     @RequestParam(value = "storeDomain", required = false) String storeDomain) {
        AvailableProductsDto availableProducts = availableProductsService.getAvailableProducts(sub, email, storeDomain);
        availableProducts.getPurchasedProducts().forEach(item -> {
            String productCode = item.getProductCode();
            IsbnNameDescription withOtherFormatAs = isbnNameDescriptionService.findISBNWithOtherFormatAs(productCode);
            if(withOtherFormatAs != null){
                item.setProductCode(withOtherFormatAs.getISBN());
            }
        });
        return availableProducts;
    }

}
