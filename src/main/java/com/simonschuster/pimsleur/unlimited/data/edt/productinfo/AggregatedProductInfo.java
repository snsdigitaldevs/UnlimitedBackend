package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

public class AggregatedProductInfo {
    private ProductInfoFromUnlimited productInfoFromPU;
    private ProductInfoFromPCM productInfoFromPCM;

    public void setProductInfoFromPCM(ProductInfoFromPCM productInfoFromPCM) {
        this.productInfoFromPCM = productInfoFromPCM;
    }

    public ProductInfoFromPCM getProductInfoFromPCM() {
        return productInfoFromPCM;
    }

    public void setProductInfoFromPU(ProductInfoFromUnlimited productInfoFromPU) {
        this.productInfoFromPU = productInfoFromPU;
    }

    public ProductInfoFromUnlimited getProductInfoFromPU() {
        return productInfoFromPU;
    }
}
