package com.github.sebastiankg02.csy2061.as1.user;

import android.util.Log;

import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
 * Simple class holding data about a user 
 */
public class UserPersonalProfile {
	//User full name (Supports any amount of sub-names)
    private String[] name;
    private LocalDate birthdate;
    private LocalDateTime lastLogin;
	//User key stage - used to provide "Year Group" information on UserProfileInformation fragment
    private KeyStage stage;
    private int age;

	//Copy constructor
    public UserPersonalProfile(UserPersonalProfile other) {
        this.name = other.name;
        this.birthdate = other.birthdate;
        this.lastLogin = other.lastLogin;
        this.stage = other.stage;
        this.age = other.age;
    }

	//Full custom constructor
    public UserPersonalProfile(String[] name, LocalDate birthdate, LocalDateTime lastLogin, KeyStage stage, int age) {
        this.name = name;
        this.birthdate = birthdate;
        this.lastLogin = lastLogin;
        this.stage = stage;
        this.age = age;
    }

	//JSON Constructor, creates UPP object based on JSON data provided
	//To be used with AppHelper.load
    public UserPersonalProfile(JSONObject j) throws JSONException {
        String t_name = j.getString("name");
		//Convert loaded JSON string into String array
        this.name = t_name.split(" ");
		
		//Check for birthdate entry
        if (j.getString("birthdate").isEmpty()) {
			//Generate new birthdate entry
            this.birthdate = LocalDate.now();
        } else {
			//Parse loaded JSON string into LocalDate
            this.birthdate = LocalDate.parse(j.getString("birthdate"), AppHelper.formatterDate);
        }
		
		//Get rest of UPP information
        this.lastLogin = LocalDateTime.from(AppHelper.formatterDateTime.parse(j.getString("last-login")));
        this.stage = KeyStage.fromEducationLevel(j.getInt("ks"));
        this.age = j.getInt("age");
    }

	//Default constructor - initalise fields and populate with blank data
    public UserPersonalProfile() {
        this.name = new String[]{""};
        this.birthdate = LocalDate.now();
        this.lastLogin = LocalDateTime.now();
        this.stage = KeyStage.GUEST;
        this.age = 0;
    }

	//BEGIN necessary getters & setters

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

	//Concat name array to single string (for use in UserProfile fragment edittext)
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

	//END necessary getters & setters

	//Convert object to raw JSON form
    public JSONObject toJSON() throws JSONException {
        JSONObject output = new JSONObject();
        output.put("name", getNameAsSingleString());
        output.put("birthdate", AppHelper.formatterDate.format(birthdate));
        output.put("last-login", AppHelper.formatterDateTime.format(lastLogin));
        output.put("ks", stage.getEducationLevel());
        output.put("age", age);
        return output;
    }

	//Set name based on single string - to be used with UserProfile fragment and loading EditText content
    public boolean setName(String singleStringName) {
        try {
			//Convert single string into string array, assign to name
            String[] output = singleStringName.trim().split(" ");
            this.name = output;
        } catch (Exception e) {
			//If an error occurs, forward issue to caller
            return false;
        } finally {
			//Default return
            return true;
        }
    }
}
