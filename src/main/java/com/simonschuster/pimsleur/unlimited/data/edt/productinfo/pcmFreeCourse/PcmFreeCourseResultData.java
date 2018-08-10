package com.simonschuster.pimsleur.unlimited.data.edt.productinfo.pcmFreeCourse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Image;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.LanguageImageMetadata;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.LanguageName;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.MediaSet;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "productsId",
        "productsAudioUrl",
        "productsImage",
        "productsName",
        "productsLanguageName",
        "productsLevel",
        "productsMedia",
        "productsTotalLessons",
        "productsLessonRange",
        "productsHighestLevelAvail",
        "productsNumMedia",
        "productsMediaSize",
        "productsTitle",
        "mediaSet",
        "additionalProductData"
})

public class PcmFreeCourseResultData {
    private static final String PCM_IMAGE_DOMAIN = "https://public.pimsleur.cdn.edtnet.us";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(ALLOW_SINGLE_QUOTES, true);

    @JsonProperty("productsId")
    private long productsId;
    @JsonProperty("productsAudioUrl")
    private String productsAudioUrl;
    @JsonProperty("productsImage")
    private String productsImage;
    @JsonProperty("productsName")
    private String productsName;
    @JsonProperty("productsLanguageName")
    private String productsLanguageName;
    @JsonProperty("productsLevel")
    private long productsLevel;
    @JsonProperty("productsMedia")
    private String productsMedia;
    @JsonProperty("productsTotalLessons")
    private long productsTotalLessons;
    @JsonProperty("productsLessonRange")
    private String productsLessonRange;
    @JsonProperty("productsHighestLevelAvail")
    private long productsHighestLevelAvail;
    @JsonProperty("productsNumMedia")
    private long productsNumMedia;
    @JsonProperty("productsMediaSize")
    private String productsMediaSize;
    @JsonProperty("productsTitle")
    private String productsTitle;
    @JsonProperty("mediaSet")
    private MediaSet mediaSet;
    @JsonProperty("additionalProductData")
    private AdditionalProductData additionalProductData;
    @JsonProperty("languageName")
    private LanguageName languageName;

    @JsonProperty("productsId")
    public long getProductsId() {
        return productsId;
    }

    @JsonProperty("productsId")
    public void setProductsId(long productsId) {
        this.productsId = productsId;
    }

    @JsonProperty("productsAudioUrl")
    public String getProductsAudioUrl() {
        return productsAudioUrl;
    }

    @JsonProperty("productsAudioUrl")
    public void setProductsAudioUrl(String productsAudioUrl) {
        this.productsAudioUrl = productsAudioUrl;
    }

    @JsonProperty("productsImage")
    public String getProductsImage() {
        return productsImage;
    }

    @JsonProperty("productsImage")
    public void setProductsImage(String productsImage) {
        this.productsImage = productsImage;
    }

    @JsonProperty("productsName")
    public String getProductsName() {
        return productsName;
    }

    @JsonProperty("productsName")
    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    @JsonProperty("productsLanguageName")
    public String getProductsLanguageName() {
        return productsLanguageName;
    }

    @JsonProperty("productsLanguageName")
    public void setProductsLanguageName(String productsLanguageName) {
        this.productsLanguageName = productsLanguageName;
    }

    @JsonProperty("productsLevel")
    public long getProductsLevel() {
        return productsLevel;
    }

    @JsonProperty("productsLevel")
    public void setProductsLevel(long productsLevel) {
        this.productsLevel = productsLevel;
    }

    @JsonProperty("productsMedia")
    public String getProductsMedia() {
        return productsMedia;
    }

    @JsonProperty("productsMedia")
    public void setProductsMedia(String productsMedia) {
        this.productsMedia = productsMedia;
    }

    @JsonProperty("productsTotalLessons")
    public long getProductsTotalLessons() {
        return productsTotalLessons;
    }

    @JsonProperty("productsTotalLessons")
    public void setProductsTotalLessons(long productsTotalLessons) {
        this.productsTotalLessons = productsTotalLessons;
    }

    @JsonProperty("productsLessonRange")
    public String getProductsLessonRange() {
        return productsLessonRange;
    }

    @JsonProperty("productsLessonRange")
    public void setProductsLessonRange(String productsLessonRange) {
        this.productsLessonRange = productsLessonRange;
    }

    @JsonProperty("productsHighestLevelAvail")
    public long getProductsHighestLevelAvail() {
        return productsHighestLevelAvail;
    }

    @JsonProperty("productsHighestLevelAvail")
    public void setProductsHighestLevelAvail(long productsHighestLevelAvail) {
        this.productsHighestLevelAvail = productsHighestLevelAvail;
    }

    @JsonProperty("productsNumMedia")
    public long getProductsNumMedia() {
        return productsNumMedia;
    }

    @JsonProperty("productsNumMedia")
    public void setProductsNumMedia(long productsNumMedia) {
        this.productsNumMedia = productsNumMedia;
    }

    @JsonProperty("productsMediaSize")
    public String getProductsMediaSize() {
        return productsMediaSize;
    }

    @JsonProperty("productsMediaSize")
    public void setProductsMediaSize(String productsMediaSize) {
        this.productsMediaSize = productsMediaSize;
    }

    @JsonProperty("productsTitle")
    public String getProductsTitle() {
        return productsTitle;
    }

    @JsonProperty("productsTitle")
    public void setProductsTitle(String productsTitle) {
        this.productsTitle = productsTitle;
    }

    @JsonProperty("mediaSet")
    public MediaSet getMediaSet() {
        return mediaSet;
    }

    @JsonProperty("mediaSet")
    public void setMediaSet(MediaSet mediaSet) {
        this.mediaSet = mediaSet;
    }

    @JsonProperty("additionalProductData")
    public AdditionalProductData getAdditionalProductData() {
        return additionalProductData;
    }

    @JsonProperty("additionalProductData")
    public void setAdditionalProductData(AdditionalProductData additionalProductData) {
        this.additionalProductData = additionalProductData;
    }

    @JsonProperty("languageName")
    public LanguageName getLanguageName() {
        return languageName;
    }

    @JsonProperty("languageName")
    public void setLanguageName(LanguageName languageName) {
        this.languageName = languageName;
    }

    public Image getPcmImage() {
        Image image = new Image();
        if (languageName != null && languageName.getLanguageImageMetadata() != null) {
            try {
                LanguageImageMetadata metadata =
                        OBJECT_MAPPER.readValue(languageName.getLanguageImageMetadata(),
                        new TypeReference<LanguageImageMetadata>() {
                });
                String imageUrl = PCM_IMAGE_DOMAIN + metadata.getImageFilePath() + metadata.getImageFileName();

                image.setThumbImageAddress(imageUrl);
                image.setFullImageAddress(imageUrl);
                return image;
            } catch (IOException ignored) {
                return image;
            }
        }

        return image;
    }
}
