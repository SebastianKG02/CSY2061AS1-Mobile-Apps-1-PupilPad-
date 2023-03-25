package com.github.sebastiankg02.csy2061.as1.fragments.apps;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.fragments.MainMenuFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

/**
 * Helper class for general app-wide actions
 */
public class AppHelper {
    //First FragmentManager loaded into the app - used to control fragment movements
    public static FragmentManager fragmentManager;
    //LocalDate formatter & parser
    public static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    //LocalDateTime formatter & parser
    public static DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Method for creating a quick AlertDialog Builder
     *
     * @param c       The currently active context
     * @param title   Android String Resource to be used for AlertDialog title
     * @param message Android String Resource to be used for AlertDialog message
     * @return The created AlertDialog.Builder
     */
    public static AlertDialog.Builder createAlertDialogBuilder(Context c, int title, int message) {
        return new AlertDialog.Builder(c).setTitle(title).setMessage(message);
    }

    /**
     * Method for creating quick AlertDialogs with only one button
     *
     * @param a       The currently active Fragment (will get context)
     * @param title   Android String Resource to be used for AlertDialog title
     * @param message Android String Resource to be used for AlertDialog message
     * @param okID    Android String Resource to be used for the button
     */
    public static void createAlertDialog(Fragment a, int title, int message, int okID) {
        //Create AlertDialog Builder
        AlertDialog d = createAlertDialogBuilder(a.getContext(), title, message).setPositiveButton(okID, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //Special functionality for LoginFragment - will only move to main menu if the okID String resource matches R.string.ok
                if (okID == R.string.contToMain) {
                    moveToFragment(MainMenuFragment.class, null);
                }
            }
        }).create();
        d.show();
    }

    /**
     * Simple method to move to another fragment from within a fragment.
     *
     * @param destination Fragment Class to be moved to (class name added to BackStack)
     * @param b           Any BundleInstance information
     */
    public static void moveToFragment(Class<? extends Fragment> destination, Bundle b) {
        FragmentTransaction move = fragmentManager.beginTransaction();
        move.replace(R.id.mainContainer, destination, b).addToBackStack(destination.getName());
        move.commit();
    }

    /**
     * Simple method to go back to the previous fragment from within a fragment
     *
     * @param f The Fragment to pop from the BackStack (identified by class name)
     */
    public static void back(Fragment f) {
        fragmentManager.popBackStack(f.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * App-wide file loading method.
     *
     * @param path                Path to file (must use activity.getFilesDir()!)
     * @param owner               Parent activity
     * @param createIfNotFound    Set TRUE if a file is to be created if it is not found
     * @param updateFileIfCreated Set TRUE if a created file is to be updated upon creation
     * @param updateFileIfEmpty   Set TRUE if a file is to be updated if it is found
     * @param creationPayload     The payload used to populate files while using the above flags
     * @return a FileSystemReturn value, with an accompanying String payload
     */
    public static FileSystemReturn loadFile(String path,
                                            Activity owner,
                                            boolean createIfNotFound,
                                            boolean updateFileIfCreated,
                                            boolean updateFileIfEmpty,
                                            @Nullable String creationPayload) {

        try {
            //Create file instance
            File fileToLoad = new File(path);
            Log.i("AH", "Loading file: " + path + "[" + fileToLoad.getName() + "]");
            //Check if file exists
            if (fileToLoad.exists()) {
                //Build input streams
                FileInputStream fileIn = new FileInputStream(fileToLoad);
                //Ensure UTF-8 encoding
                InputStreamReader inputStream = new InputStreamReader(fileIn, StandardCharsets.UTF_8);
                //Prepare for reading
                BufferedReader reader = new BufferedReader(inputStream);
                StringBuilder sb = new StringBuilder();

                //Read contents of file line-by-line
                String line = reader.readLine();
                do {
                    sb.append(line);
                    line = reader.readLine();
                } while (line != null);

                //Final output String
                line = sb.toString();
                //Ensure streams are closed
                fileIn.close();
                inputStream.close();
                reader.close();
                //If output is not empty, return output
                if (!line.isEmpty()) {
                    return FileSystemReturn.FILE_PRESENT.setPayload(line);
                } else {
                    //If output is empty, and updateFileIfEmpty is set, populate file
                    if (updateFileIfEmpty) {
                        //Check for creationPayload
                        if (creationPayload == null) {
                            //If no creationPayload has been set, populate file with empty string
                            return saveFile("", path, "", owner, FileSystemReturn.FILE_PRESENT_EMPTY_UPDATED);
                        } else {
                            //If creationPayload has been set, populate file with desired payload
                            return saveFile("", path, creationPayload, owner, FileSystemReturn.FILE_PRESENT_EMPTY_UPDATED);
                        }
                    } else {
                        //If output is empty and updateFileIfEmpty is not set, return empty string payload
                        return FileSystemReturn.FILE_PRESENT_EMPTY.setPayload("");
                    }
                }
            } else {
                //If file doesn't exist, check creation flags
                if (createIfNotFound) {
                    //Ensure file is really deleted
                    fileToLoad.delete();
                    //Create new file
                    if (fileToLoad.createNewFile()) {
                        //Populate file contents if needed
                        if (updateFileIfCreated) {
                            //Check for creation payload
                            if (creationPayload == null) {
                                //If no creation payload, populate file with empty string
                                return saveFile("", path, "", owner, FileSystemReturn.FILE_NOT_PRESENT_CREATED_UPDATED);
                            } else {
                                //If creation payload set, populate file with desired payload
                                return saveFile("", path, creationPayload, owner, FileSystemReturn.FILE_NOT_PRESENT_CREATED_UPDATED);
                            }
                        } else {
                            //If not, inform user of result
                            return FileSystemReturn.FILE_NOT_PRESENT_CREATED;
                        }
                    }
                } else {
                    //If don't need to create file, notify user
                    return FileSystemReturn.FILE_NOT_PRESENT;
                }
            }
        } catch (Exception e) {
            //General error catching
            return FileSystemReturn.ERROR.setPayload("Error loading [" + path + "]" + e.getMessage());
        }
        //This statement should never be reached, as this would mean that error catching has not worked!
        return FileSystemReturn.ERROR.setPayload("Unexpected behaviour");
    }

    /**
     * App-wide file saving method.
     *
     * @param path           Full path to <i>file</i> containing directory. Any unmade directories along this path will be created.
     * @param file           File name to be saved
     * @param payload        Actual file data to be saved
     * @param owner          Calling activity, used to access internal storage
     * @param returnOverride Override the default FileSystemReturns generated by this method
     * @return
     * @apiNote The FileSystemReturn of this method does NOT contain a payload by default!
     * The only way to provide a payload via this method is to override the default FileSystemReturn!
     */
    public static FileSystemReturn saveFile(String path, String file, String payload, Activity owner, @Nullable FileSystemReturn returnOverride) {
        try {
            //Ensure containing directory exists
            File folder = new File(owner.getFilesDir() + path);
            if (!folder.exists()) {
                //Create directories
                folder.mkdirs();
            }

            //Create file instance
            File f = new File(owner.getFilesDir() + path, file);
            //Check if file exists
            if (!f.exists()) {
                //Create new file
                f.createNewFile();
            }

            //As file now exists, dump payload to file
            FileOutputStream outputStream = new FileOutputStream(f);
            outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
            //Ensure streams are closed
            outputStream.flush();
            outputStream.close();

            //Return save success, override if necessary
            if (returnOverride != null) {
                return returnOverride;
            } else {
                return FileSystemReturn.FILE_SAVED;
            }
        } catch (Exception e) {
            return FileSystemReturn.ERROR.setPayload(e.getMessage());
        }
    }

    /**
     * String payload-bearing enum that represents the state of the AppHelper FileSystem.
     * Each enum represents different states, and each state has a self-descriptive name.
     * This allows for more efficient debugging and case handling of
     */
    public enum FileSystemReturn {
        FILE_NOT_PRESENT(""),
        FILE_NOT_PRESENT_CREATED(""),
        FILE_NOT_PRESENT_CREATED_UPDATED(""),
        FILE_PRESENT(""),
        FILE_PRESENT_EMPTY(""),
        FILE_PRESENT_EMPTY_UPDATED(""),
        FILE_SAVED(""),
        FILE_NOT_SAVED(""),
        ERROR("");

        //This FileSystemReturn's payload, or content loaded
        public String payload = "";

        //Private constructor to ensure all FSR values are non-null
        FileSystemReturn(String data) {
            this.payload = data;
        }

        /**
         * Set this FileSystemReturn's payload.
         *
         * @param p Payload to be set
         * @return This FileSystemReturn with updated payload <i>p</i>.
         */
        public FileSystemReturn setPayload(String p) {
            this.payload = p;
            return this;
        }
    }

}
