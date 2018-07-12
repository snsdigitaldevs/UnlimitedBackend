package com.simonschuster.pimsleur.unlimited.services;

import com.simonschuster.pimsleur.unlimited.configs.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppIdService {

    @Autowired
    private ApplicationConfiguration config;

    public String getAppId(String storeDomain) {
        String appId = "pims2.0";
        if (storeDomain.contains("android")) {
            appId = config.getProperty("APP_ID_ANDROID");
        } else if (storeDomain.contains("ios")) {
            appId = config.getProperty("APP_ID_IOS");
        } else if (storeDomain.contains("alexa")) {
            appId = config.getProperty("APP_ID_ALEXA");
        }
        return appId;
    }
}
