package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.SpeakEasy;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit.createWithSpeakEasies;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.*;
import static java.lang.Integer.parseInt;
import static java.nio.charset.Charset.forName;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class SpeakEasyUtil {

    private static DateTimeFormatter colonDotTimeFormatter = forPattern("mm:ss.SSS");
    private static DateTimeFormatter colonColonTimeFormatter = forPattern("mm:ss:SSS");

    public static List<PracticesInUnit> csvToSpeakEasies(String csvUrl) throws IOException {
        CSVParser csvRecords = getCsvRecordsFromUrl(csvUrl);

        String unitNumKey = unitNumKey(csvRecords);
        String startKey = findRealHeaderName(csvRecords, "Start");
        String stopKey = findRealHeaderName(csvRecords, "Stop");
        String speakerKey = findRealHeaderName(csvRecords, "Spkr");
        String textKey = findRealHeaderName(csvRecords, "Text");
        String nativeTextKey = findRealHeaderName(csvRecords, "NativeText");
        String orderKey = findRealHeaderName(csvRecords, "Vis Conv");

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
                .sorted(comparingInt(PracticesInUnit::getUnitNumber))
                .collect(toList());
    }

    private static PracticesInUnit groupToUnit(String unitNumString, String startKey, String stopKey,
                                               String speakerKey, String textKey, String nativeTextKey,
                                               String orderKey, Map.Entry<String, List<CSVRecord>> group) {
        int unitNumber = parseInt(unitNumString);

        final int[] counter = {0};

        List<SpeakEasy> speakEasies = group.getValue().stream()
                .map(csvRecord -> new SpeakEasy(
                        getMilliSeconds(startKey, csvRecord),
                        getMilliSeconds(stopKey, csvRecord),
                        getFromCsv(speakerKey, csvRecord),
                        getFromCsv(textKey, csvRecord),
                        getFromCsv(nativeTextKey, csvRecord),
                        getOrder(orderKey, counter, csvRecord)))
                .collect(toList());

        return createWithSpeakEasies(unitNumber, speakEasies);
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
        String timeString = getFromCsv(key, csvRecord);
        if (timeString.contains(".")) {
            return colonDotTimeFormatter.parseDateTime(timeString).getMillisOfDay();
        } else {
            return colonColonTimeFormatter.parseDateTime(timeString).getMillisOfDay();
        }
    }

    private static String getFromCsv(String key, CSVRecord csvRecord) {
        if (csvRecord.isSet(key)) {
            return csvRecord.get(key).replace("\"", "");
        }
        return "";
    }

    private static CSVParser getCsvRecordsFromUrl(String url) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(forName("UTF-8")));
        String csvString = replaceDuplicateHeaders(restTemplate.getForObject(url, String.class));

        return CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .withIgnoreEmptyLines()
                .withIgnoreHeaderCase()
                .parse(new StringReader(csvString));
    }
}
