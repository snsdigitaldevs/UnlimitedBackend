package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.ProductActivation;

import java.util.List;

public class CustomerInfoDTO {
    private List<String> unlimitedProductCodes;
    private List<String> pcmProductCodes;

    private List<ProductActivation> productActivations;
    private List<ProgressDTO> progresses;
    private List<SubUserDto> subUsers;

    public CustomerInfoDTO(List<String> unlimitedProductCodes,
                           List<String> pcmProductCodes,
                           List<ProductActivation> productActivations,
                           List<ProgressDTO> progresses,
                           List<SubUserDto> subUsers) {
        this.unlimitedProductCodes = unlimitedProductCodes;
        this.pcmProductCodes = pcmProductCodes;
        this.productActivations = productActivations;
        this.progresses = progresses;
        this.subUsers = subUsers;
    }

    public List<String> getUnlimitedProductCodes() {
        return unlimitedProductCodes;
    }

    public List<String> getPcmProductCodes() {
        return pcmProductCodes;
    }

    public List<ProductActivation> getProductActivations() {
        return productActivations;
    }

    public List<ProgressDTO> getProgresses() {
        return progresses;
    }

    public List<SubUserDto> getSubUsers() {
        return subUsers;
    }
}
