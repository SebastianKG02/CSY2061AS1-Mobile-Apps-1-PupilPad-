package com.github.sebastiankg02.csy2061.as1.quiz;

import java.util.ArrayList;
import java.util.UUID;

public class Quiz {
    public String quizName;
    public ArrayList<QuizQuestion> questions;
    private UUID quizID;

    public Quiz(String name, QuizQuestion[] content) {
        this.quizName = name;
        this.questions = new ArrayList<QuizQuestion>();

        for (QuizQuestion qq : content) {
            questions.add(qq);
        }
    }

    public Quiz(Quiz other) {
        this.quizName = other.quizName;
        this.questions = other.questions;
    }

    public QuizQuestion generateQuestion(String content, QuizAnswer[] answers) {
        return new QuizQuestion(content, answers);
    }

    public QuizAnswer generateAnswer(String content, boolean correct) {
        return new QuizAnswer(content, correct);
    }
}
