package com.simonschuster.pimsleur.unlimited.data.edt.installationFileList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesUrls;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            List<FileListItem> list = this.getResultData().getFileList().getFileListItems();
            List<FileListItem> flashCardFiles = list.stream().filter(f -> isExpectedFiles(f, FlashCard)).collect(Collectors.toList());
            List<FileListItem> quickMatchFiles = list.stream().filter(f -> isExpectedFiles(f, QuickMatch)).collect(Collectors.toList());
            List<FileListItem> speakEasyFiles = list.stream().filter(f -> isExpectedFiles(f, SpeakEasy)).collect(Collectors.toList());
            List<FileListItem> readingFiles = list.stream().filter(f -> isExpectedFiles(f, Reading)).collect(Collectors.toList());
            List<FileListItem> ReviewAudioZipFiles = list.stream().filter(f -> f.getPath().contains("REVIEW_AUDIO.zip")).collect(Collectors.toList());

            FileListItem flashCardFile = filterFileByType(flashCardFiles, FlashCard);
            if (!flashCardFile.equals(null)) {
                practicesUrls.setFlashCardUrl(flashCardFile.getFullUrl());
            }

            FileListItem quickMatchFile = filterFileByType(quickMatchFiles, QuickMatch);
            if (!quickMatchFile.equals(null)) {
                practicesUrls.setQuickMatchUrl(quickMatchFile.getFullUrl());
            }

            FileListItem speakEasyFile = filterFileByType(speakEasyFiles, SpeakEasy);
            if (!speakEasyFile.equals(null)) {
                practicesUrls.setSpeakEasyUrl(speakEasyFile.getFullUrl());
            }

            FileListItem readingFile = filterFileByType(readingFiles, Reading);
            if (!readingFile.equals(null)) {
                practicesUrls.setReadingUrl(readingFile.getFullUrl());
            }

            if (ReviewAudioZipFiles.size() > 0) {
                practicesUrls.setReviewAudioBaseUrl(ReviewAudioZipFiles.get(0).getReviewAudioBaseUrl());
            } else {
                List<FileListItem> audioFiles = list.stream().filter(f -> isAudioFiles(f)).collect(Collectors.toList());
                practicesUrls.setReviewAudioBaseUrl(getReviewAudioBaseUrl(audioFiles.get(0)));
            }
        }
        return practicesUrls;
    }

    private boolean isAudioFiles(FileListItem f) {
        return f.getPath().contains(".mp3") && f.getMimeType().equals("audio-mp3");
    }

    private String getReviewAudioBaseUrl(FileListItem audioFileListItem) {
        String[] basePathItems = audioFileListItem.getPath().split("/");
        if (basePathItems.length != 3) {
            return null;
        }
        String lastItem = basePathItems[basePathItems.length - 1];
        String productCode = "";
        if (!StringUtils.isEmpty(lastItem)) {
            productCode = lastItem.split("_")[0];
        } else {
            new RuntimeException("error, can't parse the product code ");
        }
        String path = String.format("%s/%s/%s_REVIEW_AUDIO_SNIPPETS/", basePathItems[0], basePathItems[1], productCode);
        return audioFileListItem.getSourceURL() + path;
    }

    private boolean isExpectedFiles(FileListItem f, String fileType) {
        return f.getPath().contains(fileType + ".csv");
    }

    FileListItem filterFileByType(List<FileListItem> files, String fileType) {
        Optional<FileListItem> first =
                files.stream().filter(f -> f.getPath().endsWith(fileType + ".csv")).findFirst();
        if (files.size() == 1 || !first.isPresent()) {
            return files.get(0);
        } else {
            return first.get();
        }
    }
}
