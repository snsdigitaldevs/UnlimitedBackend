package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.availableProducts.AvailableProductsDto;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.CustomerInfoDTO;
import com.simonschuster.pimsleur.unlimited.services.availableProducts.AvailableProductsService;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.alexa.StoreDomainUtil.ALEXA_STORE_DOMAIN;

@RestController
public class CustomerInfoForAlexaController {

    @Autowired
    private EDTCustomerInfoService edtCustomerInfoService;

    @Autowired
    private AvailableProductsService availableProductsService;

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
//        AggregatedCustomerInfo customerInfo = edtCustomerInfoService.getCustomerInfos(sub, ALEXA_STORE_DOMAIN, "");
//        CustomerInfoDTO customerInfoDTO = customerInfo.toDto();
        CustomerInfoDTO customerInfoDTO = edtCustomerInfoService.getCustomerInfoDTO(sub, ALEXA_STORE_DOMAIN, "");
        customerInfoDTO.getProductActivations().forEach(activation -> { String productCode = activation.getProductCode();
            if(activation.getChildProductCodesString() != null){
                String childProductCodes = activation.getChildProductCodesString();
                String[] childProductCodeArray = childProductCodes.split(",");
                if(customerInfoDTO.getUnlimitedProductCodes().contains(productCode)){
                    customerInfoDTO.getUnlimitedProductCodes().addAll(Arrays.asList(childProductCodeArray));
                }
            }
        });

        AvailableProductsDto availableProducts = availableProductsService.getAvailableProducts(sub, "", ALEXA_STORE_DOMAIN);
        List<String> purchasedPCMProductCodes = availableProducts.getPurchasedProducts()
                .stream().filter(product -> product.isPuProduct() == false)
                .map(product -> product.getProductCode())
                .collect(Collectors.toList());
        customerInfoDTO.getPcmProductCodes().addAll(purchasedPCMProductCodes);
        return customerInfoDTO;
    }
}
