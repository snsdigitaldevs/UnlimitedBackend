package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.UserAppStateDatum;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class PCMProgressConverter {
    private static final String AUDIO_TRACK_COMPLETE = "audioTrackComplete";
    private static final String LAST_ACCESS_DATE = "lastAccessDate";
    private static final String LAST_AUDIO_POS_MILLIS = "lastAudioPosMillis";

    private static final String CURRENT_MEDIA_SET_HISTORY_ID = "currentMediaSetHistoryId";
    private static final String CURRENT_MEDIA_ITEM_HISTORY_ID = "currentMediaItemHistoryId";

    private static final int custIdLength = 6;
    private static final int productCodeLength = 13;

    private static List<String> keyWordsToExtract =
            asList(AUDIO_TRACK_COMPLETE, LAST_ACCESS_DATE, LAST_AUDIO_POS_MILLIS);

    public static List<ProgressDTO> pcmProgressToDto(List<UserAppStateDatum> edtProgresses) {
        String currentMediaItem = findCurrentMediaItem(edtProgresses);
        return edtProgresses.stream()
                .filter(PCMProgressConverter::hasKeyWord)
                .collect(groupingBy(UserAppStateDatum::idPartOfKey)).values().stream()
                .map(group -> groupToDTO(group, currentMediaItem))
                .collect(toList());
    }

    private static String findCurrentMediaItem(List<UserAppStateDatum> edtProgresses) {
        Optional<UserAppStateDatum> currentMediaSet = edtProgresses.stream()
                .filter(progress -> progress.getKey().contains(CURRENT_MEDIA_SET_HISTORY_ID))
                .findFirst();
        if (currentMediaSet.isPresent()) {
            String mediaSetId = currentMediaSet.get().getValue().toString();
            Optional<UserAppStateDatum> currentMediaItem = edtProgresses.stream()
                    .filter(progress -> progress.getKey().contains(mediaSetId) && progress.getKey().contains(CURRENT_MEDIA_ITEM_HISTORY_ID))
                    .findFirst();
            if (currentMediaItem.isPresent()) {
                return currentMediaItem.get().getValue().toString();
            }
        }
        return "currentMediaItemNotFound";
    }

    private static ProgressDTO groupToDTO(List<UserAppStateDatum> group, String currentMediaItem) {
        ProgressDTO progressDTO = new ProgressDTO();
        extractProductAndMediaItem(progressDTO, group.get(0).getKey(), currentMediaItem);
        group.forEach(progress -> {
            if (progress.getKey().contains(AUDIO_TRACK_COMPLETE) && (Boolean) progress.getValue()) {
                progressDTO.setCompleted(true);
            }
            if (progress.getKey().contains(LAST_ACCESS_DATE)) {
                progressDTO.setLastPlayedDate(Long.parseLong(progress.getValue().toString()));
            }
            if (progress.getKey().contains(LAST_AUDIO_POS_MILLIS)) {
                progressDTO.setLastPlayHeadLocation(Double.valueOf(progress.getValue().toString()));
            }
        });
        return progressDTO;
    }

    private static void extractProductAndMediaItem(ProgressDTO progressDTO, String key, String currentMediaItem) {
        if (key.contains(currentMediaItem)) {
            progressDTO.setCurrent(true);
        }
        String combinedIds = key.split("_")[1].split("#")[0];
        String productCode = combinedIds.substring(custIdLength, custIdLength + productCodeLength);
        progressDTO.setProductCode(productCode);
        String mediaItem = combinedIds.substring(custIdLength + productCodeLength);
        progressDTO.setMediaItemId(Integer.parseInt(mediaItem));
    }

    private static boolean hasKeyWord(UserAppStateDatum progress) {
        return keyWordsToExtract.stream().anyMatch((keyWord) -> progress.getKey().contains(keyWord));
    }
}
