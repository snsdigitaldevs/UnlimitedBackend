package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.services.practices.PcmAvailablePracticesService;
import com.simonschuster.pimsleur.unlimited.services.practices.PuAvailablePracticesService;
import com.simonschuster.pimsleur.unlimited.utils.PuFreeLessonISBNUtil;
import com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.simonschuster.pimsleur.unlimited.utils.PuFreeLessonISBNUtil.toNormalISBN;

//this api tells you what kind of practices are available for each lesson inside a course

@RestController
public class AvailablePracticesController {

    @Autowired
    private PuAvailablePracticesService puAvailablePracticesService;

    @Autowired
    private PcmAvailablePracticesService pcmAvailablePracticesService;

    @RequestMapping(value = "/puProduct/{productCode}/availablePractices", method = RequestMethod.GET)
    public AvailablePractices getPuAvailablePractices(@PathVariable("productCode") String productCode)
            throws IOException {
        String normalProductCode = toNormalISBN(productCode);
        return UnlimitedPracticeUtil
                .getAvailablePractices(puAvailablePracticesService.getPracticeCsvLocations(normalProductCode));
    }

    @RequestMapping(value = "/pcmProduct/{productCode}/availablePractices", method = RequestMethod.GET)
    public AvailablePractices getPcmAvailablePractices(@PathVariable("productCode") String productCode, @RequestParam(value = "sub") String sub) {
        return pcmAvailablePracticesService.getAvailablePractices(productCode, sub);
    }
}
