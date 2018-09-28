package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.pcmProgressParserDto;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.UserAppStateDatum;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class PCMProgressConverter {
    private static final String AUDIO_TRACK_COMPLETE = "audioTrackComplete";
    private static final String LAST_ACCESS_DATE = "lastAccessDate";
    private static final String LAST_AUDIO_POS_MILLIS = "lastAudioPosMillis";

    private static final String CURRENT_MEDIA_SET_HISTORY_ID = "currentMediaSetHistoryId";
    private static final String CURRENT_MEDIA_ITEM_HISTORY_ID = "currentMediaItemHistoryId";
    private static final int PRODUCT_CODE_LENGTH = 13;

    private static List<String> keyWordsToExtract =
            asList(AUDIO_TRACK_COMPLETE, LAST_ACCESS_DATE, LAST_AUDIO_POS_MILLIS);

    public static List<ProgressDTO> pcmProgressToDto(List<UserAppStateDatum> edtProgresses) {
        pcmProgressParserDto currentMediaItemProgressDto = findCurrentMediaItem(edtProgresses);
        return edtProgresses.stream()
                .filter(PCMProgressConverter::hasKeyWord)
                .collect(groupingBy(UserAppStateDatum::idPartOfKey)).values().stream()
                .map(group -> groupToDTO(group, currentMediaItemProgressDto))
                .collect(toList());

    }

    private static pcmProgressParserDto findCurrentMediaItem(List<UserAppStateDatum> edtProgresses) {
        Optional<UserAppStateDatum> currentMediaSet = edtProgresses.stream()
                .filter(progress -> progress.getKey().contains(CURRENT_MEDIA_SET_HISTORY_ID))
                .findFirst();
        if (currentMediaSet.isPresent()) {
            String key = currentMediaSet.get().getKey();
            String customerId = key.substring("com.edt.models::Customer_".length(), key.indexOf("#"));
            String mediaSetId = currentMediaSet.get().getValue().toString();
            String productCode = mediaSetId.substring(customerId.length());
            Optional<UserAppStateDatum> currentMediaItem = edtProgresses.stream()
                    .filter(progress -> progress.getKey().contains(mediaSetId) && progress.getKey().contains(CURRENT_MEDIA_ITEM_HISTORY_ID))
                    .findFirst();
            if (currentMediaItem.isPresent()) {
                String mediaItemId = currentMediaItem.get().getValue().toString().substring(mediaSetId.length());
                return new pcmProgressParserDto(customerId, productCode, mediaItemId);
            }
        }
        return null;
    }

    private static ProgressDTO groupToDTO(List<UserAppStateDatum> group, pcmProgressParserDto currentMediaItemProgress) {
        ProgressDTO progressDTO = new ProgressDTO();
        extractProductAndMediaItem(progressDTO, group.get(0).getKey(), currentMediaItemProgress);
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

    private static void extractProductAndMediaItem(ProgressDTO progressDTO, String key, pcmProgressParserDto currentMediaItemProgress) {
        //com.edt.models::MediaItemHistory_17892 9781 4423 1603 4 40339#lastAudioPosMillis
        String s = key.split("#")[0];
        if (currentMediaItemProgress != null) {
            if (key.contains(currentMediaItemProgress.getProductCode()) && key.contains(currentMediaItemProgress.getMediaItem())) {
                progressDTO.setCurrent(true);
            }
            int customerIdIndex = s.indexOf(currentMediaItemProgress.getCustomerId());
            int productCodeIndex = customerIdIndex + currentMediaItemProgress.getCustomerId().length();
            int mediaItemIndex = productCodeIndex + PRODUCT_CODE_LENGTH;
            String code = s.substring(productCodeIndex, mediaItemIndex);
            String mediaItem = s.substring(mediaItemIndex);
            progressDTO.setProductCode(code);
            progressDTO.setMediaItemId(Integer.parseInt(mediaItem));
        }else {
            /*com.edt.models::MediaItemHistory_68277978150821796170464*/
            String[] splitByUnderline = s.split("_");
            if (splitByUnderline.length == 2) {
                String totalCode = splitByUnderline[1];
                String regex = "(\\d{4,})(978\\d{10})(\\d+)";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(totalCode);
                while (m.find()) {
                    String productCode = m.group(2);
                    String mediaItem = m.group(3);
                    progressDTO.setProductCode(productCode);
                    progressDTO.setMediaItemId(Integer.parseInt(mediaItem));
                }
            }
        }
    }


    private static boolean hasKeyWord(UserAppStateDatum progress) {
        boolean keyDoesNotHaveNullString = !progress.getKey().contains("null");
        return keyDoesNotHaveNullString &&
                keyWordsToExtract.stream().anyMatch((keyWord) -> progress.getKey().contains(keyWord));
    }

}
