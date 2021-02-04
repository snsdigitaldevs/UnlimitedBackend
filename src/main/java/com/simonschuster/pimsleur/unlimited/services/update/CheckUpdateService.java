package com.simonschuster.pimsleur.unlimited.services.update;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import com.simonschuster.pimsleur.unlimited.data.dto.update.CheckUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckUpdateService {
    @Autowired
    private ApplicationConfiguration config;

    public CheckUpdateDto checkUpdate(Double version) {
        String releaseNote = config.getProperty("releaseNote");
        Double currentAppVersion = Double.valueOf(config.getProperty("currentAppVersion"));
        Double leastSupportedAppVersion = Double.valueOf(config.getProperty("leastSupportedAppVersion"));
        boolean isUpdate = version < currentAppVersion;
        boolean forceUpdate = version < leastSupportedAppVersion;
        return new CheckUpdateDto(releaseNote, isUpdate, forceUpdate, currentAppVersion);
    }
}
