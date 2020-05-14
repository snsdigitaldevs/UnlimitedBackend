package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.UserAppStateDatum;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class UnlimitedProgressConverter {

    private static final Logger LOG = LoggerFactory.getLogger(UnlimitedProgressConverter.class);
    private static final String COMPLETED = "isCompleted";
    private static final String LAST_PLAYED_DATE = "lastPlayedDate";
    private static final String LAST_PLAYED_HEAD_LOCATION = "lastPlayHeadLocation";
    private static final List<String> keyWordsToExtract = asList(COMPLETED, LAST_PLAYED_DATE, LAST_PLAYED_HEAD_LOCATION);

    public static List<ProgressDTO> UnlimitedSyncStateToDTO(List<UserAppStateDatum> userAppStateData) {
        HashMap<String, Long> currentLastPlayedDateMap = new HashMap<>();
        List<ProgressDTO> result = userAppStateData.stream()
                .filter(UnlimitedProgressConverter::hasKeyWord)
                .collect(Collectors.groupingBy(UserAppStateDatum::idPartOfKey))
                .values().stream()
                .map(group -> getProgressDTO(currentLastPlayedDateMap, group))
                .filter(Objects::nonNull)
                .collect(toList());

        getCurrentForEachSubUser(result, currentLastPlayedDateMap);
        return result;
    }

    private static ProgressDTO getProgressDTO(HashMap<String, Long> currentLastPlayedDateMap,
        List<UserAppStateDatum> group) {
        try {
            String key = group.get(0).getKey();
            //com.ss.models::UserLessonHistory_68277_5baa718586383_9781508222033_4000030#lastPlayedDate
            String[] ids = key.split("#")[0].split("_");

            String subUserID = ids.length <= 5 ? ids[2] : ids[3];
            int mediaItemId = ids.length <= 5 ? Integer.parseInt(ids[4]) : Integer.parseInt(ids[5]);
            String productCode = ids.length <= 5 ? ids[3] : ids[4];
            ProgressDTO progressDTO = new ProgressDTO(mediaItemId, productCode, subUserID, false,
                false);
            group.forEach(
                getUserAppStateDatumConsumer(subUserID, progressDTO, currentLastPlayedDateMap));
            return progressDTO;
        } catch (NumberFormatException e) {
            LOG.error("Progress Convert Failed", e);
        }
        return null;
    }

    private static void getCurrentForEachSubUser(List<ProgressDTO> result, HashMap<String, Long> currentLastPlayedDateMap) {
        for (String subUserID : currentLastPlayedDateMap.keySet()) {
            result.stream()
                    .filter(progress -> progress.getSubUserID().equals(subUserID)
                            && Objects.equals(progress.getLastPlayedDate(), currentLastPlayedDateMap.get(subUserID)))
                    .findFirst()
                    .get()
                    // there is one and only one item after filer
                    .setCurrent(true);
        }
    }

    private static Consumer<UserAppStateDatum> getUserAppStateDatumConsumer(String subUserID, ProgressDTO progressDTO, HashMap<String, Long> currentLastPlayedDateMap) {
        return progress -> {
            switch (progress.getKey().split("#")[1]) {
                case COMPLETED:
                    if (progress.getValue() instanceof Integer) {
                        progressDTO.setCompleted((Integer) progress.getValue() == 1);
                    } else if (progress.getValue() instanceof Boolean) {
                        progressDTO.setCompleted((Boolean) progress.getValue());
                    }
                    break;
                case LAST_PLAYED_DATE:
                    Long value = Long.parseLong(progress.getValue().toString());
                    Long currentLastPlayedDate = currentLastPlayedDateMap.get(subUserID);
                    if (currentLastPlayedDate == null || value > currentLastPlayedDate) {
                        currentLastPlayedDateMap.put(subUserID, value);
                    }
                    progressDTO.setLastPlayedDate(value);
                    break;
                case LAST_PLAYED_HEAD_LOCATION:
                    progressDTO.setLastPlayHeadLocation(Double.valueOf(progress.getValue().toString()));
                    break;
                default:
                    // do nothing
                    break;
            }
        };
    }

    private static boolean hasKeyWord(UserAppStateDatum progress) {
        return keyWordsToExtract.stream().anyMatch((keyWord) -> progress.getKey().contains(keyWord));
    }

}
