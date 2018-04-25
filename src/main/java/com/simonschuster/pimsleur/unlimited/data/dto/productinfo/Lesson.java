package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import java.util.List;

public class Lesson {
    private List<Image> images;
    private String name;
    private String audioLink;
    private Integer level;
    private String lessonNumber;

    public List<Image> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public Integer getLevel() {
        return level;
    }

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }
}
