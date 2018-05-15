package com.simonschuster.pimsleur.unlimited.data.edt.customerinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.AppUser;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultData {

    private AppUser appUser;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}