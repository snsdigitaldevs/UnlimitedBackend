package com.simonschuster.pimsleur.unlimited.data.domain.customer;

public class AggregatedCustomerInfo {
    private CustomerInfo unlimitedCustomerInfo;
    private CustomerInfo pcmCustomerInfo;

    public AggregatedCustomerInfo(CustomerInfo unlimited, CustomerInfo pcm) {
        unlimitedCustomerInfo = unlimited;
        pcmCustomerInfo = pcm;
    }

    public CustomerInfo getUnlimitedCustomerInfo() {
        return unlimitedCustomerInfo;
    }

    public void setUnlimitedCustomerInfo(CustomerInfo unlimitedCustomerInfo) {
        this.unlimitedCustomerInfo = unlimitedCustomerInfo;
    }

    public CustomerInfo getPcmCustomerInfo() {
        return pcmCustomerInfo;
    }

    public void setPcmCustomerInfo(CustomerInfo pcmCustomerInfo) {
        this.pcmCustomerInfo = pcmCustomerInfo;
    }
}
