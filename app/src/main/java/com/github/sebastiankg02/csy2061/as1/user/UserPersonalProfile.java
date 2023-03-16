package com.github.sebastiankg02.csy2061.as1.user;

import com.github.sebastiankg02.csy2061.as1.user.classroom.KeyStage;
import com.github.sebastiankg02.csy2061.as1.user.quiz.QuizQuestionType;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class UserPersonalProfile {
    private String[] name;
    private LocalDate birthdate;
    private LocalDateTime lastLogin;
    private KeyStage stage;
    private int age;

    private DateTimeFormatter formatterDate;
    private DateTimeFormatter formatterDateTime;

    private void initFormatters(){
        formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        formatterDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    }

    public UserPersonalProfile(UserPersonalProfile other){
        initFormatters();
        this.name = other.name;
        this.birthdate = other.birthdate;
        this.lastLogin = other.lastLogin;
        this.stage = other.stage;
        this.age = other.age;
    }

    public UserPersonalProfile(String[] name, LocalDate birthdate, LocalDateTime lastLogin, KeyStage stage, int age){
        initFormatters();
        this.name = name;
        this.birthdate = birthdate;
        this.lastLogin = lastLogin;
        this.stage = stage;
        this.age = age;
    }

    public UserPersonalProfile(JSONObject j) throws JSONException {
        initFormatters();
        String t_name = j.getString("name");
        this.name = t_name.split(" ");
        if(j.getString("birthdate").isEmpty()) {
            this.birthdate = LocalDate.now();
        } else {
            this.birthdate = LocalDate.parse(j.getString("birthdate"), formatterDate);
        }
        this.lastLogin = LocalDateTime.from(formatterDateTime.parse(j.getString("last-login")));
        this.stage = KeyStage.fromEducationLevel(j.getInt("ks"));
        this.age = j.getInt("age");
    }

    public UserPersonalProfile(){
        initFormatters();
        this.name = new String[]{ "" };
        this.birthdate = LocalDate.now();
        this.lastLogin = LocalDateTime.now();
        this.stage = KeyStage.GUEST;
        this.age = 0;
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

    public JSONObject toJSON() throws JSONException {
        JSONObject output = new JSONObject();
        output.put("name", getNameAsSingleString());
        output.put("birthdate", formatterDate.format(birthdate));
        output.put("last-login", formatterDateTime.format(lastLogin));
        output.put("ks", stage.getEducationLevel());
        output.put("age", age);
        return output;
    }
}
