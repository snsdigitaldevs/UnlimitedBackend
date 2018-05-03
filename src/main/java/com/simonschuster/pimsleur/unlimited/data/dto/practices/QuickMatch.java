package com.simonschuster.pimsleur.unlimited.data.dto.practices;

public class QuickMatch {
    private QuickMatchItem question;
    private QuickMatchItem answer;
    private String group;
    private boolean isQuestions;


    public QuickMatch(String group) {
        this.group = group;
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

    public boolean getCompleted() {
        return this.answer != null && this.question != null;
    }
}
