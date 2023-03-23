package com.github.sebastiankg02.csy2061.as1.user.note;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;
import com.github.sebastiankg02.csy2061.as1.user.User;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class PupilFile {
    public enum PupilFileType
    {
        NONE(-1),
        FILE(0),
        FOLDER(1);

        public int type = 0;

        PupilFileType(int t){
            this.type = t;
        }

        public static PupilFileType fromInt(int t){
            if(t == 0){
                return FILE;
            } else if (t == 1){
                return FOLDER;
            } else {
                return NONE;
            }
        }
    }

    public class PupilNote{
        //Meta data for organisation
        private LocalDateTime lastEdit = LocalDateTime.now();
        //Actual file name (abc.json), used internally for file system
        private String fileName = "";
        //File display name (ABC Test Notes), used as front-end display name
        private String displayName = "";
        //Internal title (Header for Note)
        private String title = "";
        //Actual note content (raw)
        private String content = "";

        public PupilNote(JSONObject jsonData) throws JSONException {
            this.lastEdit = LocalDateTime.parse(jsonData.getString("last-edit"), AppHelper.formatterDateTime);
            this.fileName = jsonData.getString("file");
            this.displayName = jsonData.getString("name");
            this.title = jsonData.getString("title");
            this.content = jsonData.getString("content");
        }

        public PupilNote(User u, PupilNote o){
            if(o != null) {
                this.lastEdit = o.lastEdit;
                this.fileName = o.fileName;
                this.displayName = o.displayName;
                this.title = o.title;
                this.content = o.content;
            } else {
                initBlank(u);
            }
        }

        public PupilNote(User u){
            initBlank(u);
        }

        public void initBlank(User u){
            this.lastEdit = LocalDateTime.now();
            this.fileName = UserAccountControl.currentLoggedInUser.getUsername() + "@" + AppHelper.formatterDateTime.format(this.lastEdit) + ".file.json";
            this.displayName = UserAccountControl.currentLoggedInUser.getUsername() + " (" + AppHelper.formatterDateTime.format(this.lastEdit) + ")";
            this.title = "New note";
            this.content = "Enter note here!";
        }

        public JSONObject toJSON() throws JSONException {
            JSONObject output = new JSONObject();
            output.put("last-edit", AppHelper.formatterDateTime.format(this.lastEdit));
            output.put("file", this.fileName);
            output.put("name", this.displayName);
            output.put("title", this.title);
            output.put("content", this.content);
            return output;
        }
    }

    //Owner of this file
    private UUID owner;
    //Path to this file (/notes/...)
    private String path;
    //Meta data for organisation
    private LocalDate dateCreated;
    //File data (if necessary)
    public PupilNote data;
    public PupilFileType type;

    public PupilFile(User owner, String path, PupilFileType type, @Nullable PupilNote data) throws JSONException {
        this.owner = owner.getId();
        this.path = path;
        this.dateCreated = LocalDate.now();
        this.type = type;

        if(this.type.equals(PupilFileType.FILE)){
            data = new PupilNote(new User(owner), data);
        }
    }

    public PupilFile(String folder, String fromFile, Activity owner) throws JSONException {
        String p = AppHelper.loadFile(folder, fromFile, owner, false, false, false, null, null).payload;
        Log.i("NOTES", "Loaded pupil data: " + p);
        JSONObject jsonData = new JSONObject(p);
        this.owner = UUID.fromString(jsonData.getString("owner"));
        this.path = jsonData.getString("path");
        this.dateCreated = LocalDate.parse(jsonData.getString("date-created"), AppHelper.formatterDate);
        this.type = PupilFileType.fromInt(jsonData.getInt("type"));

        if(this.type.equals(PupilFileType.FILE)){
            data = new PupilNote(jsonData.getJSONObject("data"));
        } else {
            data = null;
        }
    }

    public static PupilFile createFolderAt(String folder, String folderName, User owner, Activity ownerActivity) throws JSONException {
        PupilFile output;
        output = new PupilFile(owner, "/" + folder + "/" + folderName + ".folder.json", PupilFileType.FOLDER, null);
        output.path = "/" + folder + "/" + folderName + ".folder.json";
        output.save(ownerActivity);
        return output;
    }

    public static PupilFile createFileAt(String folder, String fileName, User owner, Activity ownerActivity) throws JSONException {
        PupilFile output;
        output = new PupilFile(owner, "/" + folder + "/" + fileName + ".file.json", PupilFileType.FILE, null);
        output.path = "/" + folder + "/";
        output.initPupilNote(owner);
        output.data.fileName = fileName + ".file.json";
        output.save(ownerActivity);
        return output;
    }

    public JSONObject getJSON() throws JSONException {
        JSONObject output = new JSONObject();
        output.put("owner", this.owner.toString());
        output.put("path", this.path);
        output.put("date-created", AppHelper.formatterDate.format(this.dateCreated));
        output.put("type", this.type.type);

        if(this.data != null) {
            output.put("data", this.data.toJSON());
        } else {
            this.data = new PupilNote(UserAccountControl.getUserByUUID(owner));
            output.put("data", this.data.toJSON());
        }

        return output;
    }

    public AppHelper.FileSystemReturn save(Activity owner) throws JSONException {
        Log.e("AH", this.path + this.data.fileName);
        return AppHelper.saveFile(this.path, this.data.fileName, getJSON().toString(), owner, null);
    }

    public void setContents(String contents){
        this.data.content = contents;
    }

    public void setTitle(String title){
        this.data.title = title;
    }

    public void setFileName(String fileName){
        this.data.fileName = fileName;
    }

    public void setDisplayName(String displayName){
        this.data.displayName = displayName;
    }

    public void updateEditTime(){
        this.data.lastEdit = LocalDateTime.now();
    }

    public String getContents(){
        return this.data.content;
    }

    public String getTitle(){
        return this.data.title;
    }

    private void initPupilNote(User u){
        this.data = new PupilNote(u);
    }
    public String getDisplayName(){
        return this.data.displayName;
    }

    public String getFileName(){
        return this.data.fileName;
    }

    public UUID getOwner(){
        return this.owner;
    }

    public String getPath() {
        return this.path;
    }

    public LocalDate getDateCreated(){
        return this.dateCreated;
    }

    public String getDateCreatedAsString(){
        return AppHelper.formatterDate.format(this.dateCreated);
    }

    public PupilFileType getType(){
        return this.type;
    }
}