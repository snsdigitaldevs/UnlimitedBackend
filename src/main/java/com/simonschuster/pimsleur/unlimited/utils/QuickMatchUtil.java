package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.QuickMatch;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.QuickMatchItem;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.replaceDuplicateHeaders;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.specialCsvFiles;
import static java.nio.charset.Charset.forName;

public class QuickMatchUtil {
    public static List<PracticesInUnit> getQuickMatchesByCsvUrl(String quickMatchesInUrl) throws IOException {
        List<PracticesInUnit> result = new ArrayList<>();
        if (quickMatchesInUrl == null || quickMatchesInUrl.isEmpty()) {
            return result;
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(forName("UTF-8")));
        String csvString = specialCsvFiles(replaceDuplicateHeaders(restTemplate.getForObject(quickMatchesInUrl, String.class)));
        CSVParser csvRecords = CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .parse(new StringReader(csvString));
        String[] needIgnoreCaseHeaders = {"QZ #", "Snippet Name"};
        Map<String, String> headerMap = getHeaderMap(csvRecords, Arrays.asList(needIgnoreCaseHeaders));
        for (CSVRecord record : csvRecords) {
            parseCsvLine(result, record, headerMap);
        }
        return result;
    }

    private static Map<String, String> getHeaderMap(CSVParser csvRecords, List<String> originHeaders) {
        Map<String, String> result = new HashMap<>();
        for (String originHeader : originHeaders) {
            result.put(originHeader, getHeaderIgnoreCase(csvRecords, originHeader));
        }
        return result;
    }

    private static void parseCsvLine(List<PracticesInUnit> result, CSVRecord record, Map<String, String> headerMap) {
        String qz = record.get(headerMap.get("QZ #"));
        Integer unit = Integer.parseInt(record.get("Unit Num"));
        String group = qz.contains("_") ? unit.toString() + "_" + qz.substring(0, 2) : "00";

        List<QuickMatch> quickMatches = result.stream()
                .filter(practices -> practices.getUnitNumber().equals(unit))
                .findFirst()
                .orElseGet(() -> {
                    PracticesInUnit newPracticesInUnit = new PracticesInUnit(unit);
                    result.add(newPracticesInUnit);
                    newPracticesInUnit.setHasQuickMatch(true);
                    return newPracticesInUnit;
                })
                .getQuickMatches();

        QuickMatch quickMatch;
        if (quickMatches.size() == 0 || quickMatches.get(quickMatches.size() - 1).completed()) {
            quickMatch = new QuickMatch(group);
            quickMatches.add(quickMatch);
        } else {
            quickMatch = quickMatches.get(quickMatches.size() - 1);
        }

        if (record.isSet("Q or S")) {
            String isQuestionStr = record.get("Q or S");
            if (isQuestionStr != null && !isQuestionStr.isEmpty()) {
                quickMatch.setQuestions(isQuestionStr.equals("Q"));
            }
        } else {
            quickMatch.setQuestions(false);
        }

        String transliteration = getStringIfNotExist(record, "Transliteration");
        String snippetName = getStringIfNotExist(record, "Snippet Name");
        QuickMatchItem quickMatchItem = new QuickMatchItem(record.get("Cue"), transliteration, snippetName);
        String qzWithoutGroup = qz.split("_")[0];
        if ((qzWithoutGroup.charAt(qzWithoutGroup.length() - 1) == 'Q')) {
            quickMatch.setQuestion(quickMatchItem);
        } else {
            quickMatch.setAnswer(quickMatchItem);
        }
    }

    private static String getStringIfNotExist(CSVRecord record, String column) {
        return record.isSet(column) ? record.get(column) : "";
    }

    private static String getHeaderIgnoreCase(CSVParser csvRecords, String originHeader) {
        for (String key : csvRecords.getHeaderMap().keySet()) {
            if (key.toLowerCase().equals(originHeader.toLowerCase())) {
                return key;
            }
        }
        return originHeader;
    }
}
