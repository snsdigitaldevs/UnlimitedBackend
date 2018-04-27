package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.AvailablePractices;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesCsvLocations;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.frequency;

public class UnlimitedPracticeUtil {
    private static final String FLASH_CARD = "flashCard";
    private static final String READING = "reading";
    private static final String QUICK_MATCH = "quickMatch";
    private static final String SPEAK_EASY = "speakEasy";

    public static AvailablePractices getAvailablePractices(PracticesCsvLocations paths) throws IOException {
        Map<String, Set<Integer>> unitsSetMap = new HashMap<>();
        unitsSetMap.put(FLASH_CARD, getUnitSetFromCSV(paths.getFlashCardUrl()));
        unitsSetMap.put(READING, getUnitSetFromCSV(paths.getReadingUrl()));
        unitsSetMap.put(QUICK_MATCH, getUnitSetFromCSV(paths.getQuickMatchUrl()));
        unitsSetMap.put(SPEAK_EASY, getUnitSetFromCSV(paths.getSpeakEasyUrl()));
        return setPracticesInUnitFromUnitSets(unitsSetMap);
    }

    private static AvailablePractices setPracticesInUnitFromUnitSets(Map<String, Set<Integer>> unitsSetMap) {
        AvailablePractices result = new AvailablePractices(new ArrayList<>());
        for (String key : unitsSetMap.keySet()) {
            for (Integer unit : unitsSetMap.get(key)) {
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

    private static Set<Integer> getUnitSetFromCSV(String url) throws IOException {
        Set<Integer> units = new HashSet<>();
        if (url == null || url.isEmpty()) {
            return units;
        }
        RestTemplate restTemplate = new RestTemplate();
        String csvString = replaceDuplicateHeaders(restTemplate.getForObject(url, String.class));

        CSVParser csvRecords = CSVFormat.EXCEL
                .withFirstRecordAsHeader().withQuote(null)
                .parse(new StringReader(csvString));
        String unitNumKey = unitNumKey(csvRecords);
        for (CSVRecord record : csvRecords) {
            System.out.println(record);
            Integer unitNum = Integer.parseInt(record.get(unitNumKey).replace("\"", ""));
            units.add(unitNum);
        }
        return units;
    }

    private static String unitNumKey(CSVParser csvRecords) {
        String quotedUnitNum = "\"Unit Num\"";
        String unitNum = "Unit Num";
        if (csvRecords.getHeaderMap().containsKey(unitNum)) {
            return unitNum;
        }
        return quotedUnitNum;
    }

    private static String replaceDuplicateHeaders(String csvString) {
        final Integer[] columnIndex = {0};

        String[] headerAndBody = csvString.split(System.lineSeparator(), 2);
        String header = headerAndBody[0];

        List<String> columns = Arrays.asList(header.split(","));
        List<String> noDuplicatedColumns = columns.stream().map((column) -> {
            int frequency = frequency(columns, column);
            if (frequency > 1) {
                columnIndex[0] = columnIndex[0] + 1;
                return column.substring(0, column.length() - 1) +
                        columnIndex[0] +
                        column.substring(column.length() - 1, column.length());
            }
            return column;
        }).collect(Collectors.toList());

        return String.join(",", noDuplicatedColumns) +
                System.lineSeparator() +
                headerAndBody[1];
    }
}
