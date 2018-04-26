
package com.simonschuster.pimsleur.unlimited.data.edt.installationFileList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesCsvLocations;

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

    public PracticesCsvLocations getPracticeCsvLocations() {
        PracticesCsvLocations practicesCsvLocations = new PracticesCsvLocations();
        if (this.getResultData() != null) {
            this.getResultData().getFileList().getFileListItems().forEach((fileListItem) -> {
                if (fileListItem.isCsvFileOf(FlashCard)) {
                    practicesCsvLocations.setFlashCardUrl(fileListItem.getFullUrl());
                } else if (fileListItem.isCsvFileOf(QuickMatch)) {
                    practicesCsvLocations.setQuickMatchUrl(fileListItem.getFullUrl());
                } else if (fileListItem.isCsvFileOf(Reading)) {
                    practicesCsvLocations.setReadingUrl(fileListItem.getFullUrl());
                } else if (fileListItem.isCsvFileOf(SpeakEasy)) {
                    practicesCsvLocations.setSpeakEasyUrl(fileListItem.getFullUrl());
                }
            });
        }
        return practicesCsvLocations;
    }
}
