package com.simonschuster.pimsleur.unlimited.data.dto.promotions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.simonschuster.pimsleur.unlimited.constants.CommonConstants.AUSTRALIA;
import static com.simonschuster.pimsleur.unlimited.constants.CommonConstants.CANADA;
import static com.simonschuster.pimsleur.unlimited.constants.CommonConstants.UK;
import static com.simonschuster.pimsleur.unlimited.constants.CommonConstants.USA;
import static java.util.stream.Collectors.toList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Base Course Type",
        "ISBN",
        "Course name",
        "Upsell in-app purchase ISBN",
        "Upsell course name",
        "Upsell web app add-to-cart",
        "Upsell2 in-app purchase ISBN",
        "Upsell2 course name",
        "Upsell2 web app add-to-cart",
        "Upgrade in-app purchase ISBN",
        "Upgrade course name",
        "Upgrade web app add-to-cart",
        "Other format 1 (Upsell) ISBN",
        "Other format 2 (Upgrade) ISBN",
        "Other format 3 (DVD) ISBN",
})
public class PurchaseMapping {
    @JsonProperty("Base Course Type")
    private String baseCourseType;
    @JsonProperty("ISBN")
    private String iSBN;
    @JsonProperty("Course name")
    private String courseName;
    @JsonProperty("Upsell in-app purchase ISBN")
    private String upsellInAppPurchaseISBN;
    @JsonProperty("Upsell course name")
    private String upsellCourseName;
    @JsonProperty("Upsell web app add-to-cart")
    private String upsellWebAppAddToCart;
    @JsonProperty("Upsell2 in-app purchase ISBN")
    private String upsell2InAppPurchaseISBN;
    @JsonProperty("Upsell2 course name")
    private String upsell2CourseName;
    @JsonProperty("Upsell2 web app add-to-cart")
    private String upsell2WebAppAddToCart;
    @JsonProperty("Upsell2 web app add-to-cart CANADA")
    private String upsell2WebAppAddToCartCanada;
    @JsonProperty("Upsell2 web app add-to-cart UK")
    private String upsell2WebAppAddToCartUk;
    @JsonProperty("Upsell2 web app add-to-cart AUSTRALIA")
    private String upsell2WebAppAddToCartAustralia;
    @JsonProperty("Upgrade in-app purchase ISBN")
    private String upgradeInAppPurchaseISBN;
    @JsonProperty("Upgrade course name")
    private String upgradeCourseName;
    @JsonProperty("Upgrade web app add-to-cart")
    private String upgradeWebAppAddToCart;
    @JsonProperty("Other format 1 (Upsell) ISBN")
    private String otherFormat1ISBN;
    @JsonProperty("Other format 2 (Upgrade) ISBN")
    private String otherFormat2ISBN;
    @JsonProperty("Other format 3 (DVD) ISBN")
    private String otherFormat3ISBN;

    public String getBaseCourseType() {
        return baseCourseType;
    }

    public String getISBN() {
        return iSBN;
    }

    public void setISBN(String iSBN) {
        this.iSBN = iSBN;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getUpsellInAppPurchaseISBN() {
        return upsellInAppPurchaseISBN;
    }

    public String getUpsellCourseName() {
        return upsellCourseName;
    }

    public String getUpgradeInAppPurchaseISBN() {
        return upgradeInAppPurchaseISBN;
    }

    public String getUpgradeCourseName() {
        return upgradeCourseName;
    }

    public String getUpsell2InAppPurchaseISBN() {
        return upsell2InAppPurchaseISBN;
    }

    public String getUpsell2CourseName() {
        return upsell2CourseName;
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

    public String getUpsellWebAppAddToCart() {
        return upsellWebAppAddToCart;
    }

    public String getUpsell2WebAppAddToCart() {
        return upsell2WebAppAddToCart;
    }

    public String getUpgradeWebAppAddToCart() {
        return upgradeWebAppAddToCart;
    }

    public String getUpsell2WebAppAddToCartCanada() {
        return upsell2WebAppAddToCartCanada;
    }

    public String getUpsell2WebAppAddToCartUk() {
        return upsell2WebAppAddToCartUk;
    }

    public String getUpsell2WebAppAddToCartAustralia() {
        return upsell2WebAppAddToCartAustralia;
    }

    public boolean matches(String isbn) {
        return getAllFormats().stream()
                .anyMatch(oneFormat -> oneFormat.equals(isbn));
    }

    public UpsellDto toUpsellDto(boolean isUpsellIgnored, boolean isSubIgnored,
        boolean isUpgradeIgnored) {
        UpsellItem nextLevel = createNextLevel(isUpsellIgnored);
        Map<String, UpsellItem> upsellItemMap = createNextSub(isSubIgnored);
        UpsellItem nextUpgrade = createNextVersion(isUpgradeIgnored);
        return UpsellDto.build(nextLevel, nextUpgrade, upsellItemMap);
    }

    private UpsellItem createNextLevel(boolean ignoreUpsell) {
        if (!ignoreUpsell && getUpsellInAppPurchaseISBN().length() > 0) {
            return new UpsellItem(getUpsellInAppPurchaseISBN(), getUpsellCourseName(), getUpsellWebAppAddToCart());
        }
        return null;
    }

    private Map<String, UpsellItem> createNextSub(boolean isSubIgnored) {

        if (!isSubIgnored && getUpsell2InAppPurchaseISBN().length() > 0) {
            Map<String, UpsellItem> upsellItemMap = new HashMap<>();
            upsellItemMap.put(USA,
                new UpsellItem(getUpsell2InAppPurchaseISBN(), getUpsell2CourseName(),
                    getUpsell2WebAppAddToCart()));
            upsellItemMap.put(UK,
                new UpsellItem(getUpsell2InAppPurchaseISBN(), getUpsell2CourseName(),
                    getUpsell2WebAppAddToCartUk()));
            upsellItemMap.put(CANADA,
                new UpsellItem(getUpsell2InAppPurchaseISBN(), getUpsell2CourseName(),
                    getUpsell2WebAppAddToCartCanada()));
            upsellItemMap.put(AUSTRALIA,
                new UpsellItem(getUpsell2InAppPurchaseISBN(), getUpsell2CourseName(),
                    getUpsell2WebAppAddToCartAustralia()));
            return upsellItemMap;
        }
        return null;
    }

    private UpsellItem createNextVersion(boolean ignoreUpgrade) {
        if (!ignoreUpgrade && getUpgradeInAppPurchaseISBN().length() > 0) {
            return new UpsellItem(getUpgradeInAppPurchaseISBN(), getUpgradeCourseName(), getUpgradeWebAppAddToCart());
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
