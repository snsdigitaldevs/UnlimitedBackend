package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.QuickMatch;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.QuickMatchItem;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.replaceDuplicateHeaders;

public class QuickMatchUtil {
    public static List<PracticesInUnit> getQuickMatchesByCsvUrl(String quickMatchesInUrl) throws IOException {
        List<PracticesInUnit> result = new ArrayList<>();
        if (quickMatchesInUrl == null || quickMatchesInUrl.isEmpty()) {
            return null;
        }

        RestTemplate restTemplate = new RestTemplate();
        String csvString = replaceDuplicateHeaders(restTemplate.getForObject(quickMatchesInUrl, String.class));

        CSVParser csvRecords = CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .parse(new StringReader(csvString));
        for (CSVRecord record : csvRecords) {
            String qz = record.get("QZ #");
            String group = qz.contains("_") ? qz.substring(0, 2) : "00";

            Integer unit = Integer.parseInt(record.get("Unit Num"));
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
            if (quickMatches.size() == 0 || quickMatches.get(quickMatches.size() - 1).getCompleted()) {
                quickMatch = new QuickMatch(group);
                quickMatches.add(quickMatch);
            } else {
                quickMatch = quickMatches.get(quickMatches.size() - 1);
            }

            String isQuestionStr = record.get("Q or S");
            if (isQuestionStr != null && !isQuestionStr.isEmpty()) {
                quickMatch.setQuestions(isQuestionStr.equals("Q"));
            }

            QuickMatchItem quickMatchItem = new QuickMatchItem(record.get("Cue"), record.get("Transliteration"), record.get("Snippet Name"));
            if ((qz.charAt(2) == 'Q')) {
                quickMatch.setQuestion(quickMatchItem);
            } else {
                quickMatch.setAnswer(quickMatchItem);
            }
        }
        return result;
    }
}
