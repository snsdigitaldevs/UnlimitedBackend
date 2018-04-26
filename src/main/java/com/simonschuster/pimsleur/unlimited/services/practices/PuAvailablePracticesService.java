package com.simonschuster.pimsleur.unlimited.services.practices;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import com.simonschuster.pimsleur.unlimited.data.edt.syncState.SyncState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import static com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil.postToEdt;

@Service
public class PuAvailablePracticesService {
    @Autowired
    private ApplicationConfiguration config;

    public PracticesCsvLocations getPracticeCsvLocations(String productCode) {
        InstallationFileList installationFileList = postToEdt(
                createPostBody(productCode),
                config.getProperty("edt.api.installationFileListUrl"),
                InstallationFileList.class
        );

        return installationFileList.getPracticeCsvLocations();
    }

    private HttpEntity<String> createPostBody(String productCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(
                String.format(config.getApiParameter("installationFileListParameters"), productCode),
                headers);
    }
}
