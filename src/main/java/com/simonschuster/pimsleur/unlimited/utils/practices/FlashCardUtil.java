package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.FlashCard;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesUrls;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit.createWithFlashCards;
import static com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil.*;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class FlashCardUtil {

    public static List<PracticesInUnit> csvToFlashCards(PracticesUrls practicesUrls) throws IOException {
        if (practicesUrls.getFlashCardUrl() == null) {
            return emptyList();
        }

        List<CSVRecord> csvRecords = urlToCsv(practicesUrls.getFlashCardUrl());

        String unitNumKey = findRealHeaderName(csvRecords.get(0), "Unit Num");
        String transliterationKey = findRealHeaderName(csvRecords.get(0), "Transliteration");
        String translationKey = findRealHeaderName(csvRecords.get(0), "English Translation");
        String LanguageKey = findRealHeaderName(csvRecords.get(0), "Language");
        String mp3FileKey = findRealHeaderName(csvRecords.get(0), "MP3 snippet file name");

        return csvRecords.stream()
                .collect(groupingBy(csvRecord -> getUnitNumString(csvRecord, unitNumKey)))
                .entrySet().stream()
                .map(group -> {
                    String unitNumString = group.getKey();
                    if (isNumeric(unitNumString)) {
                        return groupToUnit(unitNumString,
                                transliterationKey, translationKey,
                                LanguageKey, mp3FileKey,
                                practicesUrls.getFlashCardAudioBaseUrl(),
                                practicesUrls.getFlashCardAudioBaseFileName(),
                                group);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted(comparingInt(PracticesInUnit::getUnitNumber))
                .collect(toList());
    }

    private static PracticesInUnit groupToUnit(String unitNumString,
                                               String transliterationKey, String translationKey,
                                               String LanguageKey, String mp3FileKey,
                                               String flashCardAudioBaseUrl,
                                               String flashCardAudioBaseFileName,
                                               Map.Entry<String, List<CSVRecord>> group) {
        int unitNumber = parseInt(unitNumString);

        List<FlashCard> speakEasies = group.getValue().stream()
                .map(csvRecord -> new FlashCard(
                        getFromCsv(transliterationKey, csvRecord),
                        getFromCsv(translationKey, csvRecord),
                        getFromCsv(LanguageKey, csvRecord),
                        flashCardAudioBaseUrl + getMp3FileName(unitNumber, mp3FileKey, flashCardAudioBaseFileName, csvRecord)))
                .collect(toList());

        return createWithFlashCards(unitNumber, speakEasies);
    }

    private static String getMp3FileName(int unitNumber, String mp3FileKey,
                                         String flashCardAudioBaseFileName, CSVRecord csvRecord) {
        // We expect mp3Filekey = 'MP3 snippet file name', however, sometimes, the column name is 'Snippet Name'
         String mp3FileName = getFromCsv(mp3FileKey, csvRecord);
        if (mp3FileName.length() != 0) {
            return mp3FileName;
        } else {
            return constructMp3FileNameByPattern(unitNumber, flashCardAudioBaseFileName, csvRecord);
        }
    }

    private static String constructMp3FileNameByPattern(int unitNumber,
                                                        String flashCardAudioBaseFileName,
                                                        CSVRecord csvRecord) {
        String flashCardNumberKey = findRealHeaderName(csvRecord, "Flash Card #");

        return flashCardAudioBaseFileName
                .concat("_U")
                .concat(valueOf(unitNumber))
                .concat("_")
                .concat(csvRecord.get(flashCardNumberKey))
                .concat(".mp3");
    }

}
