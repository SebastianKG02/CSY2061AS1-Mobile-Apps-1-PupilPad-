package com.github.sebastiankg02.csy2061.as1.user;

import com.github.sebastiankg02.csy2061.as1.user.classroom.KeyStage;
import com.github.sebastiankg02.csy2061.as1.user.quiz.QuizQuestionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserPersonalProfile {
    private String[] name;
    private LocalDate birthdate;
    private LocalDateTime lastLogin;
    private KeyStage stage;
    private int age;

    public UserPersonalProfile(UserPersonalProfile other){
        this.name = other.name;
        this.birthdate = other.birthdate;
        this.lastLogin = other.lastLogin;
        this.stage = other.stage;
        this.age = other.age;
    }

    public UserPersonalProfile(String[] name, LocalDate birthdate, LocalDateTime lastLogin, KeyStage stage, int age){
        this.name = name;
        this.birthdate = birthdate;
        this.lastLogin = lastLogin;
        this.stage = stage;
        this.age = age;
    }

    public String[] getName(){
        return this.name;
    }

    public LocalDate getBirthdate(){
        return this.birthdate;
    }

    public boolean isBirthday(){
        if(birthdate.getMonth().compareTo(LocalDate.now().getMonth()) == 0
           && birthdate.getDayOfMonth() == LocalDate.now().getDayOfMonth()){
            return true;
        } else {
            return false;
        }
    }

    public String getNameAsSingleString(){
        String output = "";

        for(String s: name){
            output += s + " ";
        }

        return output.trim();
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime newLoginTime){
        this.lastLogin = newLoginTime;
    }

    public KeyStage getKeyStage(){
        return this.stage;
    }

    public void setKeyStage(KeyStage stage){
        this.stage = stage;
    }
}
