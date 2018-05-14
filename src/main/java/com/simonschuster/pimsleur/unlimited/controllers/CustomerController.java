package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.services.customer.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    @Autowired
    UserInfoService userInfoService;

    @PutMapping(value = "customer/{customerId}/appUserIds/{appUserId}")
    public void updateUserInfo(@PathVariable String customerId,
                               @PathVariable String appUserId,
                               @RequestParam String name) {
        userInfoService.update(customerId, appUserId, name);
    }

    @PostMapping(value = "customer/{customerId}")
    public void createUserInfo(@PathVariable String customerId) {

    }

    @DeleteMapping(value = "customer/{customerId}/appUserIds/{appUserIds}")
    public void deleteUserInfo(@PathVariable String customerId) {

    }
}
