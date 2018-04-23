package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.UserAppStateDatum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class UnlimitedProgressConverter {
    private static Long currentLastPlayedDate;
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
                        ProgressDTO progressDTO = new ProgressDTO(Integer.parseInt(ids[4]), ids[3], ids[2], false, false);
                        group.forEach(progress -> {
                            switch (progress.getKey().split("#")[1]) {
                                case COMPLETED:
                                    progressDTO.setCompleted((Integer) progress.getValue() == 1);
                                    break;
                                case LAST_PLAYED_DATE:
                                    Long value = (Long) progress.getValue();
                                    if (currentLastPlayedDate == null || value < currentLastPlayedDate) {
                                        currentLastPlayedDate = value;
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
            for (ProgressDTO progress : result) {
                if (progress.getLastPlayedDate() == currentLastPlayedDate) {
                    // do not use "equals" instead of "=="
                    progress.setCurrent(true);
                    break;
                }
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
