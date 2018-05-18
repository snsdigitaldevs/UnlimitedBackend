package com.simonschuster.pimsleur.unlimited.data.dto.customerInfo.signUp;

public class SignUpBodyDTO {
    private String email;
    private String userName;
    private String password;
    private String storeDomain;
    private String countryCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getStoreDomain() {
        return storeDomain;
    }

    public void setStoreDomain(String storeDomain) {
        this.storeDomain = storeDomain;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
