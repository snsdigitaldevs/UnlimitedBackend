package com.simonschuster.pimsleur.unlimited.data.dto.freeLessons;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class AvailableProductDto {
    private String languageName;
    private String productCode;
    private String courseName;
    // this is the isbn you should use when calling the upsell api
    private String productCodeForUpsell;

    private boolean isPuProduct = false;
    private Integer level = 1; // default level 1 for free lessons

    public AvailableProductDto(String languageName, String productCode, boolean isPu) {
        this.languageName = languageName;
        this.productCode = productCode;
        this.isPuProduct = isPu;
    }

    public AvailableProductDto(String languageName, String courseName, String productCode, boolean isPu, Integer level) {
        this(languageName, productCode, isPu);

        this.level = level;
        this.courseName = courseName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean isPuProduct() {
        return isPuProduct;
    }

    public void setPuProduct(boolean puProduct) {
        isPuProduct = puProduct;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getProductCodeForUpsell() {
        return productCodeForUpsell;
    }

    public void setProductCodeForUpsell(String productCodeForUpsell) {
        this.productCodeForUpsell = productCodeForUpsell;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isSameLevelSameLang(AvailableProductDto that) {
        return this.isSameLang(that) &&
                Objects.equals(this.level, that.level);
    }

    public boolean isSameLang(AvailableProductDto that) {
        return Objects.equals(normalizeLangName(this.languageName), normalizeLangName(that.languageName));
    }

    private String normalizeLangName(String languageName) {
        if (Objects.equals(languageName, "Chinese Mandarin")) {
            // pcm lang name is CM, but pu lang name is MC
            return "Mandarin Chinese";
        }
        if (Objects.equals(languageName, "Portuguese Brazilian")) {
            return "Brazilian Portuguese";
        }
        return languageName;
    }
}
