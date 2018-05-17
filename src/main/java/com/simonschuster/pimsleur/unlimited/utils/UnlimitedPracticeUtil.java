package com.simonschuster.pimsleur.unlimited.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.forName;
import static java.util.Collections.frequency;

public class UnlimitedPracticeUtil {

    public static String getUnitNumString(CSVRecord record, String unitNumKey) {
        if (record.isSet(unitNumKey)) {
            return record.get(unitNumKey).replace("\"", "");
        }
        return "NoSuchKey";
    }

    public static String findRealHeaderName(CSVRecord oneRecord, String key) {
        String quotedKey = "\"" + key + "\"";
        String quotedKeyLowerCase = quotedKey.toLowerCase();
        String keyLowerCase = key.toLowerCase();

        if (oneRecord.isMapped(quotedKey)) {
            return quotedKey;
        } else if (oneRecord.isMapped(quotedKeyLowerCase)) {
            return quotedKeyLowerCase;
        } else if (oneRecord.isMapped(quotedKeyLowerCase)) {
            return keyLowerCase;
        }

        return key;
    }

    public static String replaceDuplicateHeaders(String csvString) {
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

    public static List<CSVRecord> urlToCsv(String url) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(forName("UTF-8")));
        String csvString = replaceDuplicateHeaders(restTemplate.getForObject(url, String.class));
        if (csvString.contains("Italian 2")) {
            csvString = specialCsvFiles(csvString);
        }

        return csvStringToObj(csvString);
    }

    private static List<CSVRecord> csvStringToObj(String csvString) throws IOException {
        CSVFormat csvFormat = CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .withIgnoreEmptyLines()
                .withIgnoreHeaderCase();
        try {
            // first try to parse csv with quotes
            return csvFormat
                    .parse(new StringReader(csvString)).getRecords();
        } catch (IOException e) {
            // if try fails, then try to parse csv without quotes
            return csvFormat
                    .withQuote(null)
                    .parse(new StringReader(csvString)).getRecords();
        }
    }

    public static String getFromCsv(String key, CSVRecord csvRecord) {
        if (csvRecord.isSet(key)) {
            return csvRecord.get(key).replace("\"", "");
        }
        return "";
    }

    public static String specialCsvFiles(String csvString) {
        if (csvString.contains("Spanish 3")) {
            csvString = csvString.replace("\nplease", "please").replace("\nremoved", "removed");
        }
        String rightEnd = "\",";

        String[] csvArray = csvString.split("\n");
        String header = csvArray[0];
        return header + "\n" + Arrays.stream(csvArray)
                .skip(1)
                .map(line -> {
                    if (line.contains("Italian 2") || line.contains("Italian 3")) {
                        if (!line.endsWith(rightEnd)) {
                            line = line.substring(0, line.lastIndexOf(rightEnd) + rightEnd.length());
                        }
                        if (line.contains("\"\"")) {
                            line = line.replace("\"\"", "\"");
                        }
                    } else if (line.contains("Spanish 3") && line.contains("477,") && line.contains("\" (")) {
                        line = line.replace("\" (", " (").replace("\"in,\"", "in,");
                    }
                    return line;
                })
                .collect(Collectors.joining("\n"));
    }
}
