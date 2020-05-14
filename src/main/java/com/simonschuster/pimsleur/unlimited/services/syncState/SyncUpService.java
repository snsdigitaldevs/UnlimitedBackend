package com.simonschuster.pimsleur.unlimited.services.syncState;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.common.exception.ParamInvalidException;
import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.syncUp.SyncUpDto;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncUpItem;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.syncUp.SyncUpResult;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.simonschuster.pimsleur.unlimited.constants.CommonConstants.UNDEFINED;
import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;
import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class SyncUpService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    public long syncUpPUProgress(String customerId, String subUserId,
                                 String productCode, String mediaItemId,
                                 SyncUpDto syncUpDto) throws Exception {
        checkParam(customerId, productCode, mediaItemId);
        HttpEntity<String> puPostBody = createPuPostBody(customerId, subUserId, productCode, mediaItemId, syncUpDto);
        SyncUpResult syncUpResult = postToEdt(puPostBody,
                config.getProperty("edt.api.syncUpUrl"),
                SyncUpResult.class);
        if (syncUpResult.getResultCode() != 1) {
            throw new Exception("[ERROR] EDT sync up PUProgress Error:" + puPostBody.getBody() );
        }
        return syncUpResult.getResultData().getLastSaveId();
    }

    private void checkParam(String customerId, String productCode, String mediaItemId) {
        if (UNDEFINED.equals(customerId)) {
            throw new ParamInvalidException("Invalid Param customerId" + customerId);
        }
        if (UNDEFINED.equals(productCode)) {
            throw new ParamInvalidException("Invalid Param productCode" + productCode);
        }
        if (UNDEFINED.equals(mediaItemId)) {
            throw new ParamInvalidException("Invalid Param mediaItemId" + mediaItemId);
        }
    }

    public long syncUpPcmProgress(String customerId, String productCode,
                                  String mediaItemId, SyncUpDto syncUpDto) throws Exception {
        checkParam(customerId, productCode, mediaItemId);
        HttpEntity<String> pcmPostBody = createPcmPostBody(customerId, productCode, mediaItemId, syncUpDto);
        SyncUpResult syncUpResult = postToEdt(pcmPostBody,
                config.getProperty("edt.api.syncUpUrl"),
                SyncUpResult.class);
        if (syncUpResult.getResultCode() != 1) {
            throw new Exception("[ERROR] EDT sync up PcmProgress Error:" + pcmPostBody.getBody());
        }
        return syncUpResult.getResultData().getLastSaveId();
    }

    private HttpEntity<String> createPuPostBody(String customerId, String subUserId,
                                                String productCode, String mediaItemId,
                                                SyncUpDto syncUpDto) throws JsonProcessingException {

        Map<String, SyncUpItem> edtSyncUpItems = syncUpDto.
                toEdtPUSyncItems(customerId, subUserId, productCode, mediaItemId);
        return createPostBody(customerId, syncUpDto, edtSyncUpItems, "ss_pu");

    }

    private HttpEntity<String> createPcmPostBody(String customerId, String productCode, String mediaItemId, SyncUpDto syncUpDto) throws JsonProcessingException {

        Map<String, SyncUpItem> edtSyncUpItems = syncUpDto.
                toEdtPcmSyncItems(customerId, productCode, mediaItemId);
        return createPostBody(customerId, syncUpDto, edtSyncUpItems, "");
    }


    private HttpEntity<String> createPostBody(String customerId, SyncUpDto syncUpDto,
                                              Map<String, SyncUpItem> edtSyncUpItems, String storeDomain)
            throws JsonProcessingException {
        String edtJsonParameter = mapper.writeValueAsString(edtSyncUpItems);

        String syncUpParameters = format(config.getApiParameter("syncUpParameters"),
                customerId, syncUpDto.getDeviceName(), syncUpDto.getLastSaveId(), edtJsonParameter,
                syncUpDto.getProgress().getLastChangeTimestamp(), syncUpDto.getIdentityVerificationToken(), storeDomain,
                appIdService.getAppId(storeDomain));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(syncUpParameters, headers);
    }
}
