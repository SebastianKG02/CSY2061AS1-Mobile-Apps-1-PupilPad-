package com.github.sebastiankg02.csy2061.as1.quiz;

//A simple class to store question data
public class QuizQuestion {
    //Array of answers for this question
    public QuizAnswer[] answers;
    //Actual question text
    public String question;

    //Full custom constructor
    public QuizQuestion(String q, QuizAnswer[] a) {
        this.question = q;
        this.answers = new QuizAnswer[]{};
        this.answers = a;
    }

    //Static method for custom constructor
    public static QuizQuestion makeQuestion(String q, QuizAnswer[] a) {
        return new QuizQuestion(q, a);
    }
}