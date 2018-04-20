package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseConfigs {
//    private String courseName;
//    private String version;
//    private String courseLangName;
//    private Integer courseDataPresent;
//    private Integer hasVirtualProduct;
//    private Integer hasKittedProducts;
//    private String virtualProductCode;
//    private List<String> kittedProductCodes;
//    private Integer isMetadataEncrypted;
//    private Integer readingFontSize;
//    private Integer usesTransliteration;
//    private Integer supportsTransliterationMouseovers;
//    private ReadingLessonConfig readingLessonConfig;
//    private Integer defaultTransliterationMode;
//    private List<Map<String, String>> isbnToCourseName;
//    private List<Map<String, List<String>>> kittedFileLists;
//    private List<CourseLevelDef> courseLevelDefs;

    //todo: make this field key to variable
    //todo: convert string result of this field to java object
    @JsonProperty("Mandarin_Chinese")
    private String mandarinCourseInfo;

    public String getMandarinCourseInfo() {
        return mandarinCourseInfo;
    }

    public void setMandarinCourseInfo(String mandarinCourseInfo) {
        this.mandarinCourseInfo = mandarinCourseInfo;
    }
}
