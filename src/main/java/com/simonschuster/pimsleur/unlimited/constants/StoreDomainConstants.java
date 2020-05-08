package com.simonschuster.pimsleur.unlimited.constants;

import com.google.common.collect.Sets;
import java.util.Set;

public class StoreDomainConstants {

    public static final String ANDROID_IN_APP = "android_inapp";
    public static final String IOS_IN_APP = "ios_inapp";
    public static final String ALEXA_STORE_DOMAIN = "alexa";
    public static final Set<String> MOBILE_DOMAIN = Sets.newHashSet(ANDROID_IN_APP, IOS_IN_APP);

}
