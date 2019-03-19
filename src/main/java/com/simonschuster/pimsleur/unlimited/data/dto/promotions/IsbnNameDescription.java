package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Base Course Type",
        "ISBN",
        "Course name - Upsell Card & Library",
        "Course name - Learn Page",
        "Course Description - Upsell Card",
        "Other format 1 (Upsell) ISBN",
        "Other format 2 (Upgrade) ISBN",
        "Other format 3 (DVD) ISBN"
})
public class IsbnNameDescription {

    @JsonProperty("Base Course Type")
    private String baseCourseType;
    @JsonProperty("ISBN")
    private String iSBN;
    @JsonProperty("Course name - Upsell Card & Library")
    private String inAppDisplayName;
    @JsonProperty("Course name - Learn Page")
    private String learnPageCourseName;
    @JsonProperty("Course Description - Upsell Card")
    private String inAppDescription;
    @JsonProperty("Other format 1 (Upsell) ISBN")
    private String otherFormat1ISBN;
    @JsonProperty("Other format 2 (Upgrade) ISBN")
    private String otherFormat2ISBN;
    @JsonProperty("Other format 3 (DVD) ISBN")
    private String otherFormat3ISBN;


    public String getISBN() {
        return iSBN;
    }

    public String getInAppDisplayName() {
        return inAppDisplayName;
    }

    public String getInAppDescription() {
        return inAppDescription;
    }

    public String getLearnPageCourseName() {
        return learnPageCourseName;
    }

    public void setLearnPageCourseName(String learnPageCourseName) {
        this.learnPageCourseName = learnPageCourseName;
    }

    public String getOtherFormat1ISBN() {
        return otherFormat1ISBN;
    }

    public String getOtherFormat2ISBN() {
        return otherFormat2ISBN;
    }

    public String getOtherFormat3ISBN() {
        return otherFormat3ISBN;
    }

    public String getBaseCourseType() {
        return baseCourseType;
    }

    public List<String> getAllFormats() {
        return Stream
                .of(this.getISBN(),
                        this.getOtherFormat1ISBN(),
                        this.getOtherFormat2ISBN(),
                        this.getOtherFormat3ISBN())
                .filter(isbn -> isbn.length() > 0)
                .collect(toList());
    }

    public boolean matches(String isbn) {
        return getAllFormats().stream()
                .anyMatch(oneFormat -> oneFormat.equals(isbn));
    }


}
