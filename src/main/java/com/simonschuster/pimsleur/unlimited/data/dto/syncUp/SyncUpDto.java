package com.simonschuster.pimsleur.unlimited.data.dto.syncUp;

import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncUpItem;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class SyncUpDto {
    private String deviceName;
    private String identityVerificationToken;
    private Long lastSaveId;
    private SyncUpProgressDto progress;

    private static final String PU_Sync_Key = "com.ss.models::UserLessonHistory_%s_%s_%s_%s#%s";

    public String getDeviceName() {
        return deviceName;
    }

    public String getIdentityVerificationToken() {
        return identityVerificationToken;
    }

    public Long getLastSaveId() {
        return lastSaveId;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setIdentityVerificationToken(String identityVerificationToken) {
        this.identityVerificationToken = identityVerificationToken;
    }

    public void setLastSaveId(Long lastSaveId) {
        this.lastSaveId = lastSaveId;
    }

    public SyncUpProgressDto getProgress() {
        return progress;
    }

    public void setProgress(SyncUpProgressDto progress) {
        this.progress = progress;
    }

    public Map<String, SyncUpItem> toEdtPUSyncItems(String customerId, String subUserId,
                                                    String productCode, String mediaItemId) {
        HashMap<String, SyncUpItem> syncUpItemsMap = new HashMap<>();

        createSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                "lastPlayHeadLocation", this.getProgress().getLastPlayHeadLocation());
        createSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                "furthestPlayHeadLocation", this.getProgress().getFurthestPlayHeadLocation());
        createSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                "lastPlayedDate", this.getProgress().getLastPlayedDate());
        createSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                "lastCompletionDate", this.getProgress().getLastCompletionDate());
        if (this.getProgress().getIsCompleted() != null && this.getProgress().getIsCompleted()) {
            createSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                    "isCompleted", 1L);
        }

        return syncUpItemsMap;
    }

    private void createSyncItem(String customerId, String subUserId,
                                String productCode, String mediaItemId,
                                HashMap<String, SyncUpItem> syncUpItemsMap,
                                String field, Long value) {
        if (value != null) {
            String key = format(PU_Sync_Key, customerId, subUserId, productCode, mediaItemId, field);
            SyncUpItem syncUpItem = new SyncUpItem(value, customerId, "3U", this.getProgress().getLastChangeTimestamp());
            syncUpItemsMap.put(key, syncUpItem);
        }
    }
}
