package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BatchedMediaItemUrls {

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

    public String getUrlOfMediaItem(String fileName) {
        //9781442321588_Unit_10.mp3
        //***subscription/9781508218036_Korean_Level_2/9781508218036_Reading_U05.mp3?*
        //for subscription, the product code isn't necessary the same,
        // so we can just check by unit number
        String unit = fileName.substring(fileName.indexOf("_"));
        List<ResultDataBean.UrlsBean> urls = this.getResult_data().getUrls();
        Optional<ResultDataBean.UrlsBean> match = urls.stream()
                .filter(x -> x.getUrl().contains(unit))
                .findFirst();
        if (match.isPresent()) {
            return match.get().getUrl();
        }
        return "";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultDataBean {

        private int mediaSetId;
        private int productsId;
        private List<UrlsBean> urls;

        public int getMediaSetId() {
            return mediaSetId;
        }

        public void setMediaSetId(int mediaSetId) {
            this.mediaSetId = mediaSetId;
        }

        public int getProductsId() {
            return productsId;
        }

        public void setProductsId(int productsId) {
            this.productsId = productsId;
        }

        public List<UrlsBean> getUrls() {
            return urls;
        }

        public void setUrls(List<UrlsBean> urls) {
            this.urls = urls;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class UrlsBean {

            private String url;
            private int mediaItemId;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getMediaItemId() {
                return mediaItemId;
            }

            public void setMediaItemId(int mediaItemId) {
                this.mediaItemId = mediaItemId;
            }
        }
    }
}
