
package com.simonschuster.pimsleur.unlimited.data.edt.installationFileList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "fileListId",
        "productCode",
        "enabled",
        "deployType",
        "minAppVersion",
        "kittedFileListsString",
        "fileListItems",
        "totalPayloadBytes"
})
public class FileList {

    @JsonProperty("fileListId")
    private Integer fileListId;
    @JsonProperty("productCode")
    private String productCode;
    @JsonProperty("enabled")
    private Integer enabled;
    @JsonProperty("deployType")
    private String deployType;
    @JsonProperty("minAppVersion")
    private String minAppVersion;
    @JsonProperty("kittedFileListsString")
    private String kittedFileListsString;
    @JsonProperty("fileListItems")
    private List<FileListItem> fileListItems = null;
    @JsonProperty("totalPayloadBytes")
    private Long totalPayloadBytes;

    @JsonProperty("fileListId")
    public Integer getFileListId() {
        return fileListId;
    }

    @JsonProperty("fileListId")
    public void setFileListId(Integer fileListId) {
        this.fileListId = fileListId;
    }

    @JsonProperty("productCode")
    public String getProductCode() {
        return productCode;
    }

    @JsonProperty("productCode")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @JsonProperty("enabled")
    public Integer getEnabled() {
        return enabled;
    }

    @JsonProperty("enabled")
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    @JsonProperty("deployType")
    public String getDeployType() {
        return deployType;
    }

    @JsonProperty("deployType")
    public void setDeployType(String deployType) {
        this.deployType = deployType;
    }

    @JsonProperty("minAppVersion")
    public String getMinAppVersion() {
        return minAppVersion;
    }

    @JsonProperty("minAppVersion")
    public void setMinAppVersion(String minAppVersion) {
        this.minAppVersion = minAppVersion;
    }

    @JsonProperty("kittedFileListsString")
    public String getKittedFileListsString() {
        return kittedFileListsString;
    }

    @JsonProperty("kittedFileListsString")
    public void setKittedFileListsString(String kittedFileListsString) {
        this.kittedFileListsString = kittedFileListsString;
    }

    @JsonProperty("fileListItems")
    public List<FileListItem> getFileListItems() {
        return fileListItems;
    }

    @JsonProperty("fileListItems")
    public void setFileListItems(List<FileListItem> fileListItems) {
        this.fileListItems = fileListItems;
    }

    @JsonProperty("totalPayloadBytes")
    public Long getTotalPayloadBytes() {
        return totalPayloadBytes;
    }

    @JsonProperty("totalPayloadBytes")
    public void setTotalPayloadBytes(Long totalPayloadBytes) {
        this.totalPayloadBytes = totalPayloadBytes;
    }

}
