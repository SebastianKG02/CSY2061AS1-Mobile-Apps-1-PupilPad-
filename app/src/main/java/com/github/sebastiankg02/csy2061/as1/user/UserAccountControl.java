package com.github.sebastiankg02.csy2061.as1.user;

import com.github.sebastiankg02.csy2061.as1.user.role.Role;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private static JSONObject userJSON;
    private static User currentLoggedInUser;
    private static boolean _initComplete = false;

    public static void init(String userJSONPath) throws JSONException, IOException{
        if(!_initComplete){
            File f = new File(userJSONPath);
            users = new ArrayList<User>();

            if(f.exists()){
                userJSON = new JSONObject(userJSONPath);
                _initComplete = true;
            } else {
                if(f.createNewFile()){
                    populateUserJSON();
                    userJSON = getUserJSON();
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
    }

    private static JSONObject getUserJSON() throws JSONException {
        JSONObject output = new JSONObject();
        for (User u: users) {
            output.put(u.getId().toString(), u.toJSON());
        }

        return output;
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
