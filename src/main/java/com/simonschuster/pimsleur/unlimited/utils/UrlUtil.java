package com.simonschuster.pimsleur.unlimited.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtil {

    private static final Logger LOG = LoggerFactory.getLogger(UrlUtil.class);

    public static String encodeUrl(String prefixWithDomain, String audioUrl) {
        String urlWithoutPrefix = audioUrl.replace(prefixWithDomain, "");
        return prefixWithDomain + encodeUrl(urlWithoutPrefix);
    }

    public static String encodeUrl(String audioUrl) {
        try {
            String[] split = audioUrl.split("/");
            for (int i = 0; i < split.length; i++) {
                split[i] = URLEncoder.encode(split[i], "UTF-8").replace("+", "%20");
            }
            return String.join("/", split);
        } catch (UnsupportedEncodingException e) {
            LOG.error("{} encoder error", audioUrl, e);
        }
        return audioUrl;
    }
}
