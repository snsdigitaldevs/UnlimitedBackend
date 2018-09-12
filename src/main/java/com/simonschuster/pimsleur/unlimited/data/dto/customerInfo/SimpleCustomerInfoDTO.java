package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class SimpleCustomerInfoDTO {

    private List<SubUserDto> subUsers;

    private boolean hasPendingIos;
    private boolean hasPendingAndroid;

    public List<SubUserDto> getSubUsers() {
        return subUsers;
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

    public void setSubUsers(List<SubUserDto> subUsers) {
        this.subUsers = subUsers;
    }
}
