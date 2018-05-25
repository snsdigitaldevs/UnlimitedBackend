package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@JsonInclude(NON_EMPTY)
public class PcmReadingAudio {
    private static ObjectMapper mapper = new ObjectMapper();

    private static final String READING_LESSON = "Reading Lesson";
    public static final String CULTURE_NOTES = "Culture Notes";

    private int order;
    private String title;
    private String audioLink;
    private int startPage;
    private int pageCount;
    private Integer mediaItemId;

    public PcmReadingAudio(int order, String title, String audioLink,
                           int startPage, int pageCount, Integer mediaItemId) {
        this.order = order;
        this.title = title;
        this.audioLink = audioLink;
        this.startPage = startPage;
        this.pageCount = pageCount;
        this.mediaItemId = mediaItemId;
    }

    public int getOrder() {
        return order;
    }

    public Integer getMediaItemId() {
        return mediaItemId;
    }

    public String getTitle() {
        return title;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public static PcmReadingAudio createFrom(String mediaItemTitle, String url,
                                             String mediaItemIdMetadata, int mediaItemId) {
        ReadingMetaData readingMetaData = getReadingMetaData(mediaItemIdMetadata);
        return new PcmReadingAudio(titleToUnitNumber(mediaItemTitle), mediaItemTitle, url,
                parseInt(readingMetaData.startPage), parseInt(readingMetaData.pageCount), mediaItemId);
    }

    private static ReadingMetaData getReadingMetaData(String mediaItemIdMetadata) {
        ReadingMetaData readingMetaData = new ReadingMetaData();
        try {
            readingMetaData = mapper.readValue(mediaItemIdMetadata, new TypeReference<ReadingMetaData>() {
            });
        } catch (IOException ignored) {
        }
        if (!isNumeric(readingMetaData.getStartPage())) {
            readingMetaData.setStartPage("0");
        }
        if (!isNumeric(readingMetaData.getPageCount())) {
            readingMetaData.setPageCount("0");
        }
        return readingMetaData;
    }

    private static Integer titleToUnitNumber(String title) {
        String unit = title
                .replace(READING_LESSON, "")
                .replace(CULTURE_NOTES, "")
                .replace(" ", "");
        try {
            return parseInt(unit);
        } catch (NumberFormatException e) {
            return MIN_VALUE;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "startPage",
            "pageCount",
            "processingRules"
    })
    public static class ReadingMetaData {

        @JsonProperty("startPage")
        private String startPage;
        @JsonProperty("pageCount")
        private String pageCount;
        @JsonProperty("processingRules")
        private List<String> processingRules = null;

        @JsonProperty("startPage")
        public String getStartPage() {
            return startPage;
        }

        @JsonProperty("startPage")
        public void setStartPage(String startPage) {
            this.startPage = startPage;
        }

        @JsonProperty("pageCount")
        public String getPageCount() {
            return pageCount;
        }

        @JsonProperty("pageCount")
        public void setPageCount(String pageCount) {
            this.pageCount = pageCount;
        }

        @JsonProperty("processingRules")
        public List<String> getProcessingRules() {
            return processingRules;
        }

        @JsonProperty("processingRules")
        public void setProcessingRules(List<String> processingRules) {
            this.processingRules = processingRules;
        }

    }
}
