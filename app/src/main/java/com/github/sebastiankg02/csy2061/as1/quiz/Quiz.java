package com.github.sebastiankg02.csy2061.as1.quiz;

import java.util.ArrayList;

//A simple class that holds questions
public class Quiz {
    //Name of the quiz (used in JSON serialization and display)
    public String quizName;
    //Actual questions that form this quiz
    public ArrayList<QuizQuestion> questions;

    //Full custom constructor
    public Quiz(String name, QuizQuestion[] content) {
        //Initialise variables
        this.quizName = name;
        this.questions = new ArrayList<QuizQuestion>();

        //Loop through QuizQuestion array and add to array list
        for (QuizQuestion qq : content) {
            questions.add(qq);
        }
    }

    //Copy constructor
    public Quiz(Quiz other) {
        this.quizName = other.quizName;
        this.questions = other.questions;
    }
}
