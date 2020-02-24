package com.simonschuster.pimsleur.unlimited.utils.bonusPacks;

import com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks.BonusCard;
import com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks.BonusPackInUnit;
import com.simonschuster.pimsleur.unlimited.services.bonusPacks.BonusPacksUrls;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.*;
import static com.simonschuster.pimsleur.unlimited.utils.dict.FlashCardBonusCardHeaders.*;
import static com.simonschuster.pimsleur.unlimited.utils.dict.FlashCardBonusCardHeaders.HEADER_MP3_SNIPPET_FILE_NAME;
import static com.simonschuster.pimsleur.unlimited.utils.dict.FlashCardBonusCardHeaders.HEADER_SNIPPET_NAME;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.groupingBy;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class BonusPackUtil {

    public static List<BonusPackInUnit> csvToBonusPacks(BonusPacksUrls bonusPacksUrls) throws IOException {
        List<BonusPackInUnit> bonusPacksInUnit = new ArrayList<>();

        String bonusPackFileUrl = bonusPacksUrls.getBonusPacksFileUrl();
        String reviewAudioBaseUrl = bonusPacksUrls.getReviewAudioBaseUrl();

        List<CSVRecord> csvRecords = urlToCsv(bonusPackFileUrl);
        if (csvRecords.size() != 0 ) {
            Map<String, String> csvRecordHeader = convertToUpperCSVRecordHeaderMap(csvRecords.get(0));
            String packGroupNumberKey = findRealHeaderName(csvRecordHeader, HEADER_FP);

            bonusPacksInUnit = csvRecords.stream()
                    .collect(groupingBy(csvRecord -> getUnitNumString(csvRecord, packGroupNumberKey)))
                    .entrySet().stream()
                    .map(group -> groupToBonusPacksInUnit(group, csvRecordHeader, reviewAudioBaseUrl))
                    .collect(Collectors.toList());
        }
        return bonusPacksInUnit;
    }

    private static BonusPackInUnit groupToBonusPacksInUnit(Map.Entry<String, List<CSVRecord>> group, Map<String, String> csvRecordHeader, String reviewAudioBaseUrl) {
        String packGroupNumber = group.getKey().trim();
        BonusPackInUnit bonusPackInUnit = new BonusPackInUnit();
        if (isNumeric(packGroupNumber)) {
            List<BonusCard> bonusPack = group.getValue().stream()
                    .map(csvRecord -> csvRecordToBonusCard(csvRecord, csvRecordHeader, reviewAudioBaseUrl))
                    .collect(Collectors.toList());
            bonusPackInUnit.setPackGroupNumber(parseInt(packGroupNumber));
            bonusPackInUnit.setBonusPack(bonusPack);
        }
        return bonusPackInUnit;
}

    private static BonusCard csvRecordToBonusCard(CSVRecord csvRecord, Map<String, String> csvRecordHeader, String reviewAudioBaseUrl) {
        String transliterationKey = findRealHeaderName(csvRecordHeader, HEADER_TRANSLITERATION);
        String translationKey = findRealHeaderName(csvRecordHeader, HEADER_ENGLISH_TRANSLATION, HEADER_TRANSLATION);
        String languageKey = findRealHeaderName(csvRecordHeader, HEADER_LANGUAGE);
        String mp3FileKey = findRealHeaderName(csvRecordHeader, HEADER_MP3_SNIPPET_FILE_NAME, HEADER_SNIPPET_NAME, HEADER_MP3_SNIPPET_FIRST_LITTER_CAPITALIZED);

        return new BonusCard(getFromCsv(transliterationKey, csvRecord), getFromCsv(translationKey, csvRecord),
                            getFromCsv(languageKey, csvRecord), getMp3FileName(mp3FileKey, csvRecord, reviewAudioBaseUrl));
    }

    private static String getMp3FileName(String mp3FileKey, CSVRecord csvRecord,String reviewAudioBaseUrl) {
        String mp3FileName = getFromCsv(mp3FileKey, csvRecord);
        if (mp3FileName != null) {
            return reviewAudioBaseUrl.concat(getFromCsv(mp3FileKey, csvRecord));
        }
        return null;
    }
}
