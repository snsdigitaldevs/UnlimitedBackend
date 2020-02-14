package com.simonschuster.pimsleur.unlimited.services.practices;

import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import com.simonschuster.pimsleur.unlimited.services.InstallationFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PuAvailablePracticesService {

    @Autowired
    private InstallationFileService installationFileService;

    public PracticesUrls getPracticeUrls(String productCode, String storeDomain) {
        InstallationFileList installationFileList = installationFileService.getInstallationFileList(productCode, storeDomain);

        return installationFileList.collectPracticeUrls();
    }
}
