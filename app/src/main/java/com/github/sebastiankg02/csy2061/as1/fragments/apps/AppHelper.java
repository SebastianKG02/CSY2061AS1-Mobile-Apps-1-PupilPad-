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

public class AppHelper {
    public static FragmentManager fragmentManager;
    public static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static AlertDialog.Builder createAlertDialogBuilder(Context c, int title, int message) {
        return new AlertDialog.Builder(c).setTitle(title).setMessage(message);
    }

    public static void createAlertDialog(Fragment a, int title, int message, int okID) {
        AlertDialog d = createAlertDialogBuilder(a.getContext(), title, message).setPositiveButton(okID, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (okID == R.string.contToMain) {
                    moveToFragment(MainMenuFragment.class, null);
                }
            }
        }).create();
        d.show();
    }

    public static void setFragmentManager(FragmentManager manager) {
        fragmentManager = manager;
    }

    public static void moveToFragment(Class<? extends Fragment> destination, Bundle b) {
        FragmentTransaction move = fragmentManager.beginTransaction();
        move.replace(R.id.mainContainer, destination, b).addToBackStack(destination.getName());
        move.commit();
    }

    public static void back(Fragment f) {
        fragmentManager.popBackStack(f.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static FileSystemReturn loadFile(String folder, String path,
                                            Activity owner,
                                            boolean createIfNotFound,
                                            boolean updateFileIfCreated,
                                            boolean updateFileIfEmpty,
                                            @Nullable String creationPayload,
                                            @Nullable FileSystemReturn previousReturn) {

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
                        if (creationPayload == null) {
                            return saveFile("", path, "", owner, FileSystemReturn.FILE_PRESENT_EMPTY_UPDATED, true);
                        } else {
                            return saveFile("", path, creationPayload, owner, FileSystemReturn.FILE_PRESENT_EMPTY_UPDATED, true);
                        }
                    } else {
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
                            if (creationPayload == null) {
                                return saveFile("", path, "", owner, FileSystemReturn.FILE_NOT_PRESENT_CREATED_UPDATED, true);
                            } else {
                                return saveFile("", path, creationPayload, owner, FileSystemReturn.FILE_NOT_PRESENT_CREATED_UPDATED, true);
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
            Log.e("AH", "FileSystem error: " + e.getMessage());
            throw new RuntimeException(e);
            //return FileSystemReturn.ERROR.setPayload("Error loading [" + path + "]" + e.getMessage());
        }
        return FileSystemReturn.ERROR.setPayload("Unexpected behaviour");
    }

    public static FileSystemReturn saveFile(String path, String file, String payload, Activity owner, @Nullable FileSystemReturn returnOverride, boolean overwrite) {
        try {
            File folder = new File(owner.getFilesDir() + path);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File f = new File(owner.getFilesDir() + path, file);
            if (!f.exists()) {
                f.createNewFile();
            } else {
                if (!overwrite) {
                    int filesFound = 0;
                    for (File allFiles : folder.listFiles()) {
                        if (allFiles.getName().startsWith(f.getName())) {
                            filesFound++;
                        }
                    }
                    path = path.replace(f.getName(), f.getName() + "(" + String.valueOf(filesFound) + ")");
                }
            }

            FileOutputStream outputStream = new FileOutputStream(f);
            outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
            if (returnOverride != null) {
                return returnOverride;
            } else {
                return FileSystemReturn.FILE_SAVED;
            }
        } catch (Exception e) {
            Log.e("AH", "FileSystem error: " + e.getMessage());
            Log.e("AH", "Debug Info Payload: " + owner.getFilesDir() + path + file + " {" + payload + "}");
            throw new RuntimeException(e);
            //return FileSystemReturn.ERROR.setPayload(e.getMessage());
        }
    }

    public enum FileSystemReturn {
        FILE_NOT_PRESENT(""),
        FILE_NOT_PRESENT_CREATED(""),
        FILE_NOT_PRESENT_CREATED_UPDATED(""),
        FILE_PRESENT(""),
        FILE_PRESENT_EMPTY(""),
        FILE_PRESENT_EMPTY_UPDATED(""),
        FILE_SAVED(""),
        ERROR("");

        public String payload = "";

        FileSystemReturn(String data) {
            this.payload = data;
        }

        public FileSystemReturn setPayload(String p) {
            this.payload = p;
            return this;
        }
    }

}
