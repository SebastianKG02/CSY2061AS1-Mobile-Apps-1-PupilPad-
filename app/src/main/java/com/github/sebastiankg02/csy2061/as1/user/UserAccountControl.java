package com.github.sebastiankg02.csy2061.as1.user;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.github.sebastiankg02.csy2061.as1.user.role.Role;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

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
    private static User currentLoggedInUser;
    private static boolean _initComplete = false;
    private static Activity mOwner;
    private static String path;

    public static void init(String userJSONPath, Activity owner) throws JSONException, IOException{
        if(!_initComplete){
            path = userJSONPath;
            mOwner = owner;
            File f = new File(mOwner.getFilesDir(), userJSONPath);
            users = new ArrayList<User>();
            Log.d("UAC", "Initialising UAC");

            if(f.exists()){
                try {
                    FileInputStream fis = mOwner.openFileInput(f.getName());
                    InputStreamReader inputStreamReader =
                            new InputStreamReader(fis, StandardCharsets.UTF_8);
                    StringBuilder stringBuilder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                        String line = reader.readLine();
                        while (line != null) {
                            stringBuilder.append(line).append('\n');
                            line = reader.readLine();
                        }
                    } catch (IOException e) {
                        // Error occurred when opening raw file for reading.
                    } finally {
                        String contents = stringBuilder.toString();
                        userJSON = new JSONObject(contents);
                    }

                    try {
                        Log.d("UAC", "JSON found, loaded... \n" + userJSON.toString());
                    } catch (Exception e){
                        Log.w("UAC", "Error while loading existing user JSON, wiping and re-populating. \n" + e.getMessage());
                        f.delete();
                        f.createNewFile();
                        saveJSON(false);
                        Log.d("UAC", "JSON created. Contents: \n" + userJSON.toString());
                    }
                } catch (RuntimeException e){
                    Log.w("UAC", "Error while loading existing user JSON, wiping and re-populating. \n" + e.getMessage());
                    f.delete();
                    f.createNewFile();
                    populateUserJSON();
                    saveJSON(false);
                    Log.d("UAC", "JSON created. Contents: \n" + getUserJSON().toString());
                }
                Log.d("UAC", "JSON found, loaded... \n" + userJSON.toString());
                _initComplete = true;
            } else {
                Log.d("UAC", "JSON not found, creating new file...");
                if(f.createNewFile()){
                    populateUserJSON();
                    saveJSON(false);
                    Log.d("UAC", "JSON created. Contents: \n" + userJSON.toString());
                    _initComplete = true;
                } else {
                    throw new FileNotFoundException();
                }
            }
        }
    }

    private static void populateUserJSON(){
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
        saveJSON(false);
    }

    private static JSONObject getUserJSON() throws JSONException {
        JSONObject output = new JSONObject();
        for (User u: users) {
            output.put(u.getId().toString(), u.toJSON());
        }

        return output;
    }

    public static boolean saveJSON(boolean updateUserJSON){
        if(updateUserJSON){
            try {
                userJSON = getUserJSON();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            File f = new File(mOwner.getFilesDir(), path);
            f.delete();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(getUserJSON().toString().getBytes(StandardCharsets.UTF_8));
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validateUserExistence(String username){
        if(getUser(username) == null){
            return false;
        } else {
            return true;
        }
    }

    public boolean login(String username, String password){
        if(validateUserExistence(username)){
            User u = getUser(username);
            if(u.getPassword().equals(password)){
                currentLoggedInUser = new User(u);
                currentLoggedInUser.getProfile().setLastLogin(LocalDateTime.now());
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
            if(u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }
}
