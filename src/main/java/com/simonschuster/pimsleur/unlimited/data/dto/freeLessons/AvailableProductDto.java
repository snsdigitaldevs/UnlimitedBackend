package com.simonschuster.pimsleur.unlimited.data.dto.freeLessons;

public class AvailableProductDto {
    private String languageName;
    private String productCode;
    private boolean isPuProduct = false;

    public AvailableProductDto(String languageName, String productCode, boolean isPu) {
        this.languageName = languageName;
        this.productCode = productCode;
        this.isPuProduct = isPu;
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
}
