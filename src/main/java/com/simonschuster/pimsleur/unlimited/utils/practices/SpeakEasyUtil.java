package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.SpeakEasy;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit.createWithSpeakEasies;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.*;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class SpeakEasyUtil {

    private static DateTimeFormatter formatter = forPattern("mm:ss.SSS");

    public static List<PracticesInUnit> csvToSpeakEasies(String csvUrl) throws IOException {
        CSVParser csvRecords = getCsvRecordsFromUrl(csvUrl);

        String unitNumKey = unitNumKey(csvRecords);
        String startKey = addQuoteToKeyIfNeeded(csvRecords, "Start");
        String stopKey = addQuoteToKeyIfNeeded(csvRecords, "Stop");
        String speakerKey = addQuoteToKeyIfNeeded(csvRecords, "Spkr");
        String textKey = addQuoteToKeyIfNeeded(csvRecords, "Text");
        String nativeTextKey = addQuoteToKeyIfNeeded(csvRecords, "NativeText");
        String orderKey = addQuoteToKeyIfNeeded(csvRecords, "Vis Conv");

        return stream(csvRecords.spliterator(), false)
                .collect(groupingBy(csvRecord -> getUnitNumString(csvRecord, unitNumKey)))
                .entrySet().stream()
                .map(group -> {
                    String unitNumString = group.getKey();
                    if (isNumeric(unitNumString)) {
                        return groupToUnit(unitNumString, startKey, stopKey, speakerKey, textKey,
                                nativeTextKey, orderKey, group);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private static PracticesInUnit groupToUnit(String unitNumString, String startKey, String stopKey,
                                               String speakerKey, String textKey, String nativeTextKey,
                                               String orderKey, Map.Entry<String, List<CSVRecord>> group) {
        int unitNumber = parseInt(unitNumString);

        List<SpeakEasy> speakEasies = group.getValue().stream()
                .map(csvRecord -> {
                    String orderString = getFromCsv(orderKey, csvRecord);
                    int order = parseInt(orderString.split("VC")[1]);

                    return new SpeakEasy(
                            getMilliSeconds(startKey, csvRecord),
                            getMilliSeconds(stopKey, csvRecord),
                            getFromCsv(speakerKey, csvRecord),
                            getFromCsv(textKey, csvRecord),
                            getFromCsv(nativeTextKey, csvRecord),
                            order);
                })
                .collect(toList());

        return createWithSpeakEasies(unitNumber, speakEasies);
    }

    private static int getMilliSeconds(String key, CSVRecord csvRecord) {
        return formatter.parseDateTime(getFromCsv(key, csvRecord)).getMillisOfDay();
    }

    private static String getFromCsv(String key, CSVRecord csvRecord) {
        if (csvRecord.isSet(key)) {
            return csvRecord.get(key).replace("\"", "");
        }
        return "";
    }
}
