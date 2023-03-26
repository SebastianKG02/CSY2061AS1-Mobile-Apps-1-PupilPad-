package com.github.sebastiankg02.csy2061.as1.user.note;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;
import com.github.sebastiankg02.csy2061.as1.user.User;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Manages User notes and Folders via JSON and raw file system
 */
public class PupilFile {
    //File data (if necessary)
    public PupilNote data;
    public PupilFileType type;
    //Owner of this file
    private UUID owner;
    //Path to this file (/notes/...)
    private String path;
    //Meta data for organisation
    private LocalDate dateCreated;

    //Full constructor (PupilNote data is optional, will generate default data if null)
    public PupilFile(User owner, String path, PupilFileType type, @Nullable PupilNote data) throws JSONException {
        this.owner = owner.getId();
        this.path = path;
        this.dateCreated = LocalDate.now();
        this.type = type;
        //Create new default pupil note, update with data if present
        data = new PupilNote(new User(owner), data);
    }

    //Copy constructor
    public PupilFile(User u, PupilFile other) {
        this.owner = other.owner;
        this.path = other.path;
        this.dateCreated = other.dateCreated;
        this.type = other.type;
        this.data = new PupilNote(u, other.data);
    }

    //File-based constructor, should be used when loading notes from disk
    public PupilFile(String fromFile, Activity owner) throws JSONException {
        //Load file from disk
        String p = AppHelper.loadFile(fromFile, owner, false, false, false, null).payload;
        //Construct JSONObject from payload
        JSONObject jsonData = new JSONObject(p);
        //Initialise variables
        this.owner = UUID.fromString(jsonData.getString("owner"));
        this.path = jsonData.getString("path");
        this.dateCreated = LocalDate.parse(jsonData.getString("date-created"), AppHelper.formatterDate);
        this.type = PupilFileType.fromInt(jsonData.getInt("type"));
        //Attempt to load PupilNote data from JSON
        data = new PupilNote(jsonData.getJSONObject("data"));
    }

    /**
     * Creates a ".folder.json" file at the given directory, which will be owned by the user specified.
     * @param folder destination folder
     * @param folderName name of folder
     * @param owner user that owns this folder - other users will not be able to see this folder
     * @param ownerActivity - parent activity
     * @return the created file
     * @throws JSONException any JSON error
     */
    public static PupilFile createFolderAt(String folder, String folderName, User owner, Activity ownerActivity) throws JSONException {
        //Create default folder-specific PupilFile object
        PupilFile output;
        output = new PupilFile(owner, "/" + folder + "/" + folderName + ".folder.json", PupilFileType.FOLDER, null);
        //Update data
        output.path = "/" + folder + "/";
        output.initPupilNote(owner);
        output.data.fileName = folderName + ".folder.json";
        //Ensure folder file is saved
        output.save(ownerActivity);
        return output;
    }

    /**
     * Creates a ".file.json" file at the given directory, which will be owned by the user specified.
     * @param folder destination folder
     * @param fileName name of folder
     * @param owner user that owns this file - other users will not be able to see this file
     * @param ownerActivity - parent activity
     * @return the created file
     * @throws JSONException any JSON error
     */
    public static PupilFile createFileAt(String folder, String fileName, User owner, Activity ownerActivity) throws JSONException {
        //Create default file-specific PupilFile object
        PupilFile output;
        output = new PupilFile(owner, "/" + folder + "/" + fileName + ".file.json", PupilFileType.FILE, null);
        //Update file data
        output.path = "/" + folder + "/";
        output.initPupilNote(owner);
        output.data.fileName = fileName + ".file.json";
        //Ensure file is saved before continuing
        output.save(ownerActivity);
        return output;
    }

    //Parse data to JSON
    public JSONObject getJSON() throws JSONException {
        //Create initial JSONObject
        JSONObject output = new JSONObject();
        output.put("owner", this.owner.toString());
        output.put("path", this.path);
        output.put("date-created", AppHelper.formatterDate.format(this.dateCreated));
        output.put("type", this.type.type);

        //Add PupilNote data
        if (this.data != null) {
            output.put("data", this.data.toJSON());
        } else {
            this.data = new PupilNote(UserAccountControl.getUserByUUID(owner));
            output.put("data", this.data.toJSON());
        }

        return output;
    }

    //Save current PupilFile into its' path, under its' name, and populate file with freshly serialized JSON data
    public AppHelper.FileSystemReturn save(Activity owner) throws JSONException {
        return AppHelper.saveFile(this.path, this.data.fileName, getJSON().toString(), owner, null);
    }

    //BEGIN Getters & Setters ------------------------------------------------------------------ BEGIN
    public String getContents() {
        return this.data.content;
    }

    public void setContents(String contents) {
        this.data.content = contents;
    }

    public String getTitle() {
        return this.data.title;
    }

    public void setTitle(String title) {
        this.data.title = title;
    }

    private void initPupilNote(User u) {
        this.data = new PupilNote(u);
    }

    public String getDisplayName() {
        return this.data.displayName;
    }

    public void setDisplayName(String displayName) {
        this.data.displayName = displayName;
    }

    public String getFileName() {
        return this.data.fileName;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public String getPath() {
        return this.path;
    }
    //END Getters & Setters ---------------------------------------------------------------------- END

    /**
     * Simple enum that stores Android String resource ids for different file types (either FILE or FOLDER)
     * & assists program logic by distinguishing between types of PupilFile
     */
    public enum PupilFileType {
        NONE(-1, -1, -1, -1, -1, -1, -1, -1),
        FILE(0,
                R.string.delete_file_title,
                R.string.delete_file_desc,
                R.string.delete_confirm,
                R.string.delete_cancel,
                R.string.delete_file_cancel_complete,
                R.string.delete_file_complete,
                R.string.delete_file_error),
        FOLDER(1,
                R.string.delete_folder_title,
                R.string.delete_folder_desc,
                R.string.delete_confirm,
                R.string.delete_cancel,
                R.string.delete_folder_cancel_complete,
                R.string.delete_folder_complete,
                R.string.delete_folder_error);

        //BEGIN Android String Resource variables ---------------- BEGIN
        public int type = 0;
        public int deleteDialogTitle;
        public int deleteDialogDesc;
        public int deleteTag;
        public int cancelTag;
        public int cancelSuccessTag;
        public int deleteSuccess;
        public int deleteFail;
        //END Android String Resource variables ------------------ END

        //Internal constructor
        PupilFileType(int t, int ddt, int ddd, int dt, int ct, int cst, int ds, int df) {
            this.type = t;
            this.deleteDialogTitle = ddt;
            this.deleteDialogDesc = ddd;
            this.deleteTag = dt;
            this.cancelTag = ct;
            this.cancelSuccessTag = cst;
            this.deleteSuccess = ds;
            this.deleteFail = df;
        }

        //Convert from integer to PupilFileType (used during JSON serialization)
        public static PupilFileType fromInt(int t) {
            if (t == 0) {
                return FILE;
            } else if (t == 1) {
                return FOLDER;
            } else {
                return NONE;
            }
        }
    }

    /**
     * Contains primarily PupilFile:File specific data as an extension to PupilFile
     */
    public class PupilNote {
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

        //File-based JSON constructor
        public PupilNote(JSONObject jsonData) throws JSONException {
            this.lastEdit = LocalDateTime.parse(jsonData.getString("last-edit"), AppHelper.formatterDateTime);
            this.fileName = jsonData.getString("file");
            this.displayName = jsonData.getString("name");
            this.title = jsonData.getString("title");
            this.content = jsonData.getString("content");
        }

        //Copy constructor, will generate default data if Note is null, based on User u
        public PupilNote(User u, PupilNote o) {
            if (o != null) {
                this.lastEdit = o.lastEdit;
                this.fileName = o.fileName;
                this.displayName = o.displayName;
                this.title = o.title;
                this.content = o.content;
            } else {
                initBlank(u);
            }
        }

        //Simple constructor
        public PupilNote(User u) {
            initBlank(u);
        }

        //Generate default values
        public void initBlank(User u) {
            this.lastEdit = LocalDateTime.now();
            this.fileName = u.getUsername() + "@" + AppHelper.formatterDateTime.format(this.lastEdit) + ".file.json";
            this.displayName = u.getUsername() + " (" + AppHelper.formatterDateTime.format(this.lastEdit) + ")";
            this.title = "New note";
            this.content = "Enter note here!";
        }

        //Parse data to JSON
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
}
