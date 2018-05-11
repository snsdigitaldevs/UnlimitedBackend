
package com.simonschuster.pimsleur.unlimited.data.edt.freeLessons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "courseLanguages"
})
public class ResultData {

    @JsonProperty("courseLanguages")
    private Map<String, List<PCMProduct>> courseLanguages;

    @JsonProperty("courseLanguages")
    public Map<String, List<PCMProduct>> getCourseLanguages() {
        return courseLanguages;
    }

    @JsonProperty("courseLanguages")
    public void setCourseLanguages(Map<String, List<PCMProduct>> courseLanguages) {
        this.courseLanguages = courseLanguages;
    }

}
