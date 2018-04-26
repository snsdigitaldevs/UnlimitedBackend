package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.services.practices.AvailablePracticesService;
import com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

//this api tells you what kind of practices are available for each lesson inside a course

@RestController
public class AvailablePracticesController {

    @Autowired
    private AvailablePracticesService availablePracticesService;

    @RequestMapping(value = "/product/{productCode}/availablePractices", method = RequestMethod.GET)
    public AvailablePractices getAvailablePractices(@PathVariable("productCode") String productCode) throws IOException {
        return UnlimitedPracticeUtil
                .getAvailablePractices(availablePracticesService.getPracticeCsvLocations(productCode));
    }
}
