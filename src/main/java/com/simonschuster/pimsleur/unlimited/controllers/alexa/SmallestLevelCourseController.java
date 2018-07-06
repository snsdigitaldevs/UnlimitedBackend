package com.simonschuster.pimsleur.unlimited.controllers.alexa;

import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.services.course.PUCourseInfoService;
import com.simonschuster.pimsleur.unlimited.services.course.PcmCourseInfoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SmallestLevelCourseController {
    @Autowired
    private PUCourseInfoService puCourseInfoService;
    @Autowired
    private PcmCourseInfoService pcmCourseInfoService;

    @ApiOperation(value = "Get one of the smallest level in purchased courses for Alexa",
            notes = "")

    @RequestMapping(value = "alexa/productInfoWithSmallestLevel", method = RequestMethod.GET)
    public Course getProductInfoWithSmallestLevel(
            @RequestParam(value = "sub") String sub,
            @RequestParam(value = "purchasedPUProductCodes", required = false) String purchasedPUProductCodes,
            @RequestParam(value = "purchasedPCMProductCodes", required = false) String purchasedPCMProductCodes)
    throws IOException {
        List<Course> purchasedCourses = new ArrayList();

        List<Course> purchasedPUCourses = getProductCodeList(purchasedPUProductCodes).stream()
                .map(purchasedPUProductCode ->
                    puCourseInfoService.getPuProductInfo(purchasedPUProductCode).toDto())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<Course> purchasedPCMCourses = getProductCodeList(purchasedPCMProductCodes).stream()
                .map(purchasedPCMProductCode ->
                    pcmCourseInfoService.getCourses(purchasedPCMProductCode, sub))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        purchasedCourses.addAll(purchasedPUCourses);
        purchasedCourses.addAll(purchasedPCMCourses);

        return purchasedCourses.stream()
                .sorted((course1, course2) -> course1.getLevel().compareTo(course2.getLevel())).findFirst().get();
    }

    private List<String> getProductCodeList(@RequestParam(value = "purchasedPUProductCodes") String purchasedPUProductCodes) {
        if (StringUtils.isEmpty(purchasedPUProductCodes)){
            return new ArrayList<>();
        }
        String[] purchasedPUProductCodesArray = purchasedPUProductCodes.split(",");
        return Arrays.stream(purchasedPUProductCodesArray)
                .map(String::trim)
                .map(code -> code.replace("\"", ""))
                .map(code -> code.replace("\\", ""))
                .collect(Collectors.toList());
    }
}
