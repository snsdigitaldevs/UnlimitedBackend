package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@JsonInclude(NON_EMPTY)
public class ReadingAudio {
    private static final Logger LOG = LoggerFactory.getLogger(ReadingAudio.class);
    private static ObjectMapper mapper = new ObjectMapper();

    private static final String ESL_SPANISH_READING_LESSON = "Lectura lección";
    private static final String READING_LESSON = "Reading Lesson";
    public static final String CULTURE_NOTES = "Culture Notes";

    private Integer order;
    private String title;
    private String audioLink;
    private Integer startPage;
    private Integer pageCount;
    private Integer mediaItemId;

    private ReadingAudio(Integer order, String title, String audioLink,
                         Integer startPage, Integer pageCount, Integer mediaItemId) {
        this.order = order;
        this.title = title;
        this.audioLink = audioLink;
        this.startPage = startPage;
        this.pageCount = pageCount;
        this.mediaItemId = mediaItemId;
    }

    public ReadingAudio(Integer order, String audioLink) {
        this.order = order;
        this.audioLink = audioLink;
    }

    public Integer getOrder() {
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

    public Integer getStartPage() {
        return startPage;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public static ReadingAudio createFrom(String mediaItemTitle, String url,
                                          String mediaItemIdMetadata, Integer mediaItemId) {
        int startPage = 0;
        int pageCount = 0;
        if (mediaItemIdMetadata != null) {
            ReadingMetaData readingMetaData = getReadingMetaData(mediaItemIdMetadata);
            startPage = parseInt(readingMetaData.startPage);
            pageCount = parseInt(readingMetaData.pageCount);
        }

        return new ReadingAudio(titleToUnitNumber(mediaItemTitle), mediaItemTitle, url,
                startPage, pageCount, mediaItemId);
    }

    private static ReadingMetaData getReadingMetaData(String mediaItemIdMetadata) {
        ReadingMetaData readingMetaData = new ReadingMetaData();
        try {
            readingMetaData = mapper.readValue(mediaItemIdMetadata, new TypeReference<ReadingMetaData>() {
            });
        } catch (IOException exception) {
            LOG.warn(exception.getMessage());
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
                .replace(ESL_SPANISH_READING_LESSON, "")
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
        @JsonProperty("endPage")
        private String endPage;
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

        public String getEndPage() {
            return endPage;
        }

        public void setEndPage(String endPage) {
            this.endPage = endPage;
        }
    }

    @JsonIgnore
    public boolean isCultureNotes() {
        return this.title != null && this.title.contains(CULTURE_NOTES);
    }

    @JsonIgnore
    public boolean isReadingLesson() {
        return this.title != null && (this.title.contains(READING_LESSON) || this.title.contains(ESL_SPANISH_READING_LESSON));
    }
}
