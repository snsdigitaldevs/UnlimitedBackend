package com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "level1FullCourseTotalLessons"
})
public class AdditionalProductData {

    @JsonProperty("level1FullCourseTotalLessons")
    private int level1FullCourseTotalLessons;

    @JsonProperty("level1FullCourseTotalLessons")
    public int getLevel1FullCourseTotalLessons() {
        return level1FullCourseTotalLessons;
    }

    @JsonProperty("level1FullCourseTotalLessons")
    public void setLevel1FullCourseTotalLessons(int level1FullCourseTotalLessons) {
        this.level1FullCourseTotalLessons = level1FullCourseTotalLessons;
    }

}