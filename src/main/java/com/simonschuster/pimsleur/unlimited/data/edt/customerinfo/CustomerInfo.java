package com.simonschuster.pimsleur.unlimited.data.edt.customerinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.SubUserDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerInfo {

    private ResultData result_data;
    private int result_code;

    public ResultData getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultData result_data) {
        this.result_data = result_data;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public SubUserDto toDto(String name, String appUserId) {
        boolean isRootUser = result_data == null ? false : result_data.getAppUser().isRootSubUser();
        return new SubUserDto(name, appUserId, isRootUser);
    }

    public SubUserDto toDto(String name) {
        boolean isRootUser = result_data == null ? false : result_data.getAppUser().isRootSubUser();
        String appUserId = result_data == null ? "" : result_data.getAppUser().getAppUserId();
        return new SubUserDto(name, appUserId, isRootUser);
    }
}
