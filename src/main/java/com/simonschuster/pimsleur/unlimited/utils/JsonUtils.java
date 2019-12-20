package com.simonschuster.pimsleur.unlimited.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJsonString(Object object) {
        if (object != null) {
            try {
                return OBJECT_MAPPER.writeValueAsString(object);
            } catch (Exception e) {
                LOG.error("Parse Object to Json String error!", e);
            }
        }
        return "";
    }

}
