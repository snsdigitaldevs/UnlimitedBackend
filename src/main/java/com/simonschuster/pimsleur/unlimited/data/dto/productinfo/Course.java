package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.simonschuster.pimsleur.unlimited.constants.CommonConstants.ARABIC_PU_ISBN;
import static com.simonschuster.pimsleur.unlimited.constants.CommonConstants.HEBREW_PU_ISBN;

@JsonInclude(NON_EMPTY)
public class Course {
    private String languageName;
    private Integer level;
    private String simpleCourseName;
    private List<Lesson> lessons;
    private String productCode;
    private String courseName;
    private Boolean isOneOfNineBig;
    private Boolean isFree;
    private Readings readings; // pdf and mp3 for pcm OR mp3 for pu

    public String getLanguageName() {
        return languageName;
    }

    public Integer getLevel() {
        return level;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Boolean getIsOneOfNineBig() {
        return isOneOfNineBig;
    }

    public void setIsOneOfNineBig(Boolean isOneOfNineBig) {
        this.isOneOfNineBig = isOneOfNineBig;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean free) {
        isFree = free;
    }

    public Readings getReadings() {
        return readings;
    }

    public void setReadings(Readings readings) {
        this.readings = readings;
    }

    public AvailableProductDto toPuAvailableProductDto() {
        return new AvailableProductDto(getLanguageName(), getLanguageName(), getCourseName(),
                getProductCode(), true, getLevel());
    }

    public String getSimpleCourseName() {
        return simpleCourseName;
    }

    public void setSimpleCourseName(String simpleCourseName) {
        this.simpleCourseName = simpleCourseName;
    }

    public Boolean getPuArabic() {
        //For old app versions, add hebrew to display form right to left
        return ARABIC_PU_ISBN.contains(productCode) || HEBREW_PU_ISBN.contains(productCode);
    }
}
