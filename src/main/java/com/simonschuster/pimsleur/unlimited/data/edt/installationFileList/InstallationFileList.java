package com.simonschuster.pimsleur.unlimited.data.edt.installationFileList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.simonschuster.pimsleur.unlimited.data.edt.EdtResponseCode;
import com.simonschuster.pimsleur.unlimited.services.bonusPacks.BonusPacksUrls;
import com.simonschuster.pimsleur.unlimited.services.practices.PracticesUrls;
import com.simonschuster.pimsleur.unlimited.utils.UrlUtil;
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
public class InstallationFileList extends EdtResponseCode {

    @JsonProperty("result_data")
    private ResultData resultData;

    @JsonProperty("result_data")
    public ResultData getResultData() {
        return resultData;
    }

    @JsonProperty("result_data")
    public void setResultData(ResultData resultData) {
        this.resultData = resultData;
    }

    // there is a potential bug in this code
    // if the product code is a kitted product like "spanish level 1-4", then installation file list
    // will include all csv files of all levels
    // this code will be correct if the front end only send requests with single product code, not kitted
    public PracticesUrls collectPracticeUrls() {
        PracticesUrls practicesUrls = new PracticesUrls();
        if (this.getResultData() != null) {
            List<FileListItem> list = this.getResultData().getFileList().getFileListItems();

            FileListItem flashCardFile = getFileItemByFileType(list, FLASH_CARD);
            if (flashCardFile != null) {
                practicesUrls.setFlashCardUrl(flashCardFile.getFullUrl());
            }

            FileListItem quickMatchFile = getFileItemByFileType(list, QUICK_MATCH);
            if (quickMatchFile != null) {
                practicesUrls.setQuickMatchUrl(quickMatchFile.getFullUrl());
            }

            FileListItem speakEasyFile = getFileItemByFileType(list, SPEAK_EASY);
            if (speakEasyFile != null) {
                practicesUrls.setSpeakEasyUrl(speakEasyFile.getFullUrl());
            }

            FileListItem readingFile = getFileItemByFileType(list, READING);
            if (readingFile != null) {
                practicesUrls.setReadingUrl(readingFile.getFullUrl());
            }

            String reviewAudioBaseUrl = getReviewAudioBaseUrl();
            practicesUrls.setReviewAudioBaseUrl(reviewAudioBaseUrl);
        }
        return practicesUrls;
    }

    public BonusPacksUrls getBonusPacksUrls() {
        if (this.getResultData() != null) {
            String bonusPackFileUrl = getBonusPackFileUrl();
            String reviewAudioBaseUrl = getReviewAudioBaseUrl();
            if (StringUtils.isNotBlank(bonusPackFileUrl) && StringUtils
                .isNotBlank(reviewAudioBaseUrl)) {
                return new BonusPacksUrls(bonusPackFileUrl, reviewAudioBaseUrl);
            }
        }
        return null;
    }

    public String getUrlByFileName(String fileName) {
        if (this.getResultData() != null) {
            FileListItem fileListItem = this.resultData.getFileList().getFileListItems().stream()
                .filter(fileItem -> fileItem.getPath().endsWith(fileName)).findFirst()
                .orElse(null);
            if (fileListItem != null) {
                return fileListItem.getSourceURL().concat(UrlUtil.encodeUrl(fileListItem.getPath()));
            }
        }
        return null;

    }

    private String getReviewAudioBaseUrl() {
        List<FileListItem> fileListItems = this.getResultData().getFileList().getFileListItems();
        List<FileListItem> ReviewAudioZipFiles = fileListItems.stream().filter(f -> f.getPath().contains("REVIEW_AUDIO.zip")).collect(Collectors.toList());

        if (ReviewAudioZipFiles.size() > 0) {
            return ReviewAudioZipFiles.get(0).getReviewAudioBaseUrl();
        } else {
            List<FileListItem> audioFiles = fileListItems.stream().filter(this::isAudioFiles).collect(Collectors.toList());
            return getReviewAudioBaseUrlFromAudioFiles(audioFiles.get(0));
        }
    }

    private boolean isAudioFiles(FileListItem f) {
        return f.getPath().contains(".mp3") && f.getMimeType().equals("audio-mp3");
    }

    private String getReviewAudioBaseUrlFromAudioFiles(FileListItem audioFileListItem) {
        String[] basePathItems = audioFileListItem.getPath().split("/");
        if (basePathItems.length != 3) {
            return null;
        }
        String lastItem = basePathItems[basePathItems.length - 1];
        String productCode;
        if (!StringUtils.isEmpty(lastItem)) {
            productCode = lastItem.split("_")[0];
        } else {
            throw new RuntimeException("error, can't parse the product code ");
        }
        String path = String.format("%s/%s/%s_REVIEW_AUDIO_SNIPPETS/", basePathItems[0], basePathItems[1], productCode);
        return audioFileListItem.getSourceURL() + path;
    }

    private FileListItem getFileItemByFileType(List<FileListItem> allListItemList,
                                               String fileType) {
        List<FileListItem> fileListItems = allListItemList.stream()
                .filter(fileListItem -> isContainedFileType(fileListItem, fileType)).collect(
                        Collectors.toList());
        Optional<FileListItem> firstEndWithFileTypeItem = fileListItems.stream().filter(fileListItem -> isEndFileType(fileListItem, fileType))
                .findFirst();
        if (firstEndWithFileTypeItem.isPresent()) {
            return firstEndWithFileTypeItem.orElse(null);
        } else {
            return fileListItems.stream().findFirst().orElse(null);
        }
    }

    private boolean isEndFileType(FileListItem f, String fileType) {
        return f.getPath().endsWith(fileType + ".csv");
    }


    private boolean isContainedFileType(FileListItem f, String fileType) {
        return f.getPath().contains(fileType + ".csv");
    }

    public String getReadingPdfUrlByFileName(String readingIntroPdfName) {
        if (this.getResultData() != null) {
            FileListItem fileListItem = this.resultData.getFileList().getFileListItems().stream()
                    .filter(this::isPdfFiles)
                    .filter(fileItem -> fileItem.getPath().contains(readingIntroPdfName)).findFirst()
                    .orElse(null);
            if (fileListItem != null) {
                return fileListItem.getSourceURL().concat(fileListItem.getPath());
            }
        }
        return null;
    }

    private boolean isPdfFiles(FileListItem fileListItem) {
        return fileListItem.getPath().endsWith(".pdf") && fileListItem.getMimeType().equals("pdf");
    }

    private String getBonusPackFileUrl() {
        List<FileListItem> fileListItems = this.resultData.getFileList().getFileListItems();
        FileListItem bonusPacksFile = getFileItemByFileType(fileListItems, BONUS_PACK);
        if (bonusPacksFile != null) {
            return bonusPacksFile.getFullUrl();
        }
        return null;
    }
}
