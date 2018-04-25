package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.CustomerInfoDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

//this api tells you what kind of practices are available for each lesson inside a course

@RestController
public class AvailablePracticesController {
    @RequestMapping(value = "/product/{productCode}/availablePractices", method = RequestMethod.GET)
    public AvailablePractices getAvailablePractices(@PathVariable("productCode") String productCode) {
        return null;
    }
}
