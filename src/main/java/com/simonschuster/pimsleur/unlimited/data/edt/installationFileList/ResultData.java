
package com.simonschuster.pimsleur.unlimited.data.edt.installationFileList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fileList"
})
public class ResultData {

    @JsonProperty("fileList")
    private FileList fileList;

    @JsonProperty("fileList")
    public FileList getFileList() {
        return fileList;
    }

    @JsonProperty("fileList")
    public void setFileList(FileList fileList) {
        this.fileList = fileList;
    }

}
