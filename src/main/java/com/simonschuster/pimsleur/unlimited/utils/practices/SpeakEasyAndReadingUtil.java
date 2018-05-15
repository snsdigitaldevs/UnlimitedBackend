package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.SpeakEasyOrReading;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit.createWithReadings;
import static com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit.createWithSpeakEasies;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.*;
import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class SpeakEasyAndReadingUtil {

    private static DateTimeFormatter colonColonTimeFormatter = forPattern("mm:ss:SSS");
    private static DateTimeFormatter colonDotTimeFormatter = forPattern("mm:ss.SSS");
    private static DateTimeFormatter starTimeFormatter = forPattern("mm:*ss.*SSS");
    private static DateTimeFormatter dotDotTimeFormatter = forPattern("mm:ss..SSS");
    private static DateTimeFormatter beginStarTimeFormatter = forPattern("*mm:*ss.*SSS");

    public static List<PracticesInUnit> csvToSpeakEasies(String csvUrl) throws IOException {
        return getPracticesInUnits(csvUrl, "Text", "NativeText", "Vis Conv", false);
    }

    public static List<PracticesInUnit> csvToReadings(String csvUrl) throws IOException {
        return getPracticesInUnits(csvUrl, "English Translation", "Language", "RL Item #", true);
    }

    private static List<PracticesInUnit> getPracticesInUnits(String csvUrl, String text, String nativeText, String order, boolean isReading) throws IOException {
        if (csvUrl == null) {
            return emptyList();
        }

        CSVParser csvRecords = urlToCsv(csvUrl);

        String unitNumKey = unitNumKey(csvRecords);
        String startKey = findRealHeaderName(csvRecords, "Start");
        String stopKey = findRealHeaderName(csvRecords, "Stop");
        String speakerKey = findRealHeaderName(csvRecords, "Spkr");
        String textKey = findRealHeaderName(csvRecords, text);
        String nativeTextKey = findRealHeaderName(csvRecords, nativeText);
        String orderKey = findRealHeaderName(csvRecords, order);

        return stream(csvRecords.spliterator(), false)
                .collect(groupingBy(csvRecord -> getUnitNumString(csvRecord, unitNumKey)))
                .entrySet().stream()
                .map(group -> {
                    String unitNumString = group.getKey();
                    if (isNumeric(unitNumString)) {
                        return groupToUnit(unitNumString, startKey, stopKey, speakerKey, textKey,
                                nativeTextKey, orderKey, group, isReading);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted(comparingInt(PracticesInUnit::getUnitNumber))
                .collect(toList());
    }

    private static PracticesInUnit groupToUnit(String unitNumString, String startKey, String stopKey,
                                               String speakerKey, String textKey, String nativeTextKey,
                                               String orderKey, Map.Entry<String, List<CSVRecord>> group, boolean isReading) {
        int unitNumber = parseInt(unitNumString);

        final int[] counter = {0};

        List<SpeakEasyOrReading> speakEasies = group.getValue().stream()
                .map(csvRecord -> new SpeakEasyOrReading(
                        getMilliSeconds(startKey, csvRecord),
                        getMilliSeconds(stopKey, csvRecord),
                        getFromCsv(speakerKey, csvRecord),
                        getFromCsv(textKey, csvRecord),
                        getFromCsv(nativeTextKey, csvRecord),
                        getOrder(orderKey, counter, csvRecord)))
                .collect(toList());

        if (isReading) {
            return createWithReadings(unitNumber, speakEasies);
        } else {
            return createWithSpeakEasies(unitNumber, speakEasies);
        }
    }

    private static int getOrder(String orderKey, int[] counter, CSVRecord csvRecord) {
        int order;
        String orderString = getFromCsv(orderKey, csvRecord);
        String[] vcs = orderString.split("VC");
        if (vcs.length == 2) {
            order = parseInt(vcs[1]);
            counter[0] = order;
        } else {
            counter[0]++;
            order = counter[0];
        }
        return order;
    }

    private static int getMilliSeconds(String key, CSVRecord csvRecord) {
        String timeString = getFromCsv(key, csvRecord).trim();

        if (timeString.startsWith("*")) {
            return beginStarTimeFormatter.parseDateTime(timeString).getMillisOfDay();
        } else if (timeString.contains("..")) {
            return dotDotTimeFormatter.parseDateTime(timeString).getMillisOfDay();
        } else if (timeString.contains("*")) {
            return starTimeFormatter.parseDateTime(timeString).getMillisOfDay();
        } else if (timeString.contains(".")) {
            return colonDotTimeFormatter.parseDateTime(timeString).getMillisOfDay();
        } else {
            return colonColonTimeFormatter.parseDateTime(timeString).getMillisOfDay();
        }
    }

}
