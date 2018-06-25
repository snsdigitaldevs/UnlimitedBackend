package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.ProductActivation;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class CustomerInfoDTO {
    private List<String> unlimitedProductCodes;
    private List<String> pcmProductCodes;

    private List<ProductActivation> productActivations;
    private List<ProgressDTO> progresses;
    private List<SubUserDto> subUsers;

    private Long unlimitedLastSaveId;
    private Long pcmLastSaveId;

    private boolean hasPendingIos;
    private boolean hasPendingAndroid;

    private String customerId;
    private String registrantId;
    private String identityVerificationToken;

    private String optinNewProductInfo;

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

    public boolean getHasPendingIos() {
        return hasPendingIos;
    }

    public void setHasPendingIos(boolean hasPendingIos) {
        this.hasPendingIos = hasPendingIos;
    }

    public boolean getHasPendingAndroid() {
        return hasPendingAndroid;
    }

    public void setHasPendingAndroid(boolean hasPendingAndroid) {
        this.hasPendingAndroid = hasPendingAndroid;
    }

    public String getOptinNewProductInfo() {
        return optinNewProductInfo;
    }

    public void setOptinNewProductInfo(String optinNewProductInfo) {
        this.optinNewProductInfo = optinNewProductInfo;
    }
}
