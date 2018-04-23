package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.UserAppStateDatum;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class PCMProgressConverter {
    private static final String AUDIO_TRACK_COMPLETE = "audioTrackComplete";
    private static final String LAST_ACCESS_DATE = "lastAccessDate";
    private static final String LAST_AUDIO_POS_MILLIS = "lastAudioPosMillis";
    private static final int custIdLength = 6;
    private static final int productCodeLength = 13;

    private static List<String> keyWordsToExtract =
            asList(AUDIO_TRACK_COMPLETE, LAST_ACCESS_DATE, LAST_AUDIO_POS_MILLIS);

    public static List<ProgressDTO> edtProgressToProgressDto(List<UserAppStateDatum> edtProgresses) {
        return edtProgresses.stream()
                .filter(PCMProgressConverter::hasKeyWord)
                .collect(Collectors.groupingBy(UserAppStateDatum::idPartOfKey)).values().stream()
                .map(PCMProgressConverter::groupToDTO)
                .collect(toList());
    }

    private static ProgressDTO groupToDTO(List<UserAppStateDatum> group) {
        ProgressDTO progressDTO = new ProgressDTO();
        extractProductAndMediaItem(progressDTO, group.get(0));
        group.forEach(progress -> {
            if (progress.getKey().contains(AUDIO_TRACK_COMPLETE) && (Boolean) progress.getValue()) {
                progressDTO.setCompleted(true);
            }
            if (progress.getKey().contains(LAST_ACCESS_DATE)) {
                progressDTO.setLastPlayedDate((Long) progress.getValue());
            }
            if (progress.getKey().contains(LAST_AUDIO_POS_MILLIS)) {
                progressDTO.setLastPlayHeadLocation((Double) progress.getValue());
            }
        });
        return progressDTO;
    }

    private static void extractProductAndMediaItem(ProgressDTO progressDTO, UserAppStateDatum userAppStateDatum) {
        String combinedIds = userAppStateDatum.getKey().split("_")[1].split("#")[0];
        String productCode = combinedIds.substring(custIdLength, custIdLength + productCodeLength);
        progressDTO.setProductCode(productCode);
        String mediaItem = combinedIds.substring(custIdLength + productCodeLength);
        progressDTO.setMediaItemId(Integer.parseInt(mediaItem));
    }

    private static boolean hasKeyWord(UserAppStateDatum progress) {
        return keyWordsToExtract.stream().anyMatch((keyWord) -> progress.getKey().contains(keyWord));
    }
}
