package com.simonschuster.pimsleur.unlimited.services;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import com.simonschuster.pimsleur.unlimited.utils.EDTRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class InstallationFileService {
    @Autowired
    private ApplicationConfiguration config;
    @Autowired
    private AppIdService appIdService;

    public InstallationFileList getInstallationFileList(String productCode, String storeDomain) {
        String installationFileListUrl = config.getProperty("edt.api.installationFileListUrl");
        HttpEntity<String> httpEntity =  createPostBody(productCode, storeDomain, "installationFileListParameters");

        return EDTRequestUtil.postToEdt(httpEntity, installationFileListUrl, InstallationFileList.class);
    }

    private HttpEntity<String> createPostBody(String productCode, String storeDomain, String propertyName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(storeDomain);
        return new HttpEntity<>(
                String.format(config.getApiParameter(propertyName), productCode, appId),
                headers);
    }
}
