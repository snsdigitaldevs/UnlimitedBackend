package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.UserAppStateDatum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class UnlimitedProgressConverter {
    private static HashMap<String, Long> currentLastPlayedDateMap = new HashMap<String, Long>();
    private static final String COMPLETED = "isCompleted";
    private static final String LAST_PLAYED_DATE = "lastPlayedDate";
    private static final String LAST_PLAYED_HEAD_LOCATION = "lastPlayHeadLocation";
    private static final List<String> keyWordsToExtract =
            asList(COMPLETED, LAST_PLAYED_DATE, LAST_PLAYED_HEAD_LOCATION);

    public static List<ProgressDTO> UnlimitedSyncState2DOT(SyncState state) {
        List<ProgressDTO> result = new ArrayList<ProgressDTO>();
        try {
            result = state.getResultData().getUserAppStateData().stream()
                    .filter(UnlimitedProgressConverter::hasKeyWord)
                    .collect(Collectors.groupingBy(UserAppStateDatum::idPartOfKey))
                    .values().stream()
                    .map(group -> {
                        String[] ids = group.get(0).getKey().split("#")[0].split("_");
                        String subUserID = ids[2];
                        ProgressDTO progressDTO = new ProgressDTO(Integer.parseInt(ids[4]), ids[3], subUserID, false, false);
                        group.forEach(progress -> {
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
                                    break;
                            }
                        });
                        return progressDTO;
                    })
                    .collect(toList());
            for (String subUserID : currentLastPlayedDateMap.keySet()) {
                result.stream()
                        .filter(progress -> progress.getSubUserID().equals(subUserID)
                                && progress.getLastPlayedDate() == currentLastPlayedDateMap.get(subUserID))
                        // do not use "equals" instead of "=="
                        .findFirst()
                        .orElse(new ProgressDTO())
                        .setCurrent(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static boolean hasKeyWord(UserAppStateDatum progress) {
        return keyWordsToExtract.stream().anyMatch((keyWord) -> progress.getKey().contains(keyWord));
    }
}
