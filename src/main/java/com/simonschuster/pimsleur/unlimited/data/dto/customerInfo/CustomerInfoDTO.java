package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.ProductActivation;

import java.util.List;

public class CustomerInfoDTO {
    private List<String> unlimitedProductCodes;
    private List<String> pcmProductCodes;

    private List<ProductActivation> productActivations;

    private List<ProgressDTO> progresses;
}
