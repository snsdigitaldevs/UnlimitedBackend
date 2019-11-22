package com.simonschuster.pimsleur.unlimited.data.edt.customer;


public class AuthDescriptor {
    private String userId;
    private String customersId;
    private String authProvidersId;
    private int authProviderOrdinal;
    private long authResultDateTime;
    private int resultCode;
    private boolean isAuthenticated;
    private boolean isAuthDenied;
    private int requestCount;
    private int authType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomersId() {
        return customersId;
    }

    public void setCustomersId(String customersId) {
        this.customersId = customersId;
    }

    public String getAuthProvidersId() {
        return authProvidersId;
    }

    public void setAuthProvidersId(String authProvidersId) {
        this.authProvidersId = authProvidersId;
    }

    public int getAuthProviderOrdinal() {
        return authProviderOrdinal;
    }

    public void setAuthProviderOrdinal(int authProviderOrdinal) {
        this.authProviderOrdinal = authProviderOrdinal;
    }

    public long getAuthResultDateTime() {
        return authResultDateTime;
    }

    public void setAuthResultDateTime(long authResultDateTime) {
        this.authResultDateTime = authResultDateTime;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isAuthDenied() {
        return isAuthDenied;
    }

    public void setAuthDenied(boolean authDenied) {
        isAuthDenied = authDenied;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }
}
