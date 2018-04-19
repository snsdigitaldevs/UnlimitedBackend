package com.simonschuster.pimsleur.unlimited.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerInfoController {
    @RequestMapping(value = "/customerInfo", method = RequestMethod.GET)
    public String getUser(@RequestParam(value = "sub") String sub) {
        return sub;
    }
}
