package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.SpeakEasyOrReading;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit.createWithReadings;
import static com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit.createWithSpeakEasies;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.*;
import static com.simonschuster.pimsleur.unlimited.utils.dict.SpeakEasyAndReadingHeaders.*;
import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class SpeakEasyAndReadingUtil {

    private static final DateTimeFormatter COLON_COLON_TIME_FORMATTER = forPattern("mm:ss:SSS");
    private static final DateTimeFormatter COLON_DOT_TIME_FORMATTER = forPattern("mm:ss.SSS");
    private static final DateTimeFormatter STAR_TIME_FORMATTER = forPattern("mm:*ss.*SSS");
    private static final DateTimeFormatter DOT_DOT_TIME_FORMATTER = forPattern("mm:ss..SSS");
    private static final DateTimeFormatter BEGIN_STAR_TIME_FORMATTER = forPattern("*mm:*ss.*SSS");

    public static List<PracticesInUnit> csvToSpeakEasies(String csvUrl) throws IOException {
        return getPracticesInUnits(csvUrl, HEADER_LESSON, HEADER_TEXT, HEADER_NATIVE_TEXT, HEADER_VIS_CONV, false);
    }

    public static List<PracticesInUnit> csvToReadings(String csvUrl) throws IOException {
        return getPracticesInUnits(csvUrl, HEADER_PREMIUM_RL_DAY, HEADER_ENGLISH_TRANSLATION, HEADER_LANGUAGE, HEADER_RL_ITEM, true);
    }

    private static List<PracticesInUnit> getPracticesInUnits(String csvUrl, String alterKey, String text, String nativeText,
                                                             String order, boolean isReading) throws IOException {
        if (csvUrl == null) {
            return emptyList();
        }

        List<CSVRecord> csvRecords = urlToCsv(csvUrl);

        Map<String, String> csvRecordHeader = convertToUpperCSVRecordHeaderMap(csvRecords.get(0));

        String unitNumKey = findRealHeaderName(csvRecordHeader, HEADER_UNIT_NUM, alterKey);
        String startKey = findRealHeaderName(csvRecordHeader, HEADER_START);
        String stopKey = findRealHeaderName(csvRecordHeader, HEADER_STOP);
        String speakerKey = findRealHeaderName(csvRecordHeader, HEADER_SPKR, HEADER_SPKR);
        String transliterationKey = findRealHeaderName(csvRecordHeader, HEADER_TRANSLITERATION);
        String helpTextKey = findRealHeaderName(csvRecordHeader, HEADER_XLITHELP);
        String textKey = findRealHeaderName(csvRecordHeader, text, HEADER_TRANSLATION);
        String nativeTextKey = findRealHeaderName(csvRecordHeader, nativeText);
        String orderKey = findRealHeaderName(csvRecordHeader, order);

        return csvRecords.stream()
                .collect(groupingBy(csvRecord -> getUnitNumString(csvRecord, unitNumKey)))
                .entrySet().stream()
                .map(group -> {
                    String unitNumString = group.getKey().trim();
                    if (isNumeric(unitNumString)) {
                        return groupToUnit(unitNumString, startKey, stopKey, speakerKey, textKey,
                                nativeTextKey, orderKey, group, transliterationKey, helpTextKey, isReading);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted(comparingInt(PracticesInUnit::getUnitNumber))
                .collect(toList());
    }

    private static PracticesInUnit groupToUnit(String unitNumString, String startKey, String stopKey,
                                               String speakerKey, String textKey, String nativeTextKey,
                                               String orderKey, Map.Entry<String, List<CSVRecord>> group,
                                               String transliterationKey, String helpTextKey, boolean isReading) {
        int unitNumber = parseInt(unitNumString);
        final int[] counter = {0};

        List<SpeakEasyOrReading> speakEasies = group.getValue().stream()
                .map(csvRecord -> new SpeakEasyOrReading(
                        getMilliSeconds(startKey, csvRecord),
                        getMilliSeconds(stopKey, csvRecord),
                        getFromCsv(speakerKey, csvRecord),
                        generateText(isReading, textKey, csvRecord),
                        getFromCsv(nativeTextKey, csvRecord),
                        getFromCsv(transliterationKey, csvRecord),
                        Jsoup.parse(getFromCsv(helpTextKey, csvRecord)).text(),
                        getOrder(orderKey, counter, csvRecord)))
                .collect(toList());
        return isReading ? createWithReadings(unitNumber, speakEasies) : createWithSpeakEasies(unitNumber, speakEasies);
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
            return BEGIN_STAR_TIME_FORMATTER.parseDateTime(timeString).getMillisOfDay();
        } else if (timeString.contains("..")) {
            return DOT_DOT_TIME_FORMATTER.parseDateTime(timeString).getMillisOfDay();
        } else if (timeString.contains("*")) {
            return STAR_TIME_FORMATTER.parseDateTime(timeString).getMillisOfDay();
        } else if (timeString.contains(".")) {
            return COLON_DOT_TIME_FORMATTER.parseDateTime(timeString).getMillisOfDay();
        } else {
            return COLON_COLON_TIME_FORMATTER.parseDateTime(timeString).getMillisOfDay();
        }
    }

    private static String generateText(boolean isReading, String textKey, CSVRecord csvRecord) {
        return isReading ? (csvRecord.isSet(textKey) ? csvRecord.get(textKey) : "") : getFromCsv(textKey, csvRecord);
    }

}
