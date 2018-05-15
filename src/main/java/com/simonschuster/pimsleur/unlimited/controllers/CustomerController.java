package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.data.edt.customerinfo.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.services.customer.CustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    @Autowired
    CustomerInfoService customerInfoService;

    @PutMapping(value = "customer/{customerId}/appUserIds/{appUserId}")
    public CustomerInfo updateUserInfo(@PathVariable String customerId,
                                       @PathVariable String appUserId,
                                       @RequestParam String token,
                                       @RequestParam String name) {
        CustomerInfo customerInfo = customerInfoService.update(customerId, appUserId, name, token);
        checkResultCode(customerInfo);
        return customerInfo;
    }

    @PostMapping(value = "customer/{customerId}")
    public CustomerInfo createUserInfo(@PathVariable String customerId,
                                       @RequestParam String token,
                                       @RequestParam String name) {
        CustomerInfo customerInfo = customerInfoService.create(customerId, name, token);
        checkResultCode(customerInfo);
        return customerInfo;
    }

    @DeleteMapping(value = "customer/{customerId}/appUserIds/{appUserId}")
    public CustomerInfo deleteUserInfo(@PathVariable String customerId,
                                       @RequestParam String token,
                                       @PathVariable String appUserId) {
        CustomerInfo customerInfo = customerInfoService.delete(customerId, appUserId, token);
        checkResultCode(customerInfo);
        return customerInfo;
    }

    private void checkResultCode(CustomerInfo customerInfo) {
        if (customerInfo.getResult_code() != 1) {
            throw new ParamInvalidException("invalid parameters");
        }
    }
}
