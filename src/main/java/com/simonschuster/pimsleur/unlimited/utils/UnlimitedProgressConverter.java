package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.UserAppStateDatum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class UnlimitedProgressConverter {
    private static HashMap<String, Long> currentLastPlayedDateMap = new HashMap<String, Long>();
    private static final String COMPLETED = "isCompleted";
    private static final String LAST_PLAYED_DATE = "lastPlayedDate";
    private static final String LAST_PLAYED_HEAD_LOCATION = "lastPlayHeadLocation";
    private static final List<String> keyWordsToExtract = asList(COMPLETED, LAST_PLAYED_DATE, LAST_PLAYED_HEAD_LOCATION);

    public static List<ProgressDTO> UnlimitedSyncStateToDTO(List<UserAppStateDatum> userAppStateData) {
        List<ProgressDTO> result = userAppStateData.stream()
                .filter(UnlimitedProgressConverter::hasKeyWord)
                .collect(Collectors.groupingBy(UserAppStateDatum::idPartOfKey))
                .values().stream()
                .map(group -> {
                    String[] ids = group.get(0).getKey().split("#")[0].split("_");
                    String subUserID = ids[2];
                    ProgressDTO progressDTO = new ProgressDTO(Integer.parseInt(ids[4]), ids[3], subUserID, false, false);
                    group.forEach(getUserAppStateDatumConsumer(subUserID, progressDTO));
                    return progressDTO;
                })
                .collect(toList());

        getCurrentForEachSubUser(result);
        return result;
    }

    private static void getCurrentForEachSubUser(List<ProgressDTO> result) {
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

    private static Consumer<UserAppStateDatum> getUserAppStateDatumConsumer(String subUserID, ProgressDTO progressDTO) {
        return progress -> {
            switch (progress.getKey().split("#")[1]) {
                case COMPLETED:
                    progressDTO.setCompleted((Integer) progress.getValue() == 1);
                    break;
                case LAST_PLAYED_DATE:
                    Long value = (Long) progress.getValue();
                    Long currentLastPlayedDate = currentLastPlayedDateMap.get(subUserID);
                    if (currentLastPlayedDate == null || value < currentLastPlayedDate) {
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
