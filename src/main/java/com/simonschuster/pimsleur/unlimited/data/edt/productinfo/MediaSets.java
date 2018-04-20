package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaSets {
//    private String isbn; //todo: this might be a list, current one is the one purchased.
//    private String mediaSetDescription;
//    private String courseLanguageNameAbbr;
//    private String mediaSetTitle;
//    private Integer isEnabled;
//    private String isbn13; //same as isbn, previous isbn is the key of this mediaset info.
//    private Integer productsId;
//    private String courseLanguageName;
//    private Integer courseLevel;
//    private Integer mediaSetId;
//    private Integer isVisible;
//    private List<MediaItem> mediaItems;


    //todo: make this field key to variable
    //todo: convert string result of this field to java object
    @JsonProperty("9781508243328")
    private String _9781508243328;

    @JsonProperty("9781508243328")
    public String get_9781508243328() {
        return _9781508243328;
    }

    @JsonProperty("9781508243328")
    public void set_9781508243328(String _9781508243328) {
        this._9781508243328 = _9781508243328;
    }
}
