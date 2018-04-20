package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

import java.util.List;

public class MediaSets {
    private String isbn; //todo: this might be a list, current one is the one purchased.
    private String mediaSetDescription;
    private String courseLanguageNameAbbr;
    private String mediaSetTitle;
    private Integer isEnabled;
    private String isbn13; //same as isbn, previous isbn is the key of this mediaset info.
    private Integer productsId;
    private String courseLanguageName;
    private Integer courseLevel;
    private Integer mediaSetId;
    private Integer isVisible;
    private List<MediaItem> mediaItems;
}
