package com.simonschuster.pimsleur.unlimited.services.syncState;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.AggregatedSyncState;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

//todo: is kelly k going to combine this api as well????
@Service
public class EDTSyncStateService {
    @Autowired
    private ApplicationConfiguration config;

    public AggregatedSyncState getSyncStates(Integer customerId, String token) {
        return new AggregatedSyncState(
                postToEdt(createPostBody(customerId, token,
                        config.getApiParameter("unlimitedDomain")),
                        config.getProperty("edt.api.syncStateApiUrl"), SyncState.class),
                postToEdt(createPostBody(customerId, token,
                        config.getApiParameter("pcmDomain")),
                        config.getProperty("edt.api.syncStateApiUrl"), SyncState.class));
    }

    private HttpEntity<String> createPostBody(Integer customerId, String token, String domain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(
                String.format(config.getApiParameter("syncStateParameters"), customerId, token, domain),
                headers);
    }
}
