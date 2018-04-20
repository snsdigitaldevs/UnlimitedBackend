package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class ResultData {
    @JsonProperty("courseConfigs")
    private Map<String, String> courseConfigs;

    @JsonProperty("mediaSets")
    private MediaSets mediaSets;

    @JsonProperty("courseConfigs")
    public Map<String, String> getCourseConfigs() {
        return courseConfigs;
    }

    @JsonProperty("courseConfigs")
    public void setCourseConfigs(Map<String, String> courseConfigs) {
        this.courseConfigs = courseConfigs;
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
