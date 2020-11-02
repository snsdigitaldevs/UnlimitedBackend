package com.simonschuster.pimsleur.unlimited.controllers;

import static com.simonschuster.pimsleur.unlimited.utils.DataConverterUtil.distinctByKey;
import static java.util.Comparator.comparing;

import com.simonschuster.pimsleur.unlimited.data.dto.InAppProduct;
import com.simonschuster.pimsleur.unlimited.data.dto.availableProducts.AvailableProductsDto;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.FormatMapping;
import com.simonschuster.pimsleur.unlimited.services.availableProducts.AvailableProductsService;
import com.simonschuster.pimsleur.unlimited.services.promotions.FormatMappingService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
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
    private FormatMappingService formatMappingService;

    @ApiOperation(value = "All products a customer can choose to learn(includes purchased and free)")
    @RequestMapping(value = "/availableProducts", method = RequestMethod.GET)
    public AvailableProductsDto getAvailableProducts(@RequestParam(value = "sub", required = false) String sub,
                                                     @RequestParam(value = "email", required = false) String email,
                                                     @RequestParam(value = "storeDomain", required = false) String storeDomain) {
        AvailableProductsDto availableProducts = availableProductsService.getAvailableProducts(sub, email, storeDomain);
        availableProducts
                .setPurchasedProducts(availableProducts.getPurchasedProducts().stream().peek(item -> {
                    String productCode = item.getProductCode();
                    FormatMapping withOtherFormatAs = formatMappingService
                            .findISBNWithOtherFormatAs(productCode);
                    if (withOtherFormatAs != null) {
                        item.setProductCode(withOtherFormatAs.getISBN());
                    }
                    updateCourseName(item);
                }).sorted(comparing(AvailableProductDto::getCourseName))
                        .filter(distinctByKey(AvailableProductDto::getProductCode))
                        .collect(Collectors.toList()));
        availableProducts.getFreeProducts().forEach(this::updateCourseName);
        availableProducts.getFreeProducts().sort(comparing(AvailableProductDto::getCourseName));
        return availableProducts;
    }

    private void updateCourseName(AvailableProductDto item) {
        FormatMapping formatMappingFor = formatMappingService.findFormatMappingFor(item.getProductCode());
        item.setCourseName(formatMappingFor != null ? formatMappingFor.getCourseName() : item.getLanguageName());
    }


    @ApiOperation(value = "get all isbn that purchase in app")
    @RequestMapping(value = "/purchaseInApp", method = RequestMethod.GET)
    public List<InAppProduct> purchaseInApp(@RequestParam(value = "sub", required = false) String sub,
                                            @RequestParam(value = "email", required = false) String email,
                                            @RequestParam(value = "storeDomain", required = false) String storeDomain) {
        return availableProductsService.purchaseInApp(sub, email, storeDomain);
    }

}
