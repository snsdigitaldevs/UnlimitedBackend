package com.simonschuster.pimsleur.unlimited.services.bonusPacks;

import com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks.BonusPacksInUnit;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import com.simonschuster.pimsleur.unlimited.services.InstallationFileService;
import com.simonschuster.pimsleur.unlimited.utils.bonusPacks.BonusPackUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BonusPacksService {

    @Autowired
    private InstallationFileService installationFileService;


    public List<BonusPacksInUnit> getBonusPacksInUnit(String productCode, String storeDomain) {
        List<BonusPacksInUnit> bonusPacksInUnits = new ArrayList<>();
        String bonusPackFileUrl  = getBonusPacksUrls(productCode, storeDomain);
        if (bonusPackFileUrl != null) {
            bonusPacksInUnits = BonusPackUtil.csvToBonusPacks(bonusPackFileUrl);
        }
        return bonusPacksInUnits;
    }

    private String getBonusPacksUrls(String productCode, String storeDomain) {
        InstallationFileList installationFileList = installationFileService.getInstallationFileList(productCode, storeDomain);

        return installationFileList.getBonusPackFileUrl();
    }
}
