package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.aop.annotation.LogCostTime;
import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.IntentionToBuyBody;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptBody;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.VerifyReceiptDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.promotions.FormatMapping;
import com.simonschuster.pimsleur.unlimited.services.course.PUCourseInfoService;
import com.simonschuster.pimsleur.unlimited.services.course.PcmCourseInfoService;
import com.simonschuster.pimsleur.unlimited.services.course.PcmFreeCourseService;
import com.simonschuster.pimsleur.unlimited.services.purchase.IntentionToBuyService;
import com.simonschuster.pimsleur.unlimited.services.purchase.VerifyReceiptService;
import com.simonschuster.pimsleur.unlimited.services.promotions.FormatMappingService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.simonschuster.pimsleur.unlimited.utils.DataConverterUtil.distinctByKey;
import static java.util.stream.Collectors.toList;

@RestController
public class ProductController {

    @Autowired
    private PUCourseInfoService puCourseInfoService;
    @Autowired
    private PcmCourseInfoService pcmCourseInfoService;
    @Autowired
    private PcmFreeCourseService pcmFreeCourseService;

    @Autowired
    private IntentionToBuyService intentionToBuyService;

    @Autowired
    private VerifyReceiptService verifyReceiptService;

    @Autowired
    private FormatMappingService formatMappingService;

    @ApiOperation(value = "Get product info by ISBN. Product info includes audio links, images, culture contents, etc.")
    @RequestMapping(value = "/productInfo", method = RequestMethod.GET)
    public List<Course> getProductInfo(@RequestParam("isPUProductCode") boolean isPUProductCode,
                                       @RequestParam(name = "isFree", required = false) boolean isFree,
                                       @RequestParam(value = "productCode") String productCode,
                                       @RequestParam(value = "sub") String sub,
                                       @RequestParam(value = "email", required = false) String email,
                                       @RequestParam(value = "storeDomain", required = false) String storeDomain) {
        validateProductCode(productCode);
        List<Course> courseList = getProductInfos(isPUProductCode, isFree, productCode, sub, email, storeDomain)
                .stream()
                .filter(distinctByKey(Course::getProductCode))
                // remove duplicate by isbn, sometimes there could be same isbn show up more than once
                // because user could have bought a product and a subscription that covers this product
                .map(item -> {
                    FormatMapping formatMappingFor = formatMappingService.findFormatMappingFor(item.getProductCode());
                    if (formatMappingFor != null) {
                        item.setCourseName(formatMappingFor.getCourseName());
                        item.setSimpleCourseName(formatMappingFor.getSimpleCourseName());
                    }
                    return item;
                })
                .collect(toList());
        return courseList;
    }

    private List<Course> getProductInfos(boolean isPUProductCode,
                                         boolean isFree,
                                         String productCode,
                                         String sub,
                                         String email, String storeDomain) {
        if (isFree && !isPUProductCode) {
            return pcmFreeCourseService.getPcmFreeCourseInfos(productCode, storeDomain);
        } else if (isPUProductCode) {
            return puCourseInfoService.getPuProductInfo(productCode, storeDomain).toDto();
        } else {
            return pcmCourseInfoService.getCourses(productCode, sub, email, storeDomain);
        }
    }

    @ApiOperation(value = "Call this api before paying through apple or google, so that EDT knows a user has the intention to purchase.")
    @RequestMapping(value = "/account/{customerId}/product/{isbn}/intentionToBuy", method = RequestMethod.POST)
    public void intentionToBuy(@PathVariable("customerId") String customerId,
                               @PathVariable("isbn") String isbn,
                               @RequestBody IntentionToBuyBody intentionToBuyBody) {
        intentionToBuyService.intentionToBuy(customerId, isbn, intentionToBuyBody.getStoreDomain());
    }

    @ApiOperation(value = "Send transaction result and receipt to EDT, so that EDT can create order for user.")
    @RequestMapping(value = "/account/{customerId}/receipt", method = RequestMethod.POST)
    @LogCostTime
    public VerifyReceiptDTO verifyReceipt(@PathVariable("customerId") String customerId,
                                          @RequestBody VerifyReceiptBody verifyReceiptBody)
            throws UnsupportedEncodingException {
        if (StringUtils.isBlank(verifyReceiptBody.getReceipt())) {
            throw new ParamInvalidException("receipt can not be blank");
        }
        return verifyReceiptService.verifyReceipt(verifyReceiptBody, customerId);
    }

    private void validateProductCode(@RequestParam(value = "productCode") String productCode) {
        if (productCode == null || productCode.isEmpty()) {
            throw new ParamInvalidException("Product code missing!");
        }
    }
}
