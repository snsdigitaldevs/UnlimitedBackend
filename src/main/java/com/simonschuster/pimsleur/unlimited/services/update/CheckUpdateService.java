package com.simonschuster.pimsleur.unlimited.services.update;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.update.CheckUpdateDto;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckUpdateService {

    private static String TITLE = "A new version is now available";

    @Autowired
    private ApplicationConfiguration config;

    public CheckUpdateDto checkUpdate(String version, String storeDomain) {
        String updateURL = getUpdateURL(storeDomain);
        String releaseNote = config.getProperty("releaseNote");
        String currentAppVersion = config.getProperty("currentAppVersion");
        String leastSupportedAppVersion = config.getProperty("leastSupportedAppVersion");
        boolean isUpdate = compareVersion(version, currentAppVersion);
        boolean forceUpdate = compareVersion(version, leastSupportedAppVersion);
        return new CheckUpdateDto(releaseNote, isUpdate, forceUpdate, currentAppVersion, updateURL, TITLE);
    }

    public boolean compareVersion(String appVersion, String targetVersion) {
        String[] appNumbers = appVersion.split("\\.");
        String[] targetNumbers = targetVersion.split("\\.");
        int length = appNumbers.length < targetNumbers.length ? appNumbers.length : targetNumbers.length;
        for (int i = 0; i < length; i++) {
            if (Integer.parseInt(appNumbers[i]) < Integer.parseInt(targetNumbers[i])) {
                return true;
            }
        }
        return false;
    }

    private String getUpdateURL(String storeDomain) {
        if ("android_inapp".equals(storeDomain)) {
            return config.getProperty("androidUpdateURL");
        }

        if ("ios_inapp".equals(storeDomain)) {
            return config.getProperty("iosUpdateURL");
        }

        return StringUtil.EMPTY_STRING;
    }
}
