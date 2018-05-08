package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.QuickMatchItem;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.Skill;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.unlimited.utils.QuickMatchUtil.getHeaderMap;
import static com.simonschuster.pimsleur.unlimited.utils.QuickMatchUtil.getSnippetName;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.specialCsvFiles;

public class SkillUtil {
    public static List<PracticesInUnit> getSkillsByIsbn(String Isbn) throws IOException {
        List<PracticesInUnit> result = new ArrayList<>();
        File skillDir = new File("src/main/resources/skill");
        String fileName = Arrays.stream(skillDir.list())
                .filter(file -> file.contains(Isbn))
                .findFirst()
                .orElse(null);
        if (fileName == null) {
            return result;
        }
        
        Map<String, String> skillKeyMap = getSkillKeyMap(skillDir);
        File skillCsv = new File(skillDir, fileName);
        FileInputStream fileInputStream = new FileInputStream(skillCsv);
        InputStreamReader streamReader = new InputStreamReader(fileInputStream);
        StringBuilder strBuf = new StringBuilder();
        while (streamReader.ready()) {
            strBuf.append((char) streamReader.read());
        }
        String originCsvString = specialCsvFiles(strBuf.toString());
        String csvString = specialCsvFiles(originCsvString);
        CSVParser csvRecords = CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .parse(new StringReader(csvString));
        String[] needIgnoreCaseHeaders = {"QZ #", "Snippet Name", "ISBN"};
        Map<String, String> headerMap = getHeaderMap(csvRecords, Arrays.asList(needIgnoreCaseHeaders));
        for (CSVRecord record : csvRecords) {
            parseCsvLine(result, skillKeyMap, headerMap, record);
        }
        return result;
    }

    private static void parseCsvLine(List<PracticesInUnit> result, Map<String, String> skillKeyMap, Map<String, String> headerMap, CSVRecord record) {
        String qz = record.get(headerMap.get("QZ #"));
        Integer unit = Integer.parseInt(record.get("Unit Num"));
        List<Skill> skills = result.stream()
                .filter(practices -> practices.getUnitNumber().equals(unit))
                .findFirst()
                .orElseGet(() -> {
                    PracticesInUnit newPracticesInUnit = new PracticesInUnit(unit);
                    result.add(newPracticesInUnit);
                    newPracticesInUnit.setHasSkills(true);
                    return newPracticesInUnit;
                })
                .getSkills();
        Skill skill;
        if (skills.size() == 0 || skills.get(skills.size() - 1).completed()) {
            skill = new Skill();
            skills.add(skill);
        } else {
            skill = skills.get(skills.size() - 1);
        }

        skill.setCategories(Arrays.stream(record.get("Skills").split(","))
                .map(skillKeyMap::get)
                .collect(Collectors.toList()));
        String transliteration = record.isSet("Transliteration") ? record.get("Transliteration") : "";
        String snippetName = record.isSet("Snippet Name") ? record.get("Snippet Name") : getSnippetName(record, headerMap);
        QuickMatchItem quickMatchItem = new QuickMatchItem(record.get("Cue"), transliteration, snippetName);
        String qzWithoutGroup = qz.split("_")[0];
        if ((qzWithoutGroup.charAt(qzWithoutGroup.length() - 1) == 'Q')) {
            skill.setQuestion(quickMatchItem);
        } else {
            skill.setAnswer(quickMatchItem);
        }
    }

    private static Map<String, String> getSkillKeyMap(File skillDir) throws IOException {
        // TODO: optimize: move the codes to a singleton's constructor
        Map<String, String> skillKeyMap = new HashMap<>();
        File skillKeyCsv = new File(skillDir, "Skills Key.csv");
        Reader skillKeyCsvReader = new FileReader(skillKeyCsv.getAbsoluteFile());
        CSVParser parser = CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .parse(skillKeyCsvReader);
        for (CSVRecord record : parser) {
            skillKeyMap.put(record.get("Skills Number"), record.get("Skills Name"));
        }
        return skillKeyMap;
    }
}
