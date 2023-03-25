package com.github.sebastiankg02.csy2061.as1.quiz;

public class QuizAnswer {
    //Answer text
    public String content;
    //Determines whether or not this answer upon selection in QuizPlay will grant the user a point
    public boolean isCorrect;

    //Default constructor
    public QuizAnswer() {
        this.content = "";
        this.isCorrect = false;
    }

    //Custom constructor
    public QuizAnswer(String content, boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    //Static method to create a new custom answer
    public static QuizAnswer makeAnswer(String s, boolean correct) {
        return new QuizAnswer(s, correct);
    }
}