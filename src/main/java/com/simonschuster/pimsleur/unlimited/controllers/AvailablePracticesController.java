package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//this api tells you what kind of practices are available for each lesson inside a course

@RestController
public class AvailablePracticesController {
    @RequestMapping(value = "/product/{productCode}/availablePractices", method = RequestMethod.GET)
    public AvailablePractices getAvailablePractices(@PathVariable("productCode") String productCode) {
     return null;
    }
}
