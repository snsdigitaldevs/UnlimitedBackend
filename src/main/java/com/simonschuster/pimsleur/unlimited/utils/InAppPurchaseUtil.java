package com.simonschuster.pimsleur.unlimited.utils;

public class InAppPurchaseUtil {
    private static final String APP_ID_IOS = "com.thoughtworks.pimsleur.unlimited.inapppurchase";
    private static final String APP_ID_ANDROID = "com.thoughtworks.pimsleur.unlimited.qa";
    // TODO: make sure each app_id in iOS and Android

    public static String getAppId(String storeDomain) {
        String appId = "";
        if (storeDomain.contains("android")) {
            appId = APP_ID_ANDROID;
        } else if (storeDomain.contains("ios")) {
            appId = APP_ID_IOS;
        }
        return appId;
    }
}
