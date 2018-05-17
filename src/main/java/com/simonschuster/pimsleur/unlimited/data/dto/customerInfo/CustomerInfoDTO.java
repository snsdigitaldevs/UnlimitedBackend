package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

import com.simonschuster.pimsleur.unlimited.data.edt.customer.ProductActivation;

import java.util.List;

public class CustomerInfoDTO {
    private List<String> unlimitedProductCodes;
    private List<String> pcmProductCodes;

    private List<ProductActivation> productActivations;
    private List<ProgressDTO> progresses;
    private List<SubUserDto> subUsers;

    private Long unlimitedLastSaveId;
    private Long pcmLastSaveId;

    private String customerId;
    private String registrantId;
    private String identityVerificationToken;

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

    public void setUnlimitedLastSaveId(Long unlimitedLastSaveId) {
        this.unlimitedLastSaveId = unlimitedLastSaveId;
    }

    public void setPcmLastSaveId(Long pcmLastSaveId) {
        this.pcmLastSaveId = pcmLastSaveId;
    }

    public Long getUnlimitedLastSaveId() {
        return unlimitedLastSaveId;
    }

    public Long getPcmLastSaveId() {
        return pcmLastSaveId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setIdentityVerificationToken(String identityVerificationToken) {
        this.identityVerificationToken = identityVerificationToken;
    }

    public String getIdentityVerificationToken() {
        return identityVerificationToken;
    }

    public String getRegistrantId() {
        return registrantId;
    }

    public void setRegistrantId(String registrantId) {
        this.registrantId = registrantId;
    }
}
