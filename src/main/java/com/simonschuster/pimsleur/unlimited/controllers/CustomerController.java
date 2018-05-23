package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.SubUserDto;
import com.simonschuster.pimsleur.unlimited.data.edt.customerinfo.CustomerInfo;
import com.simonschuster.pimsleur.unlimited.services.customer.CustomerInfoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.simonschuster.pimsleur.unlimited.utils.EdtResponseCode.*;

@RestController
public class CustomerController {

    @Autowired
    CustomerInfoService customerInfoService;

    @ApiOperation(value = "Create new sub user")
    @PostMapping(value = "customers/{customerId}/appUsers")
    public SubUserDto createUserInfo(@PathVariable String customerId,
                                     @ApiParam(value = "you can find identityVerificationToken in customerInfo api")
                                     @RequestParam String token,
                                     @ApiParam(value = "name of the new sub user")
                                     @RequestParam String name) {
        CustomerInfo customerInfo = customerInfoService.create(customerId, name, token);
        checkResultCode(customerInfo);
        return customerInfo.toDto(name);
    }

    @ApiOperation(value = "Update sub user, you can only change sub user's name")
    @PutMapping(value = "customers/{customerId}/appUsers/{appUserId}")
    public SubUserDto updateUserInfo(@PathVariable String customerId,
                                     @PathVariable String appUserId,
                                     @ApiParam(value = "you can find identityVerificationToken in customerInfo api")
                                     @RequestParam String token,
                                     @RequestParam String name) {
        CustomerInfo customerInfo = customerInfoService.update(customerId, appUserId, name, token);
        checkResultCode(customerInfo);
        return customerInfo.toDto(name, appUserId);
    }

    @ApiOperation(value = "Delete sub user")
    @DeleteMapping(value = "customers/{customerId}/appUsers/{appUserId}")
    public SubUserDto deleteUserInfo(@PathVariable String customerId,
                                     @ApiParam(value = "you can find identityVerificationToken in customerInfo api")
                                     @RequestParam String token,
                                     @PathVariable String appUserId,
                                     @RequestParam(defaultValue = "") String name) {
        CustomerInfo customerInfo = customerInfoService.delete(customerId, appUserId, token);
        checkResultCode(customerInfo);
        return customerInfo.toDto(name, appUserId);
    }

    private void checkResultCode(CustomerInfo customerInfo) {
        switch (customerInfo.getResult_code()) {
            case RESULT_OK:
                break;
            case NO_RESULT:
            case RESULT_GENERAL_ERROR:
                throw new ParamInvalidException("Invalid parameters");
            case RESULT_USER_ID_ALREADY_EXISTS:
                throw new ParamInvalidException("Duplicated Name");
        }
    }
}
