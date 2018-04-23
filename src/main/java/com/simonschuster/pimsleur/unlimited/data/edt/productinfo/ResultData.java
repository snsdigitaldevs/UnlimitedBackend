package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultData {
    @JsonProperty("courseConfigs")
    private Map<String, String> courseConfigs;

    @JsonProperty("mediaSets")
    private Map<String, String> mediaSets;

    @JsonProperty("courseConfigs")
    public Map<String, CourseConfig> getCourseConfigs() {
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, CourseConfig> formattedCourseConfigs = new HashMap<>();
        courseConfigs.forEach((key, value) -> {
            CourseConfig courseConfig = null;
            try {
                courseConfig = mapper.readValue(value, new TypeReference<CourseConfig>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
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
                e.printStackTrace();
            }
        });
        return formattedMediaSets;
    }
}
