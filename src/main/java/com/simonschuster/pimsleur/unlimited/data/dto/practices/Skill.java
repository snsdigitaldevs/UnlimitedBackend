package com.simonschuster.pimsleur.unlimited.data.dto.practices;

import java.util.List;

public class Skill {

    private QuickMatchItem question;
    private QuickMatchItem answer;
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public QuickMatchItem getQuestion() {
        return question;
    }

    public void setQuestion(QuickMatchItem question) {
        this.question = question;
    }

    public QuickMatchItem getAnswer() {
        return answer;
    }

    public void setAnswer(QuickMatchItem answer) {
        this.answer = answer;
    }

    public boolean completed() {
        return this.answer != null && this.question != null;
    }

}
