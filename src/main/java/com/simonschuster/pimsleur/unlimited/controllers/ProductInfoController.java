package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCourseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ProductInfoController {

    @Autowired
    private EDTCourseInfoService edtCourseInfoService;

    @RequestMapping(value = "/productInfo", method = RequestMethod.GET)
    public Course getCustomerInfo(@RequestParam(value = "productCode") String productCode,
                                  @RequestParam(value = "sub") String sub)
            throws IOException {
        return edtCourseInfoService.getCourseInfos(productCode, sub).toDto();
    }
}
