package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.services.practices.PcmAvailablePracticesService;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesCsvLocations;
import com.simonschuster.pimsleur.unlimited.services.practices.PuAvailablePracticesService;
import com.simonschuster.pimsleur.unlimited.utils.QuickMatchUtil;
import com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.unlimited.utils.PuFreeLessonISBNUtil.toNormalISBN;
import static com.simonschuster.pimsleur.unlimited.utils.practices.SpeakEasyUtil.csvToSpeakEasies;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

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
        PracticesCsvLocations csvLocations = puAvailablePracticesService
                .getPracticeCsvLocations(normalProductCode);

        AvailablePractices availablePractices = UnlimitedPracticeUtil.getAvailablePractices(csvLocations);
        List<PracticesInUnit> speakEasies = csvToSpeakEasies(csvLocations.getSpeakEasyUrl());
        List<PracticesInUnit> quickMatches = QuickMatchUtil.getQuickMatchesByCsvUrl(csvLocations.getQuickMatchUrl());
        return new AvailablePractices(mergeLists(availablePractices, speakEasies, quickMatches));
    }

    private List<PracticesInUnit> mergeLists(AvailablePractices availablePractices, List<PracticesInUnit> speakEasies, List<PracticesInUnit> quickMatches) {
        return concat(concat(availablePractices.getPracticesInUnits().stream(), speakEasies.stream()), quickMatches.stream())
                .collect(Collectors.groupingBy(PracticesInUnit::getUnitNumber))
                .values().stream()
                .map(group -> {
                    AtomicReference<PracticesInUnit> merged = new AtomicReference<>(new PracticesInUnit(0));
                    group.forEach(oneUnit -> {
                        merged.set(oneUnit.mergeWith(merged.get()));
                    });
                    return merged.get();
                })
                .sorted(comparingInt(PracticesInUnit::getUnitNumber))
                .collect(toList());
    }

    @RequestMapping(value = "/pcmProduct/{productCode}/availablePractices", method = RequestMethod.GET)
    public AvailablePractices getPcmAvailablePractices(@PathVariable("productCode") String productCode,
                                                       @RequestParam(value = "sub") String sub) {
        return pcmAvailablePracticesService.getAvailablePractices(productCode, sub);
    }
}
