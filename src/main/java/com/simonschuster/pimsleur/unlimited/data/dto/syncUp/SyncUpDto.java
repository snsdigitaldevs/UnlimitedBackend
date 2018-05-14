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
    private static final String PCM_Sync_Key = "com.edt.models::MediaItemHistory_%s%s%s#%s";
    private static final String PCM_Sync_Media_Item_Key = "com.edt.models::MediaSetHistory_%s%s#%s";
    private static final String PCM_Sync_Media_Set_Key = "com.edt.models::Customer_%s#%s";

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

        createPuSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                "lastPlayHeadLocation", this.getProgress().getLastPlayHeadLocation());
        createPuSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                "furthestPlayHeadLocation", this.getProgress().getFurthestPlayHeadLocation());
        createPuSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                "lastPlayedDate", this.getProgress().getLastPlayedDate());
        createPuSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                "lastCompletionDate", this.getProgress().getLastCompletionDate());
        if (this.getProgress().getIsCompleted() != null && this.getProgress().getIsCompleted()) {
            createPuSyncItem(customerId, subUserId, productCode, mediaItemId, syncUpItemsMap,
                    "isCompleted", 1L);
        }

        return syncUpItemsMap;
    }

    private void createPuSyncItem(String customerId, String subUserId,
                                  String productCode, String mediaItemId,
                                  HashMap<String, SyncUpItem> syncUpItemsMap,
                                  String field, Long value) {
        String key = format(PU_Sync_Key, customerId, subUserId, productCode, mediaItemId, field);
        createSyncItem(customerId, syncUpItemsMap, value, key);
    }

    private void createPcmSyncItem(String customerId,
                                   String productCode, String mediaItemId,
                                   HashMap<String, SyncUpItem> syncUpItemsMap,
                                   String field, Long value) {
        String key = format(PCM_Sync_Key, customerId, productCode, mediaItemId, field);
        createSyncItem(customerId, syncUpItemsMap, value, key);
    }

    private void createPcmSyncItem(String customerId, String productCode,
                                   HashMap<String, SyncUpItem> syncUpItemsMap, String value) {
        String key = format(PCM_Sync_Media_Item_Key, customerId, productCode, "currentMediaItemHistoryId");
        createPcmSyncItem(customerId, syncUpItemsMap, value, key);
    }

    private void createPcmSyncItem(String customerId, String productCode, String mediaItemId,
                                   HashMap<String, SyncUpItem> syncUpItemsMap, Boolean value) {
        if (value != null) {
            String key = format(PCM_Sync_Key, customerId, productCode, mediaItemId, "audioTrackComplete");
            SyncUpItem syncUpItem = new SyncUpItem(value, customerId, "3U", this.getProgress().getLastChangeTimestamp());
            syncUpItemsMap.put(key, syncUpItem);
        }
    }

    private void createPcmSyncItem(String customerId, HashMap<String, SyncUpItem> syncUpItemsMap, String value, String key) {
        SyncUpItem syncUpItem = new SyncUpItem(value, customerId, "3U", this.getProgress().getLastChangeTimestamp());
        syncUpItemsMap.put(key, syncUpItem);
    }

    private void createSyncItem(String customerId, HashMap<String, SyncUpItem> syncUpItemsMap, Long value, String key) {
        if (value != null) {
            SyncUpItem syncUpItem = new SyncUpItem(value, customerId, "3U", this.getProgress().getLastChangeTimestamp());
            syncUpItemsMap.put(key, syncUpItem);
        }
    }

    public Map<String, SyncUpItem> toEdtPcmSyncItems(String customerId, String productCode, String mediaItemId) {
        HashMap<String, SyncUpItem> syncUpItemsMap = new HashMap<>();
        createPcmSyncItem(customerId, productCode, mediaItemId, syncUpItemsMap,
                this.getProgress().getIsCompleted() != null && this.getProgress().getIsCompleted());
        createPcmSyncItem(customerId, productCode, mediaItemId, syncUpItemsMap,
                "lastAccessDate", this.getProgress().getLastPlayedDate());
        createPcmSyncItem(customerId, productCode, mediaItemId, syncUpItemsMap,
                "lastAudioPosMillis", this.getProgress().getLastPlayHeadLocation());
        createPcmSyncItem(customerId, productCode, syncUpItemsMap, customerId + productCode + mediaItemId);
        createPcmSyncItem(customerId, syncUpItemsMap, customerId + productCode
                , format(PCM_Sync_Media_Set_Key, customerId, "currentMediaSetHistoryId"));
        createPcmSyncItem(customerId, syncUpItemsMap, "VIEW_COURSE_DETAILS",
                format(PCM_Sync_Media_Set_Key, customerId, "lastVisitedView"));
        return syncUpItemsMap;
    }
}
