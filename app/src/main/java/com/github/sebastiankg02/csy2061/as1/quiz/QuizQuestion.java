package com.github.sebastiankg02.csy2061.as1.quiz;

public class QuizQuestion {
    public QuizAnswer[] answers;
    public String question;

    public QuizQuestion(String q, QuizAnswer[] a) {
        this.question = q;
        this.answers = new QuizAnswer[]{};
        this.answers = a;
    }

    public static QuizQuestion makeQuestion(String q, QuizAnswer[] a) {
        return new QuizQuestion(q, a);
    }

    public int getNumberAnswers() {
        return answers.length;
    }
}