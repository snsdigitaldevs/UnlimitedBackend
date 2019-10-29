package com.simonschuster.pimsleur.unlimited.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

  private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static String toJsonString(Object object) {
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      LOG.error("Parse Object to Json String error!", e);
    }
    return "";
  }

}
