package com.simonschuster.pimsleur.unlimited.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtil {
    public static String encodeUrl(String prefixWithDomain, String audioUrl) throws UnsupportedEncodingException {
        String urlWithoutPrefix = audioUrl.replace(prefixWithDomain, "");
        String[] split = urlWithoutPrefix.split("/");
        for (int i = 0; i < split.length; i++) {
            split[i] = URLEncoder.encode(split[i], "UTF-8");
        }

        return prefixWithDomain + String.join("/", split);
    }
}
