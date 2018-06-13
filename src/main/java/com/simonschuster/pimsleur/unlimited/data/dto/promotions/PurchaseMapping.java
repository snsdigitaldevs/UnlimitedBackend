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
        "Course name",
        "Other format 1 ISBN",
        "Other format 1 Type",
        "Other format 2 ISBN",
        "Other format 2 Type",
        "Other format 3 ISBN",
        "Other format 3 Type",
        "Upsell in-app purchase ISBN",
        "Upsell course name",
        "Upsell2 in-app purchase ISBN",
        "Upsell2 course name",
        "Upgrade in-app purchase ISBN",
        "Upgrade course name",
})
public class PurchaseMapping {

    @JsonProperty("Base Course Type")
    private String baseCourseType;
    @JsonProperty("ISBN")
    private String iSBN;
    @JsonProperty("Course name")
    private String courseName;
    @JsonProperty("Other format 1 ISBN")
    private String otherFormat1ISBN;
    @JsonProperty("Other format 1 Type")
    private String otherFormat1Type;
    @JsonProperty("Other format 2 ISBN")
    private String otherFormat2ISBN;
    @JsonProperty("Other format 2 Type")
    private String otherFormat2Type;
    @JsonProperty("Other format 3 ISBN")
    private String otherFormat3ISBN;
    @JsonProperty("Other format 3 Type")
    private String otherFormat3Type;
    @JsonProperty("Upsell in-app purchase ISBN")
    private String upsellInAppPurchaseISBN;
    @JsonProperty("Upsell course name")
    private String upsellCourseName;

    @JsonProperty("Upsell2 in-app purchase ISBN")
    private String upsell2InAppPurchaseISBN;
    @JsonProperty("Upsell2 course name")
    private String upsell2CourseName;

    @JsonProperty("Upgrade in-app purchase ISBN")
    private String upgradeInAppPurchaseISBN;
    @JsonProperty("Upgrade course name")
    private String upgradeCourseName;

    @JsonProperty("Base Course Type")
    public String getBaseCourseType() {
        return baseCourseType;
    }

    @JsonProperty("Base Course Type")
    public void setBaseCourseType(String baseCourseType) {
        this.baseCourseType = baseCourseType;
    }

    @JsonProperty("ISBN")
    public String getISBN() {
        return iSBN;
    }

    @JsonProperty("ISBN")
    public void setISBN(String iSBN) {
        this.iSBN = iSBN;
    }

    @JsonProperty("Course name")
    public String getCourseName() {
        return courseName;
    }

    @JsonProperty("Course name")
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @JsonProperty("Other format 1 ISBN")
    public String getOtherFormat1ISBN() {
        return otherFormat1ISBN;
    }

    @JsonProperty("Other format 1 ISBN")
    public void setOtherFormat1ISBN(String otherFormat1ISBN) {
        this.otherFormat1ISBN = otherFormat1ISBN;
    }

    @JsonProperty("Other format 1 Type")
    public String getOtherFormat1Type() {
        return otherFormat1Type;
    }

    @JsonProperty("Other format 1 Type")
    public void setOtherFormat1Type(String otherFormat1Type) {
        this.otherFormat1Type = otherFormat1Type;
    }

    @JsonProperty("Other format 2 ISBN")
    public String getOtherFormat2ISBN() {
        return otherFormat2ISBN;
    }

    @JsonProperty("Other format 2 ISBN")
    public void setOtherFormat2ISBN(String otherFormat2ISBN) {
        this.otherFormat2ISBN = otherFormat2ISBN;
    }

    @JsonProperty("Other format 2 Type")
    public String getOtherFormat2Type() {
        return otherFormat2Type;
    }

    @JsonProperty("Other format 2 Type")
    public void setOtherFormat2Type(String otherFormat2Type) {
        this.otherFormat2Type = otherFormat2Type;
    }

    @JsonProperty("Other format 3 ISBN")
    public String getOtherFormat3ISBN() {
        return otherFormat3ISBN;
    }

    @JsonProperty("Other format 3 ISBN")
    public void setOtherFormat3ISBN(String otherFormat3ISBN) {
        this.otherFormat3ISBN = otherFormat3ISBN;
    }

    @JsonProperty("Other format 3 Type")
    public String getOtherFormat3Type() {
        return otherFormat3Type;
    }

    @JsonProperty("Other format 3 Type")
    public void setOtherFormat3Type(String otherFormat3Type) {
        this.otherFormat3Type = otherFormat3Type;
    }

    @JsonProperty("Upsell in-app purchase ISBN")
    public String getUpsellInAppPurchaseISBN() {
        return upsellInAppPurchaseISBN;
    }

    @JsonProperty("Upsell in-app purchase ISBN")
    public void setUpsellInAppPurchaseISBN(String upsellInAppPurchaseISBN) {
        this.upsellInAppPurchaseISBN = upsellInAppPurchaseISBN;
    }

    @JsonProperty("Upsell course name")
    public String getUpsellCourseName() {
        return upsellCourseName;
    }

    @JsonProperty("Upsell course name")
    public void setUpsellCourseName(String upsellCourseName) {
        this.upsellCourseName = upsellCourseName;
    }

    @JsonProperty("Upgrade in-app purchase ISBN")
    public String getUpgradeInAppPurchaseISBN() {
        return upgradeInAppPurchaseISBN;
    }

    @JsonProperty("Upgrade in-app purchase ISBN")
    public void setUpgradeInAppPurchaseISBN(String upgradeInAppPurchaseISBN) {
        this.upgradeInAppPurchaseISBN = upgradeInAppPurchaseISBN;
    }

    @JsonProperty("Upgrade course name")
    public String getUpgradeCourseName() {
        return upgradeCourseName;
    }

    @JsonProperty("Upgrade course name")
    public void setUpgradeCourseName(String upgradeCourseName) {
        this.upgradeCourseName = upgradeCourseName;
    }

    public String getUpsell2InAppPurchaseISBN() {
        return upsell2InAppPurchaseISBN;
    }

    public void setUpsell2InAppPurchaseISBN(String upsell2InAppPurchaseISBN) {
        this.upsell2InAppPurchaseISBN = upsell2InAppPurchaseISBN;
    }

    public String getUpsell2CourseName() {
        return upsell2CourseName;
    }

    public void setUpsell2CourseName(String upsell2CourseName) {
        this.upsell2CourseName = upsell2CourseName;
    }

    public boolean matches(String isbn) {
        return getAllFormats().stream()
                .anyMatch(oneFormat -> oneFormat.equals(isbn));
    }

    public UpsellDto toUpsellDto(boolean isUpsellIgnored, boolean isSubIgnored, boolean isUpgradeIgnored) {
        return new UpsellDto(
                createNextLevel(isUpsellIgnored),
                createNextSub(isSubIgnored),
                createNextVersion(isUpgradeIgnored));
    }

    private UpsellItem createNextLevel(boolean ignoreUpsell) {
        if (!ignoreUpsell && getUpsellInAppPurchaseISBN().length() > 0) {
            return new UpsellItem(getUpsellInAppPurchaseISBN(), getUpsellCourseName());
        }
        return null;
    }

    private UpsellItem createNextSub(boolean isSubIgnored) {
        if (!isSubIgnored && getUpsell2InAppPurchaseISBN().length() > 0) {
            return new UpsellItem(getUpsell2InAppPurchaseISBN(), getUpsell2CourseName());
        }
        return null;
    }

    private UpsellItem createNextVersion(boolean ignoreUpgrade) {
        if (!ignoreUpgrade && getUpgradeInAppPurchaseISBN().length() > 0) {
            return new UpsellItem(getUpgradeInAppPurchaseISBN(), getUpgradeCourseName());
        }
        return null;
    }

    // isbns in the returned list are for the same product
    public List<String> getAllFormats() {
        return Stream
                .of(this.getISBN(),
                        this.getOtherFormat1ISBN(),
                        this.getOtherFormat2ISBN(),
                        this.getOtherFormat3ISBN())
                .filter(isbn -> isbn.length() > 0)
                .collect(toList());
    }
}