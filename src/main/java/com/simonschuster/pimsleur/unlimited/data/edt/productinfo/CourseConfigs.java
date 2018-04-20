package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import java.util.List;
import java.util.Map;

public class CourseConfigs {
    private String courseName;
    private String version;
    private String courseLangName;
    private Integer courseDataPresent;
    private Integer hasVirtualProduct;
    private Integer hasKittedProducts;
    private String virtualProductCode;
    private List<String> kittedProductCodes;
    private Integer isMetadataEncrypted;
    private Integer readingFontSize;
    private Integer usesTransliteration;
    private Integer supportsTransliterationMouseovers;
    private ReadingLessonConfig readingLessonConfig;
    private Integer defaultTransliterationMode;
    private List<Map<String, String>> isbnToCourseName;
    private List<Map<String, List<String>>> kittedFileLists;
    private List<CourseLevelDef> courseLevelDefs;

}
