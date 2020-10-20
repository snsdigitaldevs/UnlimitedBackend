package com.simonschuster.pimsleur.unlimited.controllers;

import static com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode.NO_RESULT;
import static com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode.RESULT_GENERAL_ERROR;
import static com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode.RESULT_OK;
import static com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode.RESULT_USER_ID_ALREADY_EXISTS;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.SubUserDto;
import com.simonschuster.pimsleur.unlimited.data.edt.customerinfo.SubUserInfo;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import com.simonschuster.pimsleur.unlimited.services.customer.SubUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class SubUserController {

    @Autowired
    SubUserService subUserService;

    @Autowired
    EDTCustomerInfoService edtCustomerInfoService;

    @ApiOperation(value = "Create new sub user")
    @PostMapping(value = "customers/{customerId}/appUsers")
    public SubUserDto createUserInfo(@PathVariable String customerId,
                                     @ApiParam(value = "you can find identityVerificationToken in customerInfo api")
                                     @RequestParam String token,
                                     @ApiParam(value = "only need name of the new sub user")
                                     @RequestBody SubUserDto userDto,
                                     @RequestParam(value = "storeDomain", required = false) String storeDomain) throws UnsupportedEncodingException {
        SubUserInfo subUserInfo = subUserService.create(customerId, userDto.getName(), token, storeDomain);
        checkResultCode(subUserInfo);
        return subUserInfo.toDto(userDto.getName());
    }

    @ApiOperation(value = "Update sub user, you can only change sub user's name")
    @PutMapping(value = "customers/{customerId}/appUsers/{appUserId}")
    public SubUserDto updateUserInfo(@PathVariable String customerId,
                                     @PathVariable String appUserId,
                                     @ApiParam(value = "you can find identityVerificationToken in customerInfo api")
                                     @RequestParam String token,
                                     @ApiParam(value = "only need the new name of sub user")
                                     @RequestBody SubUserDto userDto,
                                     @RequestParam(value = "storeDomain", required = false) String storeDomain) throws UnsupportedEncodingException {
        SubUserInfo subUserInfo = subUserService.update(customerId, appUserId, userDto.getName(), token, storeDomain);
        checkResultCode(subUserInfo);
        return subUserInfo.toDto(userDto.getName(), appUserId);
    }

    @ApiOperation(value = "Delete sub user")
    @DeleteMapping(value = "customers/{customerId}/appUsers/{appUserId}")
    public SubUserDto deleteUserInfo(@PathVariable String customerId,
                                     @ApiParam(value = "you can find identityVerificationToken in customerInfo api")
                                     @RequestParam String token,
                                     @PathVariable String appUserId,
                                     @ApiParam(value = "only need the name of sub user")
                                     @RequestBody SubUserDto userDto,
                                     @RequestParam(value = "storeDomain", required = false) String storeDomain) {
        SubUserInfo subUserInfo = subUserService.delete(customerId, appUserId, token, storeDomain);
        checkResultCode(subUserInfo);
        return subUserInfo.toDto(userDto.getName(), appUserId);
    }

    private void checkResultCode(SubUserInfo subUserInfo) {
        switch (subUserInfo.getResult_code()) {
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
