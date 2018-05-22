
package com.simonschuster.pimsleur.unlimited.data.edt.installationFileList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesUrls;

import static com.simonschuster.pimsleur.unlimited.data.edt.installationFileList.FileListItem.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "result_data",
        "result_code"
})
public class InstallationFileList {

    @JsonProperty("result_data")
    private ResultData resultData;
    @JsonProperty("result_code")
    private Integer resultCode;

    @JsonProperty("result_data")
    public ResultData getResultData() {
        return resultData;
    }

    @JsonProperty("result_data")
    public void setResultData(ResultData resultData) {
        this.resultData = resultData;
    }

    @JsonProperty("result_code")
    public Integer getResultCode() {
        return resultCode;
    }

    @JsonProperty("result_code")
    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    // there is a potential bug in this code
    // if the product code is a kitted product like "spanish level 1-4", then installation file list
    // will include all csv files of all levels
    // this code will be correct if the front end only send requests with single product code, not kitted
    public PracticesUrls getPracticeUrls() {
        PracticesUrls practicesUrls = new PracticesUrls();
        if (this.getResultData() != null) {
            this.getResultData().getFileList().getFileListItems().forEach((fileListItem) -> {
                if (fileListItem.isCsvFileOf(FlashCard)) {
                    practicesUrls.setFlashCardUrl(fileListItem.getFullUrl());
                } else if (fileListItem.isCsvFileOf(QuickMatch)) {
                    practicesUrls.setQuickMatchUrl(fileListItem.getFullUrl());
                } else if (fileListItem.isCsvFileOf(Reading)) {
                    practicesUrls.setReadingUrl(fileListItem.getFullUrl());
                } else if (fileListItem.isCsvFileOf(SpeakEasy)) {
                    practicesUrls.setSpeakEasyUrl(fileListItem.getFullUrl());
                } else if (fileListItem.isReviewAudioZipFile()) {
                    practicesUrls.setReviewAudioBaseUrl(fileListItem.getReviewAudioBaseUrl());
                }
            });
        }
        return practicesUrls;
    }
}
