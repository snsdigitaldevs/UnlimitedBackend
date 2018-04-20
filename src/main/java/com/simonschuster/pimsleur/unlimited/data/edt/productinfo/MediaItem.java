package com.simonschuster.pimsleur.unlimited.data.edt.productinfo;

public class MediaItem {
    //This is for practices info, like flash card info
    private String mediaItemLicenseFileName;
    private String remoteUrl;
    private Integer idMetadata;
    private String remoteAccessRestriction;
    private Integer sortOrder;
    private String imageURL;
    private Integer isActive; //This field for image media is String
    private Integer lengthSeconds;
    private String filename;
    private String licenseFileName;
    private String fileSizeBytes;  //This field for image media is Integer
    private Integer isVisible;  //This field for image media is Integer
    private Integer mediaSetId;
    private Integer isEncrypted;  //This field for image media is String
    private String description;
    private String refId;  //This field for image media is Integer
    private Integer mediaItemId;
    private String typeId;
    private String formatId;
    private Integer classId;
    private String title;

    //The following is special fields for image media information
    private String imageDescription;
    private String imageLocation;
    private String imageCredits;
    private String unit;

}
