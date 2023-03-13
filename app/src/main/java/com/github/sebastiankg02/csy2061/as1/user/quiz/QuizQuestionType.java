package com.github.sebastiankg02.csy2061.as1.user.quiz;

public enum QuizQuestionType {
    NONE('n'),
    SINGLE_CHOICE('o'),
    TWO_CHOICE('t'),
    THREE_CHOICE('f'),
    SHORT_ANSWER('s'),
    LONG_ANSWER('l');

    private char typeCode;

    public final static QuizQuestionType[] all = {
            QuizQuestionType.NONE,
            QuizQuestionType.SINGLE_CHOICE,
            QuizQuestionType.TWO_CHOICE,
            QuizQuestionType.THREE_CHOICE,
            QuizQuestionType.SHORT_ANSWER,
            QuizQuestionType.LONG_ANSWER
    };

    QuizQuestionType(char typeCode){
        this.typeCode = typeCode;
    }

    public char getTypeCode(){
        return this.typeCode;
    }

    public static QuizQuestionType fromTypeCode(char toCompare){
        for (QuizQuestionType qqt: all) {
            if(qqt.typeCode == toCompare){
                return qqt;
            }
        }
        return null;
    }
}
