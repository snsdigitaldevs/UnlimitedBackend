package com.simonschuster.pimsleur.unlimited.services.bonusPacks;

import com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks.BonusPackInUnit;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import com.simonschuster.pimsleur.unlimited.services.InstallationFileService;
import com.simonschuster.pimsleur.unlimited.utils.bonusPacks.BonusPackUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BonusPacksService {

    @Autowired
    private InstallationFileService installationFileService;


    public List<BonusPackInUnit> getBonusPacksInUnit(String productCode, String storeDomain) throws IOException {
        List<BonusPackInUnit> bonusPackInUnits = new ArrayList<>();
        BonusPacksUrls bonusPackFileUrls  = getBonusPacksUrls(productCode, storeDomain);
        if (bonusPackFileUrls != null) {
            bonusPackInUnits = BonusPackUtil.csvToBonusPacks(bonusPackFileUrls);
        }
        return bonusPackInUnits;
    }

    private BonusPacksUrls getBonusPacksUrls(String productCode, String storeDomain) {
        InstallationFileList installationFileList = installationFileService.getInstallationFileList(productCode, storeDomain);

        return installationFileList.getBonusPacksUrls();
    }
}
