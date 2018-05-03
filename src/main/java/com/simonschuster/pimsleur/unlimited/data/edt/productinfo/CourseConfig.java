package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class CourseConfig {
    @JsonProperty("version")
    private String version;
    @JsonProperty("courseLangName")
    private String courseLangName;
    @JsonProperty("courseDataPresent")
    private Integer courseDataPresent;
    @JsonProperty("hasVirtualProduct")
    private Integer hasVirtualProduct;
    @JsonProperty("hasKittedProducts")
    private Integer hasKittedProducts;
    @JsonProperty("virtualProductCode")
    private String virtualProductCode;
    @JsonProperty("kittedProductCodes")
    private List<String> kittedProductCodes;
    @JsonProperty("isMetadataEncrypted")
    private Integer isMetadataEncrypted;
    @JsonProperty("readingFontSize")
    private Integer readingFontSize;
    @JsonProperty("usesTransliteration")
    private Integer usesTransliteration;
    @JsonProperty("supportsTransliterationMouseovers")
    private Integer supportsTransliterationMouseovers;
    @JsonProperty("readingLessonConfig")
    private ReadingLessonConfig readingLessonConfig;
    @JsonProperty("appDefines")
    private AppDefines appDefines;
    @JsonProperty("isbnToCourseName")
    private Map<String, String> isbnToCourseName;
    @JsonProperty("kittedFileLists")
    private Map<String, List<String>> kittedFileLists;
    @JsonProperty("courseLevelDefs")
    private List<CourseLevelDef> courseLevelDefs;
    @JsonProperty("isESL")
    private Boolean isESL;
    @JsonProperty("eslLanguageName")
    private String eslLanguageName;
    @JsonProperty("locale")
    private String locale;

    public Boolean getESL() {
        return isESL;
    }

    public void setESL(Boolean ESL) {
        isESL = ESL;
    }

    public String getEslLanguageName() {
        return eslLanguageName;
    }

    public void setEslLanguageName(String eslLanguageName) {
        this.eslLanguageName = eslLanguageName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCourseLangName() {
        return courseLangName;
    }

    public void setCourseLangName(String courseLangName) {
        this.courseLangName = courseLangName;
    }

    public Integer getCourseDataPresent() {
        return courseDataPresent;
    }

    public void setCourseDataPresent(Integer courseDataPresent) {
        this.courseDataPresent = courseDataPresent;
    }

    public Integer getHasVirtualProduct() {
        return hasVirtualProduct;
    }

    public void setHasVirtualProduct(Integer hasVirtualProduct) {
        this.hasVirtualProduct = hasVirtualProduct;
    }

    public Integer getHasKittedProducts() {
        return hasKittedProducts;
    }

    public void setHasKittedProducts(Integer hasKittedProducts) {
        this.hasKittedProducts = hasKittedProducts;
    }

    public String getVirtualProductCode() {
        return virtualProductCode;
    }

    public void setVirtualProductCode(String virtualProductCode) {
        this.virtualProductCode = virtualProductCode;
    }

    public List<String> getKittedProductCodes() {
        return kittedProductCodes;
    }

    public void setKittedProductCodes(List<String> kittedProductCodes) {
        this.kittedProductCodes = kittedProductCodes;
    }

    public Integer getIsMetadataEncrypted() {
        return isMetadataEncrypted;
    }

    public void setIsMetadataEncrypted(Integer isMetadataEncrypted) {
        this.isMetadataEncrypted = isMetadataEncrypted;
    }

    public Integer getReadingFontSize() {
        return readingFontSize;
    }

    public void setReadingFontSize(Integer readingFontSize) {
        this.readingFontSize = readingFontSize;
    }

    public Integer getUsesTransliteration() {
        return usesTransliteration;
    }

    public void setUsesTransliteration(Integer usesTransliteration) {
        this.usesTransliteration = usesTransliteration;
    }

    public Integer getSupportsTransliterationMouseovers() {
        return supportsTransliterationMouseovers;
    }

    public void setSupportsTransliterationMouseovers(Integer supportsTransliterationMouseovers) {
        this.supportsTransliterationMouseovers = supportsTransliterationMouseovers;
    }

    public ReadingLessonConfig getReadingLessonConfig() {
        return readingLessonConfig;
    }

    public void setReadingLessonConfig(ReadingLessonConfig readingLessonConfig) {
        this.readingLessonConfig = readingLessonConfig;
    }

    public AppDefines getAppDefines() {
        return appDefines;
    }

    public void setAppDefines(AppDefines appDefines) {
        this.appDefines = appDefines;
    }

    public Map<String, String> getIsbnToCourseName() {
        return isbnToCourseName;
    }

    public void setIsbnToCourseName(Map<String, String> isbnToCourseName) {
        this.isbnToCourseName = isbnToCourseName;
    }

    public Map<String, List<String>> getKittedFileLists() {
        return kittedFileLists;
    }

    public void setKittedFileLists(Map<String, List<String>> kittedFileLists) {
        this.kittedFileLists = kittedFileLists;
    }

    public List<CourseLevelDef> getCourseLevelDefs() {
        return courseLevelDefs;
    }

    public void setCourseLevelDefs(List<CourseLevelDef> courseLevelDefs) {
        this.courseLevelDefs = courseLevelDefs;
    }
}
