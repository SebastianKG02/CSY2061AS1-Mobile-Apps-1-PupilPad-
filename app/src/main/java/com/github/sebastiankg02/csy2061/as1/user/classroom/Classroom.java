package com.github.sebastiankg02.csy2061.as1.user.classroom;

import com.github.sebastiankg02.csy2061.as1.user.User;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.UUID;

public class Classroom {
    private UUID classID;
    private String classTag;
    private String classDescription;

    private ArrayList<WeakReference<User>> pupilsInClassroom;

    public Classroom(UUID classID, String classTag, String classDescription){
        this.classID = classID;
        this.classTag = classTag;
        this.classDescription = classDescription;
    }

    public boolean addUser(String username){
        return pupilsInClassroom.add(new WeakReference<User>(UserAccountControl.getUser(username)));
    }

    public boolean delUser(String username){
        for(WeakReference<User> userWeakReference: pupilsInClassroom){
            if(userWeakReference.get().getUsername().equals(username)){
                userWeakReference.clear();
                pupilsInClassroom.remove(userWeakReference);
                return true;
            }
        }
        return false;
    }
}