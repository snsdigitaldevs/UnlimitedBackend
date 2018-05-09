package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"qz"})
public class QuickMatch {
    private QuickMatchItem question;
    private QuickMatchItem answer;
    private String group;
    private boolean isQuestions;
    private List<String> skills;
    private String qz;

    public QuickMatch(String group) {
        this.group = group;
        this.skills = new ArrayList<>();
    }

    public void setQuestions(boolean questions) {
        isQuestions = questions;
    }

    public void setQuestion(QuickMatchItem question) {
        this.question = question;
    }

    public void setAnswer(QuickMatchItem answer) {
        this.answer = answer;
    }

    public boolean completed() {
        return this.answer != null && this.question != null;
    }

    public QuickMatchItem getQuestion() {
        return question;
    }

    public QuickMatchItem getAnswer() {
        return answer;
    }

    public String getGroup() {
        return group;
    }

    public boolean isQuestions() {
        return isQuestions;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getQz() {
        return qz;
    }

    public void setQz(String qz) {
        this.qz = qz;
    }
}
