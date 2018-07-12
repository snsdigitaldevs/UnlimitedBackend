package com.simonschuster.pimsleur.unlimited.services.practices;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
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

    public PracticesUrls getPracticeUrls(String productCode, Object storeDomain) {
        InstallationFileList installationFileList = postToEdt(
                createPostBody(productCode, storeDomain),
                config.getProperty("edt.api.installationFileListUrl"),
                InstallationFileList.class
        );

        return installationFileList.getPracticeUrls();
    }

    private HttpEntity<String> createPostBody(String productCode, Object storeDomain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(
                String.format(config.getApiParameter("installationFileListParameters"), productCode, storeDomain),
                headers);
    }
}
