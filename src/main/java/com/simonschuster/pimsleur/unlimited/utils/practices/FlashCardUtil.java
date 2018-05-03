package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.FlashCard;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit.createWithFlashCards;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.*;
import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class FlashCardUtil {

    public static List<PracticesInUnit> csvToFlashCards(String csvUrl) throws IOException {
        if (csvUrl == null) {
            return emptyList();
        }

        CSVParser csvRecords = urlToCsv(csvUrl);

        String unitNumKey = findRealHeaderName(csvRecords,"Unit Num");
        String transliterationKey = findRealHeaderName(csvRecords, "Transliteration");
        String translationKey = findRealHeaderName(csvRecords, "English Translation");
        String LanguageKey = findRealHeaderName(csvRecords, "Language");
        String mp3FileKey = findRealHeaderName(csvRecords, "MP3 snippet file name");

        return stream(csvRecords.spliterator(), false)
                .collect(groupingBy(csvRecord -> getUnitNumString(csvRecord, unitNumKey)))
                .entrySet().stream()
                .map(group -> {
                    String unitNumString = group.getKey();
                    if (isNumeric(unitNumString)) {
                        return groupToUnit(unitNumString, transliterationKey, translationKey,
                                LanguageKey, mp3FileKey, group);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted(comparingInt(PracticesInUnit::getUnitNumber))
                .collect(toList());
    }

    private static PracticesInUnit groupToUnit(String unitNumString, String transliterationKey, String translationKey,
                                               String LanguageKey, String mp3FileKey, Map.Entry<String, List<CSVRecord>> group) {
        int unitNumber = parseInt(unitNumString);

        List<FlashCard> speakEasies = group.getValue().stream()
                .map(csvRecord -> new FlashCard(
                        getFromCsv(transliterationKey, csvRecord),
                        getFromCsv(translationKey, csvRecord),
                        getFromCsv(LanguageKey, csvRecord),
                        getFromCsv(mp3FileKey, csvRecord)))
                .collect(toList());

        return createWithFlashCards(unitNumber, speakEasies);
    }

}
