package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp.SignUpBodyDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp.SignUpDTO;
import com.simonschuster.pimsleur.unlimited.services.customer.SignUpService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    @RequestMapping(
            value = "/signUp",
            method = RequestMethod.POST,
            consumes = "application/json"
    )

    @ApiOperation(value = "Sign up a new account.")
    public SignUpDTO sign(@RequestBody SignUpBodyDTO signUpBodyDTO) {
        return signUpService.signUp(signUpBodyDTO);
    }
}
