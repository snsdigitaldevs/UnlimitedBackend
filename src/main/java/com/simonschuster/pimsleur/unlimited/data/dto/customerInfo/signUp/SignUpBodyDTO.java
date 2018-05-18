package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp;

public class SignUpBodyDTO {
    private String email;
    private String password;
    private String storeDomain;
    private String countryCode;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStoreDomain() {
        return storeDomain;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
