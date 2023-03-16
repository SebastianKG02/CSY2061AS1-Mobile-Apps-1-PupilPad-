package com.github.sebastiankg02.csy2061.as1.user;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.user.role.Role;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;
    private Role role;
    private UserPersonalProfile profile;

    public static final char[] passwordUppercaseChars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    public static final char[] passwordLowercaseChars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    public static final char[] passwordNumbers =   { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'  };
    public static final int passwordMinimumCharacters = 8;

    public User(){
        this(UUID.randomUUID(), "", "", Role.NONE);
    }

    public User(String username, String password, Role r){
        this(UUID.randomUUID(), username, password, r);
    }

    public User(UUID uuid, String username, String password, Role r){
        id = uuid;
        this.username = username;
        this.password = password;
        this.role = r;
        this.profile = new UserPersonalProfile();
    }

    public User(User other){
        this(other.id, other.username, other.password, other.role);
    }

    public User(JSONObject rawUser) throws JSONException {
        id = UUID.fromString(rawUser.getString("id"));
        this.username = rawUser.getString("user");
        this.password = rawUser.getString("pass");
        this.role = Role.fromInt(rawUser.getInt("role"));
        this.profile = new UserPersonalProfile(rawUser.getJSONObject("profile"));
    }

    public UUID getId(){
        return this.id;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public Role getRole(){
        return role;
    }

    public boolean setNewPassword(String newPassword){
        if(validatePassword(newPassword).isPasswordValid()){
            this.password = newPassword;
            return true;
        } else {
            return false;
        }
    }

    public PasswordValidationObject validatePassword(String passwordToCheck){
        return new PasswordValidationObject(
                checkForCharacter(passwordToCheck, passwordUppercaseChars),
                checkForCharacter(passwordToCheck, passwordLowercaseChars),
                checkForCharacter(passwordToCheck, passwordNumbers),
                (passwordToCheck.length() >= passwordMinimumCharacters)
        );
    }

    private boolean checkForCharacter(String toCheck, char[] queryCharacters){
        for(char c: queryCharacters){
            if(toCheck.contains(String.valueOf(c))){
                return true;
            }
        }
        return false;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject output = new JSONObject();
        output.put("id", getId());
        output.put("user", getUsername());
        output.put("pass", getPassword());
        output.put("role", Role.toInt(getRole()));

        if(profile == null){
            profile = new UserPersonalProfile();
        }

        output.put("profile", profile.toJSON());

        return output;
    }

    public UserPersonalProfile getProfile(){
        return this.profile;
    }

    public WeakReference<UserPersonalProfile> getProfileReference(){
        return new WeakReference<UserPersonalProfile>(getProfile());
    }
}
