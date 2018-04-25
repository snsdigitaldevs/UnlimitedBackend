package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import java.util.List;

public class Lesson {
    private Image image;
    private String name;
    private String audioLink;
    private Integer level;
    private String lessonNumber;
    private String imageDescription;

    public String getImageDescription() {return imageDescription;}

    public void setImageDescription(String imageDescription) {this.imageDescription = imageDescription; }

    public Image getImage() {
        return image;
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

    public void setImage(Image image) {
        this.image = image;
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
