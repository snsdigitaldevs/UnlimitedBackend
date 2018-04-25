package com.simonschuster.pimsleur.unlimited.data.dto.productinfo;

import java.util.List;

public class Course {
    private String languageName;
    private Integer level;
    private List<Lesson> lessons;

    public String getLanguageName() {
        return languageName;
    }

    public Integer getLevel() {
        return level;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
