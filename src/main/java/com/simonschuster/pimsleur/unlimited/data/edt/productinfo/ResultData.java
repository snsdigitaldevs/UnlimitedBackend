package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultData {
    @JsonProperty("courseConfigs")
    private CourseConfigs courseConfigs;

    @JsonProperty("mediaSets")
    private MediaSets mediaSets;

    @JsonProperty("courseConfigs")
    public CourseConfigs getCourseConfigs() {
        return courseConfigs;
    }

    @JsonProperty("courseConfigs")
    public void setCourseConfigs(CourseConfigs courseConfigs) {
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
