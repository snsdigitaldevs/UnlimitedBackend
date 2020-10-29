package com.simonschuster.pimsleur.unlimited.services.practices;

import com.simonschuster.pimsleur.unlimited.constants.CommonConstants;
import com.simonschuster.pimsleur.unlimited.constants.StoreDomainConstants;
import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.InstallationFileList;
import com.simonschuster.pimsleur.unlimited.services.InstallationFileService;
import com.simonschuster.pimsleur.unlimited.utils.UnlimitedPracticeUtil;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuAvailablePracticesService {

    @Autowired
    private InstallationFileService installationFileService;

    public PracticesUrls getPracticeUrls(String productCode, String storeDomain) {
        InstallationFileList installationFileList = installationFileService.getInstallationFileList(productCode, storeDomain);

        return installationFileList.collectPracticeUrls();
    }

    public void handleForArabic(String productCode, String storeDomain,
                                List<PracticesInUnit> allPracticesInUnits) {
        if (CommonConstants.ARABIC_PU_ISBN.contains(productCode)) {
            if (StringUtils.equalsIgnoreCase(StoreDomainConstants.WEB_DOMAIN, storeDomain)) {
                movePeriodToLeftForArabic(allPracticesInUnits);
            }
        }
    }

    private void movePeriodToLeftForArabic(List<PracticesInUnit> allPracticesInUnits) {
        allPracticesInUnits.forEach(practicesInUnit -> {
            practicesInUnit.getQuickMatches().forEach(quickMatch -> {
                quickMatch.getAnswer().setCue(
                        UnlimitedPracticeUtil.moveEndToLeftIfNeed(quickMatch.getAnswer().getCue()));
            });
            practicesInUnit.getFlashCards().forEach(flashCard -> {
                flashCard.setLanguage(UnlimitedPracticeUtil.moveEndToLeftIfNeed(flashCard.getLanguage()));
            });
            practicesInUnit.getSpeakEasies().forEach(speakEasy -> {
                speakEasy.setNativeText(UnlimitedPracticeUtil.moveEndToLeftIfNeed(speakEasy.getNativeText()));
            });
            practicesInUnit.getReadings().forEach(reading -> {
                reading.setNativeText(UnlimitedPracticeUtil.moveEndToLeftIfNeed(reading.getNativeText()));
            });
        });
    }
}
