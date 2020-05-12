package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.simonschuster.pimsleur.unlimited.UnlimitedApplication;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.QuickMatch;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.QuickMatchItem;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesUrls;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import org.thymeleaf.util.StringUtils;

import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.*;
import static com.simonschuster.pimsleur.unlimited.utils.dict.QuickMatchHeaders.*;
import static java.nio.charset.Charset.forName;

public class QuickMatchUtil {

    private static final Map<String, String> SKILL_KEYS_MAP = new HashMap<>();

    static {
        SKILL_KEYS_MAP.put("1", "Activities");
        SKILL_KEYS_MAP.put("2", "Animals");
        SKILL_KEYS_MAP.put("3", "Communications");
        SKILL_KEYS_MAP.put("4", "Directions");
        SKILL_KEYS_MAP.put("5", "Friends + Family");
        SKILL_KEYS_MAP.put("6", "Food");
        SKILL_KEYS_MAP.put("7", "General Phrases");
        SKILL_KEYS_MAP.put("8", "Health");
        SKILL_KEYS_MAP.put("9", "Information");
        SKILL_KEYS_MAP.put("10", "Meet + Greet");
        SKILL_KEYS_MAP.put("11", "Money");
        SKILL_KEYS_MAP.put("12", "Numbers");
        SKILL_KEYS_MAP.put("13", "Polite Phrases");
        SKILL_KEYS_MAP.put("14", "Shopping");
        SKILL_KEYS_MAP.put("15", "Speak + Understand");
        SKILL_KEYS_MAP.put("16", "Survival Skills");
        SKILL_KEYS_MAP.put("17", "Time");
        SKILL_KEYS_MAP.put("18", "Travel");
        SKILL_KEYS_MAP.put("19", "Weather");
        SKILL_KEYS_MAP.put("20", "Work Business");
    }

    public static List<PracticesInUnit> getQuickMatchesByCsvUrl(PracticesUrls practicesUrls) throws IOException {
        List<PracticesInUnit> result = new ArrayList<>();
        String quickMatchUrl = practicesUrls.getQuickMatchUrl();
        if (quickMatchUrl == null || quickMatchUrl.isEmpty()) {
            return result;
        }

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(forName("UTF-8")));
        String originCsvString = replaceDuplicateHeaders(restTemplate.getForObject(quickMatchUrl, String.class));
        String csvString = specialCsvFiles(originCsvString);
        CSVParser csvRecords = CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .parse(new StringReader(csvString));

        Map<String, String> headerMap = getRealCsvHeaderMap(csvRecords.getHeaderMap());

        String quickMatchAudioBaseFileName = practicesUrls.getQuickMatchAudioBaseFileName();
        String quickMatchAudioBaseUrl = practicesUrls.getQuickMatchAudioBaseUrl();
        for (CSVRecord record : csvRecords) {
            parseCsvLine(result, record, headerMap, quickMatchAudioBaseUrl, quickMatchAudioBaseFileName);
        }

        getSkill(quickMatchUrl, result);
        return result;
    }

    private static void getSkill(String originIsbn, List<PracticesInUnit> result) throws IOException {
        String isbn = getIsbn(originIsbn);
        InputStream fileStream = UnlimitedApplication.class.getClassLoader().getResourceAsStream("skill/" + isbn + ".csv");
        if (fileStream == null) {
            return;
        }
        InputStreamReader streamReader = new InputStreamReader(fileStream);
        StringBuilder strBuf = new StringBuilder();
        while (streamReader.ready()) {
            strBuf.append((char) streamReader.read());
        }
        String originCsvString = specialCsvFiles(strBuf.toString());
        String csvString = specialCsvFiles(originCsvString);
        CSVParser csvRecords = CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .parse(new StringReader(csvString));
        Map<String, String> headerMap = getRealCsvHeaderMap(csvRecords.getHeaderMap());
        for (CSVRecord record : csvRecords) {
            parseSkillCsvLine(result, SKILL_KEYS_MAP, headerMap, record);
        }
    }

    private static String getIsbn(String quickMatchesInUrl) {
        String[] urls = quickMatchesInUrl.split("/");
        return urls[urls.length - 1].split("_")[0];
    }

    private static Map<String, String> getHeaderMap(CSVParser csvRecords, List<String> originHeaders) {
        Map<String, String> result = new HashMap<>();
        for (String originHeader : originHeaders) {
            result.put(originHeader, getHeaderInCsv(csvRecords, originHeader));
        }
        return result;
    }

    public static Map<String, String> getRealCsvHeaderMap(Map<String, ?> realCsvHeaderMaps) {
        Map<String, String> upperRealCsvRecordName = new HashMap<>(realCsvHeaderMaps.size());
        for (String key : realCsvHeaderMaps.keySet()) {
            upperRealCsvRecordName.put(key.toUpperCase(), key);
        }
        Map<String, String> realCsvRecordHeaderMap = new HashMap<>(realCsvHeaderMaps.size());
        String qz = findRealHeaderName(upperRealCsvRecordName, HEADER_QUIZ, HEADER_QZ);
        realCsvRecordHeaderMap.put(HEADER_QZ, qz);
        String snippetName = findRealHeaderName(upperRealCsvRecordName, HEADER_SNIPPET_NAME, HEADER_MP_SNIPPET_FILE_NAME);
        realCsvRecordHeaderMap.put(HEADER_SNIPPET_NAME, snippetName);
        String audioIsbn = findRealHeaderName(upperRealCsvRecordName, HEADER_MP3_AUDIO_ISBN, HEADER_ISBN);
        realCsvRecordHeaderMap.put(HEADER_ISBN, audioIsbn);
        String unitNum = findRealHeaderName(upperRealCsvRecordName, HEADER_UNIT_NUM, HEADER_LESSON);
        realCsvRecordHeaderMap.put(HEADER_UNIT_NUM, unitNum);

        return realCsvRecordHeaderMap;
    }

    private static void parseCsvLine(List<PracticesInUnit> result, CSVRecord record,
                                     Map<String, String> headerMap,
                                     String quickMatchAudioBaseUrl, String quickMatchAudioBaseFileName) {
        String qz = record.get(headerMap.get(HEADER_QZ));
        Integer unit = Integer.parseInt(record.get(headerMap.get(HEADER_UNIT_NUM)).trim());
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

        if (record.isSet(HEADER_Q_OR_S)) {
            String isQuestionStr = record.get(HEADER_Q_OR_S);
            if (isQuestionStr != null && !isQuestionStr.isEmpty()) {
                quickMatch.setQuestions(isQuestionStr.equals(QUESTION_Q));
            }
        } else {
            quickMatch.setQuestions(false);
        }
        quickMatch.setQz(qz);

        String transliteration = record.isSet(HEADER_TRANSLITERATION) ? record.get(HEADER_TRANSLITERATION) : "";
        String snippetName = getSnippetName(record, headerMap, quickMatchAudioBaseFileName);

        Collection<String> values = record.toMap().values();
        List<String> valueList = new ArrayList(values);

        String cue = record.isSet(HEADER_CUE) ? record.get(HEADER_CUE) : valueList.get(10).toString();
        QuickMatchItem quickMatchItem = new QuickMatchItem(cue,
                transliteration, quickMatchAudioBaseUrl + snippetName);

        String qzWithoutGroup = qz.split("_")[0];
        if ((qzWithoutGroup.charAt(qzWithoutGroup.length() - 1) == 'Q')) {
            quickMatch.setQuestion(quickMatchItem);
        } else {
            quickMatch.setAnswer(quickMatchItem);
        }
    }

    private static String getSnippetName(CSVRecord record, Map<String, String> headerMap,
                                         String quickMatchAudioBaseFileName) {
        String snippetNameKey = headerMap.get(HEADER_SNIPPET_NAME);
        if (record.isSet(snippetNameKey)) {
            return record.get(snippetNameKey);
        } else {
            return quickMatchAudioBaseFileName
                    .concat("_U")
                    .concat(String.valueOf(Integer.parseInt(record.get(headerMap.get(HEADER_UNIT_NUM)))))
                    .concat("_")
                    .concat(record.get(headerMap.get(HEADER_QZ)))
                    .concat(".mp3");
        }
    }

    private static String getSnippetNameKey(CSVRecord record) {
        String snippetNameKey = "Snippet Name";
        String lowerCaseKey = "Snippet name";
        if (record.isSet(lowerCaseKey)) {
            snippetNameKey = lowerCaseKey;
        }
        return snippetNameKey;
    }

    private static String getHeaderInCsv(CSVParser csvRecords, String originHeader) {
        for (String key : csvRecords.getHeaderMap().keySet()) {
            if (StringUtils.equalsIgnoreCase(key, originHeader) || (originHeader.equals(HEADER_ISBN)
                && key.toUpperCase().contains(originHeader)) || (originHeader.equals(HEADER_QZ)
                && key.toUpperCase().equals(HEADER_QUIZ))) {
                return key;
            }
        }
        return originHeader;
    }

    private static void parseSkillCsvLine(List<PracticesInUnit> result, Map<String, String> skillKeyMap, Map<String, String> headerMap, CSVRecord record) {

        String qz = record.get(headerMap.get(HEADER_QZ));
        Integer unit = Integer.parseInt(record.get(headerMap.get(HEADER_UNIT_NUM)));

        PracticesInUnit practices = result.stream()
                .filter(practicesInUnit -> practicesInUnit.getUnitNumber().equals(unit))
                .findFirst()
                .orElse(null);
        if (practices == null) {
            return;
        }
        practices.setHasSkills(true);
        QuickMatch counterpartQuickMatch = practices.getQuickMatches().stream()
                .filter(quickMatch -> quickMatch.getQz().equals(qz))
                .findFirst()
                .orElse(null);
        if (counterpartQuickMatch == null) {
            return;
        }
        counterpartQuickMatch.setSkills(Arrays.stream(record.get(HEADER_SKILLS).split(","))
                .map(skillKeyMap::get)
                .collect(Collectors.toList()));
    }

}
