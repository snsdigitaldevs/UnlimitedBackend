package com.simonschuster.pimsleur.unlimited.controllers;

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
        return customerInfoService.update(customerId, appUserId, name, token);
    }

    @PostMapping(value = "customer/{customerId}")
    public CustomerInfo createUserInfo(@PathVariable String customerId,
                                       @RequestParam String token,
                                       @RequestParam String name) {
        return customerInfoService.create(customerId, name, token);
    }

    @DeleteMapping(value = "customer/{customerId}/appUserIds/{appUserId}")
    public CustomerInfo deleteUserInfo(@PathVariable String customerId,
                                       @RequestParam String token,
                                       @PathVariable String appUserId) {
        return customerInfoService.delete(customerId, appUserId, token);
    }
}
