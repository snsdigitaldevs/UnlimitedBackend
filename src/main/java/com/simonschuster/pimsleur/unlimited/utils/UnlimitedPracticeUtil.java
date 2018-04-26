package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesCsvLocations;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

public class UnlimitedPracticeUtil {
    private static final String FLASH_CARD = "flashCard";
    private static final String READING = "reading";
    private static final String QUICK_MATCH = "quickMatch";
    private static final String SPEAK_EASY = "speakEasy";

    public static AvailablePractices getAvailablePractices(PracticesCsvLocations paths) throws IOException {
        Map<String, Set> unitsSetMap = new HashMap<>();
        unitsSetMap.put(FLASH_CARD, getUnitSetFromCSV(paths.getFlashCardUrl()));
        unitsSetMap.put(READING, getUnitSetFromCSV(paths.getReadingUrl()));
        unitsSetMap.put(QUICK_MATCH, getUnitSetFromCSV(paths.getQuickMatchUrl()));
        unitsSetMap.put(SPEAK_EASY, getUnitSetFromCSV(paths.getSpeakEasyUrl()));
        return setPracticesInUnitFromUnitSets(unitsSetMap);
    }

    private static AvailablePractices setPracticesInUnitFromUnitSets(Map<String, Set> unitsSetMap) {
        AvailablePractices result = new AvailablePractices(new ArrayList<>());
        for (String key : unitsSetMap.keySet()) {
            for (Integer unit : (Set<Integer>) unitsSetMap.get(key)) {
                // this Set must be Integer Set
                PracticesInUnit practiceInUnit = result.getPracticesInUnits().stream()
                        .filter(practice -> practice.getUnitNumber().equals(unit))
                        .findFirst()
                        .orElseGet(() -> {
                            PracticesInUnit practice = new PracticesInUnit(unit);
                            result.getPracticesInUnits().add(practice);
                            return practice;
                        });
                switch (key) {
                    case FLASH_CARD:
                        practiceInUnit.setHasFlashCard(true);
                        break;
                    case READING:
                        practiceInUnit.setHasReading(true);
                        break;
                    case QUICK_MATCH:
                        practiceInUnit.setHasQuickMatch(true);
                        break;
                    case SPEAK_EASY:
                        practiceInUnit.setHasSpeakEasy(true);
                        break;
                    default:
                        // do nothing
                        break;
                }
            }
        }
        return result;
    }

    private static Set getUnitSetFromCSV(String url) throws IOException {
        Set<Integer> units = new HashSet<>();
        if (url == null || url.isEmpty()) {
            return units;
        }
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        Reader in = new StringReader(forObject);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            Integer a = Integer.parseInt(record.get("Unit Num"));
            units.add(a);
        }
        return units;
    }
}
