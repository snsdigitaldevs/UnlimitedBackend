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
    private MediaSets mediaSets;

    @JsonProperty("courseConfigs")
    public Map<String, CourseConfig> getCourseConfigs() {
        ObjectMapper mapper = new ObjectMapper();

        //todo: handle several courses with related keys.

        final String[] courseNames = new String[1];
        final String[] courseConfigStringValues = new String[1];
        courseConfigs.forEach((key, value) -> {
            courseNames[0] = key;
            courseConfigStringValues[0] = value;
        });
        CourseConfig courseConfig = null;
        try {
            courseConfig = mapper.readValue(courseConfigStringValues[0], new TypeReference<CourseConfig>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, CourseConfig> formattedCourseConfigs = new HashMap<>();
        formattedCourseConfigs.put(courseNames[0], courseConfig);

        return formattedCourseConfigs;
    }

    @JsonProperty("mediaSets")
    public MediaSets getMediaSets() {
        return mediaSets;
    }

    @JsonProperty("mediaSets")
    public void setMediaSets(MediaSets mediaSets) {
        this.mediaSets = mediaSets;
    }
}
