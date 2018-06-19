package com.simonschuster.pimsleur.unlimited.data.edt.sendEmailPermission;

public class SendEmailPermissionDto {
    String result_code;
    Object result_data;

    public Object getResult_data() {
        return result_data;
    }

    public void setResult_data(Object result_data) {
        this.result_data = result_data;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }
}
