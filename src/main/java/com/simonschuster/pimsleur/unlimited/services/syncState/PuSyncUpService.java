package com.simonschuster.pimsleur.unlimited.services.syncState;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.syncUp.SyncUpDto;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncUpItem;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.syncUp.SyncUpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class PuSyncUpService {

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ApplicationConfiguration config;

    public long syncUpPUProgress(String customerId, String subUserId,
                                 String productCode, String mediaItemId,
                                 SyncUpDto syncUpDto) throws Exception {

        SyncUpResult syncUpResult = postToEdt(createPostBody(customerId, subUserId, productCode, mediaItemId, syncUpDto),
                config.getProperty("edt.api.syncUpUrl"),
                SyncUpResult.class);
        if (syncUpResult.getResultCode() != 1) {
            throw new Exception("EDT sync up Error");
        }
        return syncUpResult.getResultData().getLastSaveId();
    }

    private HttpEntity<String> createPostBody(String customerId, String subUserId,
                                              String productCode, String mediaItemId,
                                              SyncUpDto syncUpDto) throws JsonProcessingException {

        Map<String, SyncUpItem> edtSyncUpItems = syncUpDto.
                toEdtPUSyncItems(customerId, subUserId, productCode, mediaItemId);
        String edtJsonParameter = mapper.writeValueAsString(edtSyncUpItems);

        String syncUpParameters = format(config.getApiParameter("syncUpParameters"),
                customerId, syncUpDto.getDeviceName(), syncUpDto.getLastSaveId(), edtJsonParameter,
                syncUpDto.getProgress().getLastChangeTimestamp(), syncUpDto.getIdentityVerificationToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(syncUpParameters, headers);
    }
}
