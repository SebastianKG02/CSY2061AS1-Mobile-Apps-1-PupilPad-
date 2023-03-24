package com.github.sebastiankg02.csy2061.as1.user;

import android.util.Log;

import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserPersonalProfile {
    private String[] name;
    private LocalDate birthdate;
    private LocalDateTime lastLogin;
    private KeyStage stage;
    private int age;

    public UserPersonalProfile(UserPersonalProfile other) {
        this.name = other.name;
        this.birthdate = other.birthdate;
        this.lastLogin = other.lastLogin;
        this.stage = other.stage;
        this.age = other.age;
    }

    public UserPersonalProfile(String[] name, LocalDate birthdate, LocalDateTime lastLogin, KeyStage stage, int age) {
        this.name = name;
        this.birthdate = birthdate;
        this.lastLogin = lastLogin;
        this.stage = stage;
        this.age = age;
    }

    public UserPersonalProfile(JSONObject j) throws JSONException {
        String t_name = j.getString("name");
        this.name = t_name.split(" ");
        if (j.getString("birthdate").isEmpty()) {
            this.birthdate = LocalDate.now();
        } else {
            this.birthdate = LocalDate.parse(j.getString("birthdate"), AppHelper.formatterDate);
        }
        this.lastLogin = LocalDateTime.from(AppHelper.formatterDateTime.parse(j.getString("last-login")));
        this.stage = KeyStage.fromEducationLevel(j.getInt("ks"));
        this.age = j.getInt("age");
    }

    public UserPersonalProfile() {
        this.name = new String[]{""};
        this.birthdate = LocalDate.now();
        this.lastLogin = LocalDateTime.now();
        this.stage = KeyStage.GUEST;
        this.age = 0;
    }

    public String[] getName() {
        return this.name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public String getBirthdateAsString() {
        return AppHelper.formatterDate.format(birthdate);
    }

    public boolean setBirthdate(String birthdateRaw) {
        try {
            this.birthdate = LocalDate.parse(birthdateRaw, AppHelper.formatterDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBirthday() {
        if (birthdate.getMonth().compareTo(LocalDate.now().getMonth()) == 0
                && birthdate.getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
            return true;
        } else {
            return false;
        }
    }

    public String getNameAsSingleString() {
        String output = "";

        for (String s : name) {
            output += s + " ";
        }

        return output.trim();
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime newLoginTime) {
        this.lastLogin = newLoginTime;
    }

    public KeyStage getKeyStage() {
        return this.stage;
    }

    public void setKeyStage(KeyStage stage) {
        this.stage = stage;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject output = new JSONObject();
        output.put("name", getNameAsSingleString());
        output.put("birthdate", AppHelper.formatterDate.format(birthdate));
        output.put("last-login", AppHelper.formatterDateTime.format(lastLogin));
        output.put("ks", stage.getEducationLevel());
        output.put("age", age);
        return output;
    }

    public boolean setName(String singleStringName) {
        try {
            String[] output = singleStringName.trim().split(" ");
            output[output.length - 1].trim();
            this.name = output;
        } catch (Exception e) {
            Log.e("UAC", "Error changing name. " + e.getMessage());
            return false;
        } finally {
            return true;
        }
    }
}
