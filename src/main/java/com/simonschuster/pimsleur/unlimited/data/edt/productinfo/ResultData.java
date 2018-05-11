package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.common.exception.PimsleurException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class ResultData {
    private static final Logger logger = LoggerFactory.getLogger(ResultData.class);
    @JsonProperty("courseConfigs")
    private Map<String, String> courseConfigs;

    @JsonProperty("mediaSets")
    private Map<String, String> mediaSets;

    @JsonProperty("courseConfigs")
    public Map<String, CourseConfig> getCourseConfigs() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, CourseConfig> formattedCourseConfigs = new HashMap<>();
        courseConfigs.forEach((key, value) -> {
            CourseConfig courseConfig = null;
            try {
                courseConfig = mapper.readValue(value, new TypeReference<CourseConfig>() {
                });
            } catch (IOException e) {
                logger.error("Exception when deserialize product info(courseconfig) from EDT, message Detail:" + e.getMessage());
                e.printStackTrace();
                throw new PimsleurException(e.getMessage());
            }
            formattedCourseConfigs.put(key, courseConfig);
        });

        return formattedCourseConfigs;
    }

    @JsonProperty("mediaSets")
    public Map<String, MediaSet> getMediaSets() {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, MediaSet> formattedMediaSets = new HashMap<>();
        mediaSets.forEach((key, value) -> {
            try {
                MediaSet mediaSet = mapper.readerFor(MediaSet.class).readValue(value);
                formattedMediaSets.put(key, mediaSet);
            } catch (IOException e) {
                logger.error("Exception when deserialize product info (mediasets) from EDT");
                e.printStackTrace();
                throw new PimsleurException(e.getMessage());
            }
        });
        return formattedMediaSets;
    }
}
