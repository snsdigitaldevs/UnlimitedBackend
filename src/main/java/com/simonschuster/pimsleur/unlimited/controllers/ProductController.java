package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.IntentionToBuyBody;
import com.simonschuster.pimsleur.unlimited.services.course.EDTCourseInfoService;
import com.simonschuster.pimsleur.unlimited.services.course.IntentionToBuyService;
import com.simonschuster.pimsleur.unlimited.services.course.PcmFreeCourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private IntentionToBuyService intentionToBuyService;

    @Autowired
    private EDTCourseInfoService edtCourseInfoService;
    @Autowired
    private PcmFreeCourseService pcmFreeCourseService;

    @ApiOperation(value = "Get product info by ISBN. Product info includes audio links, images, culture contents, etc.")
    @RequestMapping(value = "/productInfo", method = RequestMethod.GET)
    public List<Course> getProductInfo(@RequestParam("isPUProductCode") boolean isPUProductCode,
                                       @RequestParam(name = "isFree", required = false) boolean isFree,
                                       @RequestParam(value = "productCode") String productCode,
                                       @RequestParam(value = "sub") String sub) {
        if (productCode == null || productCode.isEmpty()) {
            throw new ParamInvalidException("Product code missing!");
        }
        if (isFree && !isPUProductCode) {
            return pcmFreeCourseService.getPcmFreeCourseInfos(productCode);
        }
        return edtCourseInfoService.getCourseInfos(isPUProductCode, productCode, sub).toDto();
    }

    @ApiOperation(value = "Call this api before paying through apple or google, so that EDT knows a user has the intention to purchase.")
    @RequestMapping(value = "/account/{customerId}/product/{isbn}/intentionToBuy", method = RequestMethod.POST)
    public void intentionToBuy(@PathVariable("customerId") String customerId,
                               @PathVariable("isbn") String isbn,
                               @RequestBody IntentionToBuyBody intentionToBuyBody) {
        intentionToBuyService.intentionToBuy(customerId, isbn, intentionToBuyBody.getStoreDomain());
    }
}
