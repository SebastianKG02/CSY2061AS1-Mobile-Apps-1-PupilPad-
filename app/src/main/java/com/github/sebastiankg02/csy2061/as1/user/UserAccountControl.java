package com.github.sebastiankg02.csy2061.as1.user;

import android.app.Activity;
import android.util.Log;

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
 * @todo Integrate with School SQL server so that this application can be centrally controlled and updated.
 *
 * @author Sebastian Kamil Gluch (21414551)
 */
public class UserAccountControl {

    private static ArrayList<User> users;
    private static JSONObject userJSON = new JSONObject();
    public static User currentLoggedInUser;
    private static boolean _initComplete = false;
    public static Activity mOwner;
    private static String path;

    public static void init(String userJSONPath, Activity owner) throws JSONException, IOException{
        if(!_initComplete){
            path = userJSONPath;
            mOwner = owner;
            users = new ArrayList<User>();
            populateUserJSON(false);
            Log.d("UAC", "Initialising UAC");
            try {
                userJSON = new JSONObject(AppHelper.loadFile("",mOwner.getFilesDir() + "/" +userJSONPath, owner, true, true, true, getUserJSON().toString(), null).payload);
            }catch (Exception e){
                Log.i("UAC", "User JSON generated: " + getUserJSON().toString());
                AppHelper.saveFile("",userJSONPath,getUserJSON().toString(), owner, null);
                userJSON = new JSONObject(AppHelper.loadFile("",mOwner.getFilesDir() + "/" +userJSONPath, owner, true, true, true, getUserJSON().toString(), null).payload);
            }
        }
        users = getUsersFromJSON();
    }

    private static void populateUserJSON(boolean saveJSON){
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

        if(saveJSON) {
            saveJSON(false);
        }
    }

    private static JSONObject getUserJSON() throws JSONException {
        JSONObject output = new JSONObject();
        for (User u: users) {
            output.put(u.getId().toString(), u.toJSON());
        }

        return output;
    }

    private static ArrayList<User> getUsersFromJSON() throws JSONException {
        ArrayList<User> output = new ArrayList<User>();
        Iterator<String> jsonKeys = userJSON.keys();

        while(jsonKeys.hasNext()){
            String objectKey = jsonKeys.next();
            Log.d("UAC", "Loading user [" + objectKey + "]");
            output.add(new User(userJSON.getJSONObject(objectKey)));
            Log.d("UAC", "Complete! Loaded: \n" + output.get(output.size()-1).toJSON().toString());
        }

        return output;
    }

    public static boolean saveJSON(boolean updateUserJSON){
        if(currentLoggedInUser != null) {
            users.add(currentLoggedInUser);
        }
        if(updateUserJSON){
            try {
                userJSON = getUserJSON();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        Log.d("UAC", "Saving json: " + userJSON.toString());
        if(AppHelper.saveFile("",path, userJSON.toString(), mOwner, null) == AppHelper.FileSystemReturn.FILE_SAVED){
            users.remove(currentLoggedInUser);
            return true;
        } else {
            users.remove(currentLoggedInUser);
            return false;
        }
    }

    public static boolean validateUserExistence(String username){
        if(getUser(username) == null){
            return false;
        } else {
            return true;
        }
    }

    public static boolean login(String username, String password){
        Log.i("UAC", "LoginAttempt [u: " + username + " & p: " +password + "]");
        if(validateUserExistence(username)){
            Log.i("UAC", "User exists!");
            User u = getUser(username);
            if(u.getPassword().equals(password)){
                Log.i("UAC", "Password correct!");
                currentLoggedInUser = new User(u);
                users.remove(u);

                if(currentLoggedInUser.getProfile() != null) {
                    currentLoggedInUser.getProfile().setLastLogin(LocalDateTime.now());
                }

                saveJSON(true);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static User getUserByUUID(UUID id){
        for(User u: users){
            if(u.getId().equals(id)){
                return u;
            }
        }
        return null;
    }

    public static User getUser(String username){
        for(User u: users){
            Log.d("UAC", "Checking [" + username + "] against User[" + u.getUsername() + "]");
            if(u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }

}
