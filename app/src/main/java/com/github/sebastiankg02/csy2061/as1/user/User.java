package com.github.sebastiankg02.csy2061.as1.user;

import com.github.sebastiankg02.csy2061.as1.user.role.Role;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/*
 * Simple object used to contain common identifiers on Users, as well as perform basic maintainance methods
 */
public class User {
	//Define acceptable characters for password as well as password constraints
    public static final char[] passwordUppercaseChars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static final char[] passwordLowercaseChars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final char[] passwordNumbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static final int passwordMinimumCharacters = 8;
    //Define class variables
	//User Unique ID (permanent identifier, where username can be changed, the UUID cannot)
	private UUID id;
    //Login username
	private String username;
    //Login password
	private String password;
	//User role (permission level): controls what user can (and cannot) do & access
    private Role role;
	//Profile information (name, last login e.t.c.)
    private UserPersonalProfile profile;

	//Default constructor
    public User() {
        this(UUID.randomUUID(), "", "", Role.NONE, new UserPersonalProfile());
    }

	//Random UUID constructor (otherwise full control)
    public User(String username, String password, Role r, UserPersonalProfile upp) {
        this(UUID.randomUUID(), username, password, r, upp);
    }

	//Full constructor
    public User(UUID uuid, String username, String password, Role r, UserPersonalProfile upp) {
        this.id = uuid;
        this.username = username;
        this.password = password;
        this.role = r;
        this.profile = upp;
    }
	
	//Lean constructor - random uuid and blank profile
    public User(String username, String password, Role r) {
        this(UUID.randomUUID(), username, password, r, new UserPersonalProfile());
    }

	//Copy constructor
    public User(User other) {
        this(other.id, other.username, other.password, other.role, other.getProfile());
    }
	
	//JSON Constructor
    public User(JSONObject rawUser) throws JSONException {
        id = UUID.fromString(rawUser.getString("id"));
        this.username = rawUser.getString("user");
        this.password = rawUser.getString("pass");
        this.role = Role.fromInt(rawUser.getInt("role"));
        this.profile = new UserPersonalProfile(rawUser.getJSONObject("profile"));
    }

	public JSONObject toJSON() throws JSONException {
        JSONObject output = new JSONObject();
        output.put("id", getId());
        output.put("user", getUsername());
        output.put("pass", getPassword());
        output.put("role", Role.toInt(getRole()));

        if (profile == null) {
            profile = new UserPersonalProfile();
        }

        output.put("profile", profile.toJSON());

        return output;
    }

	//BEGIN Validation Methods ------------------------------------------------ BEGIN
	//Validate password against defined rules, return result
	//Result is PasswordValidationObject (where return is rules failed/passed)
	public PasswordValidationObject validatePassword(String passwordToCheck) {
        return new PasswordValidationObject(
                checkForCharacter(passwordToCheck, passwordUppercaseChars),
                checkForCharacter(passwordToCheck, passwordLowercaseChars),
                checkForCharacter(passwordToCheck, passwordNumbers),
                (passwordToCheck.length() >= passwordMinimumCharacters)
        );
    }
	
	//Check if passwordToCheck is the same as existing password
    public boolean checkPasswordSimilarity(String passwordToCheck) {
        return this.password.equals(passwordToCheck);
    }

	//Internal method, checks for presence of ANY characters within queryCharacters
    private boolean checkForCharacter(String toCheck, char[] queryCharacters) {
		//Loop through each character in queryCharacters
        for (char c : queryCharacters) {
			//If toCheck string contains current character, return true as character found
            if (toCheck.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }
	//END Validation Methods --------------------------------------------------- END

	//BEGIN General Getters & Setters ------------------------------ BEGIN
    public UUID getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Role getRole() {
        return role;
    }

	//Set new password (only if new password is valid)
    public boolean setNewPassword(String newPassword) {
        if (validatePassword(newPassword).isPasswordValid()) {
            this.password = newPassword;
            return true;
        } else {
            return false;
        }
    }

    public UserPersonalProfile getProfile() {
        return this.profile;
    }
	//END General Getters & Setters -------------------------------- END
}
