package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCourseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductInfoController {

    @Autowired
    private EDTCourseInfoService edtCourseInfoService;

    @RequestMapping(value = "/productInfo", method = RequestMethod.GET)
    public List<Course> getProductInfo(@RequestParam("isPUProductCode") boolean isPUProductCode,
                                      @RequestParam(value = "productCode") String productCode,
                                      @RequestParam(value = "sub") String sub)
            {
//        if (productCode == null || productCode.isEmpty()) {
//            throw new Exception("Product code missing!");
//        }
        return edtCourseInfoService.getCourseInfos(isPUProductCode, productCode, sub).toDto();
    }
}
