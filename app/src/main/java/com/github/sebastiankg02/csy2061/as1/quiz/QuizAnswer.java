package com.github.sebastiankg02.csy2061.as1.quiz;

public class QuizAnswer {
    public String content;
    public boolean isCorrect;

    public QuizAnswer() {
        this.content = "";
        this.isCorrect = false;
    }

    public QuizAnswer(String content, boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public static QuizAnswer makeAnswer(String s, boolean correct) {
        return new QuizAnswer(s, correct);
    }
}