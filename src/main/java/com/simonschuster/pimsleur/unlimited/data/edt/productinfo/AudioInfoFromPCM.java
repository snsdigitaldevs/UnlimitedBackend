package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioInfoFromPCM {
    private ResultDataBean result_data;
    private int result_code;

    public ResultDataBean getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultDataBean result_data) {
        this.result_data = result_data;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultDataBean {
        private String url;
        private int fileSizeBytes;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getFileSizeBytes() {
            return fileSizeBytes;
        }

        public void setFileSizeBytes(int fileSizeBytes) {
            this.fileSizeBytes = fileSizeBytes;
        }
    }
}
