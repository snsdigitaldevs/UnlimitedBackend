package com.simonschuster.pimsleur.unlimited.services.bonusPacks;

import com.simonschuster.pimsleur.unlimited.data.dto.bonusPacks.BonusPackInUnit;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import com.simonschuster.pimsleur.unlimited.services.InstallationFileService;
import com.simonschuster.pimsleur.unlimited.utils.bonusPacks.BonusPackUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BonusPacksService {

    private static final Logger logger = LoggerFactory.getLogger(BonusPacksService.class);

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
        if (installationFileList.getResultCode() != EdtResponseCode.RESULT_OK) {
            logger.warn("get bonusPack Url from edt failed, please check your input and try again");
            return null;
        }
        return installationFileList.getBonusPacksUrls();
    }
}
