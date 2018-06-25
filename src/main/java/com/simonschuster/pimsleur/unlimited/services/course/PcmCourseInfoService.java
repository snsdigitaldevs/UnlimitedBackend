package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.*;
import com.simonschuster.pimsleur.unlimited.data.edt.productinfo.*;
import com.simonschuster.pimsleur.unlimited.services.customer.EDTCustomerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.simonschuster.pimsleur.unlimited.data.edt.productinfo.AggregatedProductInfo.createInstanceForPcm;

@Service
public class PcmCourseInfoService {
    @Autowired
    private EDTCustomerInfoService customerInfoService;
    @Autowired
    private PcmReadingsService pcmReadingsService;
    @Autowired
    private PcmLessonInfoService pcmLessonInfoService;

    private static final Logger logger = LoggerFactory.getLogger(PUCourseInfoService.class);

    public List<Course> getCourses(String productCode, String sub) {
        PcmProduct pcmProductInfo = getPcmProductInfo(sub);
        List<Course> pcmAudioInfo = pcmLessonInfoService.getPcmLessonInfo(productCode, pcmProductInfo);

        List<Course> courses = createInstanceForPcm(pcmProductInfo, pcmAudioInfo).toDto();
        pcmReadingsService.addReadingsToCourses(courses, pcmProductInfo);
        return courses;
    }

    private PcmProduct getPcmProductInfo(String sub) {
        try {
            Customer customer = customerInfoService.getPcmCustomerInfo(sub,"")
                    .getResultData().getCustomer();

            PcmProduct pcmProduct = new PcmProduct();
            pcmProduct.setCustomersId(customer.getCustomersId());
            pcmProduct.setCustomerToken(customer.getIdentityVerificationToken());
            pcmProduct.setOrdersProducts(customer.getAllOrdersProducts());
            return pcmProduct;
        } catch (Exception exception) {
            logger.error("Exception occurred when get product info with PCM product code.");
            exception.printStackTrace();
            throw new PimsleurException("Exception occurred when get product info with PCM product code.");
        }
    }

}
