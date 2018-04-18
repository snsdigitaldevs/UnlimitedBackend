
package com.simonschuster.pimsleur.unlimited.data.domain.customer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "productsId",
    "productsModel",
    "productsImage",
    "productsImage2",
    "productsRetailPrice",
    "isbn13",
    "productsGoodsType",
    "isbn13NoDashes",
    "productsName",
    "productsSubtext",
    "productsDescription",
    "productsViewed",
    "productsLanguageName",
    "productsLevel",
    "productsMedia",
    "productsTotalLessons",
    "productsLessonRange",
    "productsHighestLevelAvail",
    "productsHasReadings",
    "productsIsEsl",
    "productsPackageName",
    "productsNumMedia",
    "productsInclReadings",
    "productsMediaSize",
    "productsLessonLength",
    "productsManufName",
    "productsPublisherName",
    "productsAuthor",
    "productsTotalLengthMin",
    "productsStartingUnit",
    "productsGuideType",
    "productsTitleExtended",
    "formattedProductInfo",
    "productsAttributes"
})
public class Product {

    @JsonProperty("productsId")
    private Integer productsId;
    @JsonProperty("productsModel")
    private String productsModel;
    @JsonProperty("productsImage")
    private String productsImage;
    @JsonProperty("productsImage2")
    private String productsImage2;
    @JsonProperty("productsRetailPrice")
    private String productsRetailPrice;
    @JsonProperty("isbn13")
    private String isbn13;
    @JsonProperty("productsGoodsType")
    private String productsGoodsType;
    @JsonProperty("isbn13NoDashes")
    private String isbn13NoDashes;
    @JsonProperty("productsName")
    private String productsName;
    @JsonProperty("productsSubtext")
    private String productsSubtext;
    @JsonProperty("productsDescription")
    private String productsDescription;
    @JsonProperty("productsViewed")
    private Integer productsViewed;
    @JsonProperty("productsLanguageName")
    private String productsLanguageName;
    @JsonProperty("productsLevel")
    private Integer productsLevel;
    @JsonProperty("productsMedia")
    private String productsMedia;
    @JsonProperty("productsTotalLessons")
    private Integer productsTotalLessons;
    @JsonProperty("productsLessonRange")
    private String productsLessonRange;
    @JsonProperty("productsHighestLevelAvail")
    private Integer productsHighestLevelAvail;
    @JsonProperty("productsHasReadings")
    private Integer productsHasReadings;
    @JsonProperty("productsIsEsl")
    private Integer productsIsEsl;
    @JsonProperty("productsPackageName")
    private String productsPackageName;
    @JsonProperty("productsNumMedia")
    private Integer productsNumMedia;
    @JsonProperty("productsInclReadings")
    private Integer productsInclReadings;
    @JsonProperty("productsMediaSize")
    private String productsMediaSize;
    @JsonProperty("productsLessonLength")
    private Integer productsLessonLength;
    @JsonProperty("productsManufName")
    private String productsManufName;
    @JsonProperty("productsPublisherName")
    private String productsPublisherName;
    @JsonProperty("productsAuthor")
    private String productsAuthor;
    @JsonProperty("productsTotalLengthMin")
    private Integer productsTotalLengthMin;
    @JsonProperty("productsStartingUnit")
    private Integer productsStartingUnit;
    @JsonProperty("productsGuideType")
    private String productsGuideType;
    @JsonProperty("productsTitleExtended")
    private String productsTitleExtended;
    @JsonProperty("formattedProductInfo")
    private String formattedProductInfo;
    @JsonProperty("productsAttributes")
    private List<Object> productsAttributes = null;

    @JsonProperty("productsId")
    public Integer getProductsId() {
        return productsId;
    }

    @JsonProperty("productsId")
    public void setProductsId(Integer productsId) {
        this.productsId = productsId;
    }

    @JsonProperty("productsModel")
    public String getProductsModel() {
        return productsModel;
    }

    @JsonProperty("productsModel")
    public void setProductsModel(String productsModel) {
        this.productsModel = productsModel;
    }

    @JsonProperty("productsImage")
    public String getProductsImage() {
        return productsImage;
    }

    @JsonProperty("productsImage")
    public void setProductsImage(String productsImage) {
        this.productsImage = productsImage;
    }

    @JsonProperty("productsImage2")
    public String getProductsImage2() {
        return productsImage2;
    }

    @JsonProperty("productsImage2")
    public void setProductsImage2(String productsImage2) {
        this.productsImage2 = productsImage2;
    }

    @JsonProperty("productsRetailPrice")
    public String getProductsRetailPrice() {
        return productsRetailPrice;
    }

    @JsonProperty("productsRetailPrice")
    public void setProductsRetailPrice(String productsRetailPrice) {
        this.productsRetailPrice = productsRetailPrice;
    }

    @JsonProperty("isbn13")
    public String getIsbn13() {
        return isbn13;
    }

    @JsonProperty("isbn13")
    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    @JsonProperty("productsGoodsType")
    public String getProductsGoodsType() {
        return productsGoodsType;
    }

    @JsonProperty("productsGoodsType")
    public void setProductsGoodsType(String productsGoodsType) {
        this.productsGoodsType = productsGoodsType;
    }

    @JsonProperty("isbn13NoDashes")
    public String getIsbn13NoDashes() {
        return isbn13NoDashes;
    }

    @JsonProperty("isbn13NoDashes")
    public void setIsbn13NoDashes(String isbn13NoDashes) {
        this.isbn13NoDashes = isbn13NoDashes;
    }

    @JsonProperty("productsName")
    public String getProductsName() {
        return productsName;
    }

    @JsonProperty("productsName")
    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    @JsonProperty("productsSubtext")
    public String getProductsSubtext() {
        return productsSubtext;
    }

    @JsonProperty("productsSubtext")
    public void setProductsSubtext(String productsSubtext) {
        this.productsSubtext = productsSubtext;
    }

    @JsonProperty("productsDescription")
    public String getProductsDescription() {
        return productsDescription;
    }

    @JsonProperty("productsDescription")
    public void setProductsDescription(String productsDescription) {
        this.productsDescription = productsDescription;
    }

    @JsonProperty("productsViewed")
    public Integer getProductsViewed() {
        return productsViewed;
    }

    @JsonProperty("productsViewed")
    public void setProductsViewed(Integer productsViewed) {
        this.productsViewed = productsViewed;
    }

    @JsonProperty("productsLanguageName")
    public String getProductsLanguageName() {
        return productsLanguageName;
    }

    @JsonProperty("productsLanguageName")
    public void setProductsLanguageName(String productsLanguageName) {
        this.productsLanguageName = productsLanguageName;
    }

    @JsonProperty("productsLevel")
    public Integer getProductsLevel() {
        return productsLevel;
    }

    @JsonProperty("productsLevel")
    public void setProductsLevel(Integer productsLevel) {
        this.productsLevel = productsLevel;
    }

    @JsonProperty("productsMedia")
    public String getProductsMedia() {
        return productsMedia;
    }

    @JsonProperty("productsMedia")
    public void setProductsMedia(String productsMedia) {
        this.productsMedia = productsMedia;
    }

    @JsonProperty("productsTotalLessons")
    public Integer getProductsTotalLessons() {
        return productsTotalLessons;
    }

    @JsonProperty("productsTotalLessons")
    public void setProductsTotalLessons(Integer productsTotalLessons) {
        this.productsTotalLessons = productsTotalLessons;
    }

    @JsonProperty("productsLessonRange")
    public String getProductsLessonRange() {
        return productsLessonRange;
    }

    @JsonProperty("productsLessonRange")
    public void setProductsLessonRange(String productsLessonRange) {
        this.productsLessonRange = productsLessonRange;
    }

    @JsonProperty("productsHighestLevelAvail")
    public Integer getProductsHighestLevelAvail() {
        return productsHighestLevelAvail;
    }

    @JsonProperty("productsHighestLevelAvail")
    public void setProductsHighestLevelAvail(Integer productsHighestLevelAvail) {
        this.productsHighestLevelAvail = productsHighestLevelAvail;
    }

    @JsonProperty("productsHasReadings")
    public Integer getProductsHasReadings() {
        return productsHasReadings;
    }

    @JsonProperty("productsHasReadings")
    public void setProductsHasReadings(Integer productsHasReadings) {
        this.productsHasReadings = productsHasReadings;
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

    @JsonProperty("productsNumMedia")
    public Integer getProductsNumMedia() {
        return productsNumMedia;
    }

    @JsonProperty("productsNumMedia")
    public void setProductsNumMedia(Integer productsNumMedia) {
        this.productsNumMedia = productsNumMedia;
    }

    @JsonProperty("productsInclReadings")
    public Integer getProductsInclReadings() {
        return productsInclReadings;
    }

    @JsonProperty("productsInclReadings")
    public void setProductsInclReadings(Integer productsInclReadings) {
        this.productsInclReadings = productsInclReadings;
    }

    @JsonProperty("productsMediaSize")
    public String getProductsMediaSize() {
        return productsMediaSize;
    }

    @JsonProperty("productsMediaSize")
    public void setProductsMediaSize(String productsMediaSize) {
        this.productsMediaSize = productsMediaSize;
    }

    @JsonProperty("productsLessonLength")
    public Integer getProductsLessonLength() {
        return productsLessonLength;
    }

    @JsonProperty("productsLessonLength")
    public void setProductsLessonLength(Integer productsLessonLength) {
        this.productsLessonLength = productsLessonLength;
    }

    @JsonProperty("productsManufName")
    public String getProductsManufName() {
        return productsManufName;
    }

    @JsonProperty("productsManufName")
    public void setProductsManufName(String productsManufName) {
        this.productsManufName = productsManufName;
    }

    @JsonProperty("productsPublisherName")
    public String getProductsPublisherName() {
        return productsPublisherName;
    }

    @JsonProperty("productsPublisherName")
    public void setProductsPublisherName(String productsPublisherName) {
        this.productsPublisherName = productsPublisherName;
    }

    @JsonProperty("productsAuthor")
    public String getProductsAuthor() {
        return productsAuthor;
    }

    @JsonProperty("productsAuthor")
    public void setProductsAuthor(String productsAuthor) {
        this.productsAuthor = productsAuthor;
    }

    @JsonProperty("productsTotalLengthMin")
    public Integer getProductsTotalLengthMin() {
        return productsTotalLengthMin;
    }

    @JsonProperty("productsTotalLengthMin")
    public void setProductsTotalLengthMin(Integer productsTotalLengthMin) {
        this.productsTotalLengthMin = productsTotalLengthMin;
    }

    @JsonProperty("productsStartingUnit")
    public Integer getProductsStartingUnit() {
        return productsStartingUnit;
    }

    @JsonProperty("productsStartingUnit")
    public void setProductsStartingUnit(Integer productsStartingUnit) {
        this.productsStartingUnit = productsStartingUnit;
    }

    @JsonProperty("productsGuideType")
    public String getProductsGuideType() {
        return productsGuideType;
    }

    @JsonProperty("productsGuideType")
    public void setProductsGuideType(String productsGuideType) {
        this.productsGuideType = productsGuideType;
    }

    @JsonProperty("productsTitleExtended")
    public String getProductsTitleExtended() {
        return productsTitleExtended;
    }

    @JsonProperty("productsTitleExtended")
    public void setProductsTitleExtended(String productsTitleExtended) {
        this.productsTitleExtended = productsTitleExtended;
    }

    @JsonProperty("formattedProductInfo")
    public String getFormattedProductInfo() {
        return formattedProductInfo;
    }

    @JsonProperty("formattedProductInfo")
    public void setFormattedProductInfo(String formattedProductInfo) {
        this.formattedProductInfo = formattedProductInfo;
    }

    @JsonProperty("productsAttributes")
    public List<Object> getProductsAttributes() {
        return productsAttributes;
    }

    @JsonProperty("productsAttributes")
    public void setProductsAttributes(List<Object> productsAttributes) {
        this.productsAttributes = productsAttributes;
    }

}
