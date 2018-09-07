package com.simonschuster.pimsleur.unlimited.services.practices;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import com.simonschuster.pimsleur.unlimited.services.AppIdService;
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
    @Autowired
    private AppIdService appIdService;

    public PracticesUrls getPracticeUrls(String productCode, String storeDomain) {
        InstallationFileList installationFileList = postToEdt(
                createPostBody(productCode, storeDomain),
                config.getProperty("edt.api.installationFileListUrl"),
                InstallationFileList.class
        );

        return installationFileList.getPracticeUrls();
//        PracticesUrls urls = new PracticesUrls();
//        urls.setReviewAudioBaseUrl("https://install.pimsleurunlimited.com/staging_n/mobile/french/French I 2018/audio/9781508261131_REVIEW_AUDIO_SNIPPETS/");
//        urls.setFlashCardUrl("https://install.pimsleurunlimited.com/staging_n/mobile/french/French I 2018/metadata/timecode/9781508261131_French_1_FC.csv");
//        urls.setQuickMatchUrl("https://install.pimsleurunlimited.com/staging_n/mobile/french/French I 2018/metadata/timecode/9781508261131_French_1_QZ.csv");
//        urls.setReadingUrl("https://install.pimsleurunlimited.com/staging_n/mobile/french/French I 2018/metadata/timecode/9781508261131_French_1_RL.csv");
//        urls.setSpeakEasyUrl("https://install.pimsleurunlimited.com/staging_n/mobile/french/French I 2018/metadata/timecode/9781508261131_French_1_VC.csv");
//        return urls;
    }

    private HttpEntity<String> createPostBody(String productCode, String storeDomain) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String appId = appIdService.getAppId(storeDomain);
        return new HttpEntity<>(
                String.format(config.getApiParameter("installationFileListParameters"), productCode, appId),
                headers);
    }
}
