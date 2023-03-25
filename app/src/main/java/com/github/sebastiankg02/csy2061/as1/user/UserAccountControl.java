package com.github.sebastiankg02.csy2061.as1.user;

import android.app.Activity;

import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;
import com.github.sebastiankg02.csy2061.as1.user.role.Role;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Broad manager for all users within the application.
 * Handles login, password changing e.t.c.
 *
 * @author Sebastian Kamil Gluch (21414551)
 * @todo Integrate with School SQL server so that this application can be centrally controlled and updated.
 */
public class UserAccountControl {
    //Activity-wide reference to the current logged-in user (updated on load from Login fragment)
    public static User currentLoggedInUser;
    //Activity-wide reference to parent activity
    public static Activity mOwner;
    //Reference to current set of User data
    private static ArrayList<User> users;
    //Reference to raw JSON form of User data
    private static JSONObject userJSON = new JSONObject();
    //Flag for program flow control - ensures user data is loaded once
    private static boolean _initComplete = false;
    //Path to load user JSON from
    private static String path;

    //Initalise UAC system (load JSON from userJSONPath, assign owner)
    public static void init(String userJSONPath, Activity owner) throws JSONException, IOException {
        //Only execute if UAC not initalised before in this session
        if (!_initComplete) {
            //Flip flag
            _initComplete = true;
            //Assign & initalise variables
            path = userJSONPath;
            mOwner = owner;
            users = new ArrayList<User>();
            //Ensure user array is not empty before loading data from file
            populateUserJSON(false);
            try {
                //Try to load user JSON from file
                userJSON = new JSONObject(AppHelper.loadFile(mOwner.getFilesDir() + "/" + userJSONPath, owner, true, true, true, getUserJSON().toString()).payload);
            } catch (Exception e) {
                //If user JSON not loaded, save default generated data to file
                AppHelper.saveFile("", userJSONPath, getUserJSON().toString(), owner, null);
                //Generate JSON from default data
                userJSON = new JSONObject(AppHelper.loadFile(mOwner.getFilesDir() + "/" + userJSONPath, owner, true, true, true, getUserJSON().toString()).payload);
            }
        }
        //Load user data from loaded JSON
        users = getUsersFromJSON();
    }

    //Generate default user data
    private static void populateUserJSON(boolean saveJSON) {
        User defaultAdministrator = new User(
                "admin",
                "AdminPassword1",
                Role.ADMINISTRATOR
        );

        User teacher0 = new User(
                "teacher0",
                "Apples01",
                Role.MODERATOR
        );

        User teacher1 = new User(
                "teacher1",
                "Bananas75",
                Role.MODERATOR
        );

        User teacher2 = new User(
                "teacher2",
                "2CrispyMan",
                Role.MODERATOR
        );

        User pupil0 = new User(
                "donald-m",
                "trainsRC00l",
                Role.USER
        );

        User pupil1 = new User(
                "brown-c",
                "liverpoolFan12",
                Role.USER
        );

        User pupil2 = new User(
                "murray-b",
                "Helena2010",
                Role.USER
        );

        User pupil3 = new User(
                "butcher-j",
                "Welcome2020",
                Role.USER
        );

        User pupil4 = new User(
                "travolta-j",
                "Big12Biceps",
                Role.USER
        );

        User pupilBanned = new User(
                "west-l",
                "Johnson9821Six",
                Role.NONE
        );

        users.clear();
        users.add(defaultAdministrator);
        users.add(teacher0);
        users.add(teacher1);
        users.add(teacher2);
        users.add(pupil0);
        users.add(pupil1);
        users.add(pupil2);
        users.add(pupil3);
        users.add(pupil4);
        users.add(pupilBanned);

        //Save JSON IF required
        if (saveJSON) {
            saveJSON(false);
        }
    }

    //Generate JSON from user data (dump data from User.toJSON)
    private static JSONObject getUserJSON() throws JSONException {
        JSONObject output = new JSONObject();
        for (User u : users) {
            output.put(u.getId().toString(), u.toJSON());
        }

        return output;
    }

    //Generate user dataset from loaded JSON
    private static ArrayList<User> getUsersFromJSON() throws JSONException {
        //Initialise output
        ArrayList<User> output = new ArrayList<User>();
        Iterator<String> jsonKeys = userJSON.keys();

        //Loop through keys found in file (User IDs)
        while (jsonKeys.hasNext()) {
            String objectKey = jsonKeys.next();
            //Generate user data based on JSON data (using User JSON constructor)
            output.add(new User(userJSON.getJSONObject(objectKey)));
        }

        return output;
    }

    //Dump user data as JSON to file (optionally update user data JSON from Array)
    public static boolean saveJSON(boolean updateUserJSON) {
        //As currentUser is taken out during UAC operations, add back in
        if (currentLoggedInUser != null) {
            users.add(currentLoggedInUser);
        }

        //Check if user data JSON needs to be updated from Array
        if (updateUserJSON) {
            try {
                //Update user data JSON
                userJSON = getUserJSON();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        //Attempt to save file, remove current user again to maintain proper functionality, return result
        if (AppHelper.saveFile("", path, userJSON.toString(), mOwner, null) == AppHelper.FileSystemReturn.FILE_SAVED) {
            users.remove(currentLoggedInUser);
            return true;
        } else {
            users.remove(currentLoggedInUser);
            return false;
        }
    }

    //Check if user (username) exists within User Data
    public static boolean validateUserExistence(String username) {
        if (getUser(username) == null) {
            return false;
        } else {
            return true;
        }
    }

    //Attempt to log user (username) into UAC, (password) must match passsword on file
    public static boolean login(String username, String password) {
        //Check if user exists
        if (validateUserExistence(username)) {
            //Grab reference to user
            User u = getUser(username);
            //Check if password is correct
            if (u.getPassword().equals(password)) {
                //Set current user to a new copy instance of the loaded user
                currentLoggedInUser = new User(u);
                //Remove current User from the data set
                users.remove(u);

                //Update last login time for current User
                if (currentLoggedInUser.getProfile() != null) {
                    currentLoggedInUser.getProfile().setLastLogin(LocalDateTime.now());
                }

                //Save updated JSON
                saveJSON(true);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //Fetch user from data set by UUID
    public static User getUserByUUID(UUID id) {
        //Loop through data set and check each id against target ID
        for (User u : users) {
            if (u.getId().equals(id)) {
                //If both IDs matched, return loaded User
                return u;
            }
        }
        return null;
    }

    //Fetch user from data set by username String
    public static User getUser(String username) {
        //Loop through data set and check each ID against target ID
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                //If both IDs matched, return loaded User
                return u;
            }
        }
        return null;
    }

}
