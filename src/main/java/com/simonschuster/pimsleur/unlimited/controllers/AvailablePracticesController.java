package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.services.practices.PcmAvailablePracticesService;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesUrls;
import com.simonschuster.pimsleur.unlimited.services.practices.PuAvailablePracticesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.simonschuster.pimsleur.unlimited.utils.HardCodedProductsUtil.puFreeToPuNormalIsbn;
import static com.simonschuster.pimsleur.unlimited.utils.practices.FlashCardUtil.csvToFlashCards;
import static com.simonschuster.pimsleur.unlimited.utils.practices.QuickMatchUtil.getQuickMatchesByCsvUrl;
import static com.simonschuster.pimsleur.unlimited.utils.practices.SpeakEasyAndReadingUtil.csvToReadings;
import static com.simonschuster.pimsleur.unlimited.utils.practices.SpeakEasyAndReadingUtil.csvToSpeakEasies;
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
        String normalProductCode = puFreeToPuNormalIsbn(productCode);
        PracticesUrls practicesUrls = puAvailablePracticesService.getPracticeUrls(normalProductCode);

        List<PracticesInUnit> speakEasies = csvToSpeakEasies(practicesUrls.getSpeakEasyUrl());
        List<PracticesInUnit> readings = csvToReadings(practicesUrls.getReadingUrl());
        List<PracticesInUnit> flashCards = csvToFlashCards(practicesUrls);
        List<PracticesInUnit> quickMatches = getQuickMatchesByCsvUrl(practicesUrls.getQuickMatchUrl());

        return new AvailablePractices(mergeLists(readings, speakEasies, flashCards, quickMatches));
    }

    @RequestMapping(value = "/pcmProduct/{productCode}/availablePractices", method = RequestMethod.GET)
    public AvailablePractices getPcmAvailablePractices(@PathVariable("productCode") String productCode,
                                                       @RequestParam(value = "sub") String sub) {
        return pcmAvailablePracticesService.getAvailablePractices(productCode, sub);
    }

    private List<PracticesInUnit> mergeLists(List<PracticesInUnit> readings,
                                             List<PracticesInUnit> speakEasies,
                                             List<PracticesInUnit> flashCards,
                                             List<PracticesInUnit> quickMatches) {
        Stream<PracticesInUnit> allPractices = concat(concat(concat(
                readings.stream(), speakEasies.stream()),
                quickMatches.stream()), flashCards.stream());

        return allPractices
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
}
