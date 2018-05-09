
package com.simonschuster.pimsleur.unlimited.data.edt.freeLessons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.FreeLessonDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "includedTerritories",
        "productsName",
        "productsId",
        "isbn13",
        "productsLanguageName",
        "productsAudioUrl",
        "productsDescription",
        "productsSubtext",
        "productsLevel",
        "productsHighestLevelAvail",
        "productsLessonRange",
        "productsTotalLessons",
        "productsGuideType",
        "productsIsEsl",
        "productsPackageName",
        "productsMedia",
        "productsTypeCode",
        "productsPrice",
        "productsImage",
        "PRODUCT_TYPE"
})
public class PCMProduct {

    private static String pcmFreeLessonMp3BaseUrl = "https://coursemanagerdownload.pimsleurdigital.com/public/";
    private static String pcmFreeLessonImageBaseUrl = "https://public.pimsleur.cdn.edtnet.us/assets/images/";

    @JsonProperty("includedTerritories")
    private String includedTerritories;
    @JsonProperty("productsName")
    private String productsName;
    @JsonProperty("productsId")
    private long productsId;
    @JsonProperty("isbn13")
    private String isbn13;
    @JsonProperty("productsLanguageName")
    private String productsLanguageName;
    @JsonProperty("productsAudioUrl")
    private String productsAudioUrl;
    @JsonProperty("productsDescription")
    private String productsDescription;
    @JsonProperty("productsSubtext")
    private String productsSubtext;
    @JsonProperty("productsLevel")
    private long productsLevel;
    @JsonProperty("productsHighestLevelAvail")
    private long productsHighestLevelAvail;
    @JsonProperty("productsLessonRange")
    private String productsLessonRange;
    @JsonProperty("productsTotalLessons")
    private long productsTotalLessons;
    @JsonProperty("productsGuideType")
    private String productsGuideType;
    @JsonProperty("productsIsEsl")
    private Integer productsIsEsl;
    @JsonProperty("productsPackageName")
    private String productsPackageName;
    @JsonProperty("productsMedia")
    private String productsMedia;
    @JsonProperty("productsTypeCode")
    private String productsTypeCode;
    @JsonProperty("productsPrice")
    private String productsPrice;
    @JsonProperty("productsImage")
    private String productsImage;
    @JsonProperty("PRODUCT_TYPE")
    private String pRODUCTTYPE;

    @JsonProperty("includedTerritories")
    public String getIncludedTerritories() {
        return includedTerritories;
    }

    @JsonProperty("includedTerritories")
    public void setIncludedTerritories(String includedTerritories) {
        this.includedTerritories = includedTerritories;
    }

    @JsonProperty("productsName")
    public String getProductsName() {
        return productsName;
    }

    @JsonProperty("productsName")
    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    @JsonProperty("productsId")
    public long getProductsId() {
        return productsId;
    }

    @JsonProperty("productsId")
    public void setProductsId(long productsId) {
        this.productsId = productsId;
    }

    @JsonProperty("isbn13")
    public String getIsbn13() {
        return isbn13;
    }

    @JsonProperty("isbn13")
    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    @JsonProperty("productsLanguageName")
    public String getProductsLanguageName() {
        return productsLanguageName;
    }

    @JsonProperty("productsLanguageName")
    public void setProductsLanguageName(String productsLanguageName) {
        this.productsLanguageName = productsLanguageName;
    }

    @JsonProperty("productsAudioUrl")
    public String getProductsAudioUrl() {
        return productsAudioUrl;
    }

    @JsonProperty("productsAudioUrl")
    public void setProductsAudioUrl(String productsAudioUrl) {
        this.productsAudioUrl = productsAudioUrl;
    }

    @JsonProperty("productsDescription")
    public String getProductsDescription() {
        return productsDescription;
    }

    @JsonProperty("productsDescription")
    public void setProductsDescription(String productsDescription) {
        this.productsDescription = productsDescription;
    }

    @JsonProperty("productsSubtext")
    public String getProductsSubtext() {
        return productsSubtext;
    }

    @JsonProperty("productsSubtext")
    public void setProductsSubtext(String productsSubtext) {
        this.productsSubtext = productsSubtext;
    }

    @JsonProperty("productsLevel")
    public long getProductsLevel() {
        return productsLevel;
    }

    @JsonProperty("productsLevel")
    public void setProductsLevel(long productsLevel) {
        this.productsLevel = productsLevel;
    }

    @JsonProperty("productsHighestLevelAvail")
    public long getProductsHighestLevelAvail() {
        return productsHighestLevelAvail;
    }

    @JsonProperty("productsHighestLevelAvail")
    public void setProductsHighestLevelAvail(long productsHighestLevelAvail) {
        this.productsHighestLevelAvail = productsHighestLevelAvail;
    }

    @JsonProperty("productsLessonRange")
    public String getProductsLessonRange() {
        return productsLessonRange;
    }

    @JsonProperty("productsLessonRange")
    public void setProductsLessonRange(String productsLessonRange) {
        this.productsLessonRange = productsLessonRange;
    }

    @JsonProperty("productsTotalLessons")
    public long getProductsTotalLessons() {
        return productsTotalLessons;
    }

    @JsonProperty("productsTotalLessons")
    public void setProductsTotalLessons(long productsTotalLessons) {
        this.productsTotalLessons = productsTotalLessons;
    }

    @JsonProperty("productsGuideType")
    public String getProductsGuideType() {
        return productsGuideType;
    }

    @JsonProperty("productsGuideType")
    public void setProductsGuideType(String productsGuideType) {
        this.productsGuideType = productsGuideType;
    }

    @JsonProperty("productsIsEsl")
    public Integer getProductsIsEsl() {
        return productsIsEsl;
    }

    @JsonProperty("productsIsEsl")
    public void setProductsIsEsl(Integer productsIsEsl) {
        this.productsIsEsl = productsIsEsl;
    }

    @JsonProperty("productsPackageName")
    public String getProductsPackageName() {
        return productsPackageName;
    }

    @JsonProperty("productsPackageName")
    public void setProductsPackageName(String productsPackageName) {
        this.productsPackageName = productsPackageName;
    }

    @JsonProperty("productsMedia")
    public String getProductsMedia() {
        return productsMedia;
    }

    @JsonProperty("productsMedia")
    public void setProductsMedia(String productsMedia) {
        this.productsMedia = productsMedia;
    }

    @JsonProperty("productsTypeCode")
    public String getProductsTypeCode() {
        return productsTypeCode;
    }

    @JsonProperty("productsTypeCode")
    public void setProductsTypeCode(String productsTypeCode) {
        this.productsTypeCode = productsTypeCode;
    }

    @JsonProperty("productsPrice")
    public String getProductsPrice() {
        return productsPrice;
    }

    @JsonProperty("productsPrice")
    public void setProductsPrice(String productsPrice) {
        this.productsPrice = productsPrice;
    }

    @JsonProperty("productsImage")
    public String getProductsImage() {
        return productsImage;
    }

    @JsonProperty("productsImage")
    public void setProductsImage(String productsImage) {
        this.productsImage = productsImage;
    }

    @JsonProperty("PRODUCT_TYPE")
    public String getPRODUCTTYPE() {
        return pRODUCTTYPE;
    }

    @JsonProperty("PRODUCT_TYPE")
    public void setPRODUCTTYPE(String pRODUCTTYPE) {
        this.pRODUCTTYPE = pRODUCTTYPE;
    }

    public FreeLessonDto toDto() {
        return new FreeLessonDto(
                this.productsLanguageName,
                this.isbn13.replaceAll("-", ""),
                pcmFreeLessonMp3BaseUrl + this.productsAudioUrl,
                pcmFreeLessonImageBaseUrl + this.productsImage);
    }

    public boolean isLevelOne() {
        return getProductsLevel() == 1;
    }
}
