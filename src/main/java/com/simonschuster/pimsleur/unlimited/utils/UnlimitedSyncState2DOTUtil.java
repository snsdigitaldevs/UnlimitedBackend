package com.simonschuster.pimsleur.unlimited.utils;

import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.ProgressDTO;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnlimitedSyncState2DOTUtil {
    private static Long currentLastPlayedDate;

    public static List<ProgressDTO> UnlimitedSyncState2DOT(SyncState state) {
        List<ProgressDTO> result = new ArrayList<ProgressDTO>();
        try {
            state.getResultData().getUserAppStateData().forEach(datum -> {
                String[] keyArr = datum.getKey().split("#");
                switch (keyArr[1]) {
                    case "isCompleted":
                        getDTO(keyArr[0], result).setCompleted((Integer) datum.getValue() == 1);
                        break;
                    case "lastPlayedDate":
                        Long date = (Long) datum.getValue();
                        ProgressDTO item = getDTO(keyArr[0], result);
                        item.setLastPlayedDate(date);
                        if (currentLastPlayedDate == null || date > currentLastPlayedDate) {
                            item.setCurrent(true);
                            currentLastPlayedDate = date;
                        }
                        break;
                    case "lastPlayHeadLocation":
                        Object unknownTypeValue = datum.getValue();
                        double value;
                        if (unknownTypeValue instanceof Integer) {
                            value = Double.valueOf(unknownTypeValue.toString());
                        } else {
                            value = (double) unknownTypeValue;
                        }
                        getDTO(keyArr[0], result).setLastPlayHeadLocation(value);
                        break;
                    default:
                        // do nothing
                        break;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static ProgressDTO getDTO(String keyStr, List<ProgressDTO> result) {
        String[] keyArr = keyStr.split("_");
        String productCode = keyArr[3];
        Integer mediaItemId = Integer.parseInt(keyArr[4]);
        for (ProgressDTO item : result) {
            if (item.getProductCode().equals(productCode) && item.getMediaItemId().equals(mediaItemId)) {
                return item;
            }
        }
        ProgressDTO newItem = new ProgressDTO();
        newItem.setMediaItemId(mediaItemId);
        newItem.setProductCode(productCode);
        newItem.setCompleted(false);
        newItem.setCurrent(false);
        result.add(newItem);
        return result.get(result.size() - 1);
    }
}
