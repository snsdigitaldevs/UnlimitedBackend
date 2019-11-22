package com.simonschuster.pimsleur.unlimited.data.dto.availableProducts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class AvailableProductsDto {
    private List<AvailableProductDto> purchasedProducts;
    private List<AvailableProductDto> freeProducts;

    public AvailableProductsDto(List<AvailableProductDto> purchasedProducts, List<AvailableProductDto> freeProducts) {
        this.purchasedProducts = purchasedProducts;
        this.freeProducts = freeProducts;
    }

    public List<AvailableProductDto> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(
        List<AvailableProductDto> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public List<AvailableProductDto> getFreeProducts() {
        return freeProducts;
    }
}
