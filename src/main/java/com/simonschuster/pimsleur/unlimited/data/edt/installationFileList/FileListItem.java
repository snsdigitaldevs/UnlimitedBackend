
package com.simonschuster.pimsleur.unlimited.data.edt.installationFileList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "fileListItemId",
        "sourceURL",
        "path",
        "size",
        "tbd",
        "unzip",
        "mimeType",
        "dlCategory"
})
public class FileListItem {

    public static final String FLASH_CARD = "FC";
    public static final String SPEAK_EASY = "VC";
    public static final String READING = "RL";
    public static final String QUICK_MATCH = "QZ";
    public static final String BONUS_PACK = "BP";

    @JsonProperty("fileListItemId")
    private Integer fileListItemId;
    @JsonProperty("sourceURL")
    private String sourceURL;
    @JsonProperty("path")
    private String path;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("tbd")
    private Integer tbd;
    @JsonProperty("unzip")
    private Integer unzip;
    @JsonProperty("mimeType")
    private String mimeType;
    @JsonProperty("dlCategory")
    private Integer dlCategory;

    @JsonProperty("fileListItemId")
    public Integer getFileListItemId() {
        return fileListItemId;
    }

    @JsonProperty("fileListItemId")
    public void setFileListItemId(Integer fileListItemId) {
        this.fileListItemId = fileListItemId;
    }

    @JsonProperty("sourceURL")
    public String getSourceURL() {
        return sourceURL;
    }

    @JsonProperty("sourceURL")
    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("tbd")
    public Integer getTbd() {
        return tbd;
    }

    @JsonProperty("tbd")
    public void setTbd(Integer tbd) {
        this.tbd = tbd;
    }

    @JsonProperty("unzip")
    public Integer getUnzip() {
        return unzip;
    }

    @JsonProperty("unzip")
    public void setUnzip(Integer unzip) {
        this.unzip = unzip;
    }

    @JsonProperty("mimeType")
    public String getMimeType() {
        return mimeType;
    }

    @JsonProperty("mimeType")
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JsonProperty("dlCategory")
    public Integer getDlCategory() {
        return dlCategory;
    }

    @JsonProperty("dlCategory")
    public void setDlCategory(Integer dlCategory) {
        this.dlCategory = dlCategory;
    }

    public String getFullUrl() {
        return this.sourceURL +
                // pu free lessons have encrypted csv files, just remove the encr part
                // then we have an url that point to unencrypted version of the csv
                this.path.replace(".csv.encr", ".csv");
    }

    public String getReviewAudioBaseUrl() {
        return this.getFullUrl().replace(".zip", "_SNIPPETS/");
    }
}
