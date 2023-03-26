package com.github.sebastiankg02.csy2061.as1.user.note;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.NotesAppFragment;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.NotesEditorFragment;
import com.github.sebastiankg02.csy2061.as1.user.User;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

/**
 * Adapter for the NotesAppFragment RecyclerView - displays PupilFile objects and handles UI and user input to these PupilFile objects
 */
public class PupilFileAdapter extends RecyclerView.Adapter<PupilFileAdapter.ViewHolder> {
    //Reference to folder icon resource (displayed as part of each individual RV item)
    public static final int FOLDER_ICON = android.R.drawable.ic_menu_agenda;
    //Reference to file icon resource (displayed as part of each individual RV item)
    public static final int FILE_ICON = android.R.drawable.ic_menu_edit;
    //Global working directory for the adapter, updated by NotesAppFragment when necessary in moving between folders
    public static String currentDirectory;
    //Adapter data set
    public ArrayList<PupilFile> files;
    private Activity owner;
    private User currentUser;

    //Default constructor - sets variables and updates contents of adapter
    public PupilFileAdapter(String currentDirectory, Activity owner, User currentUser) {
        this.currentDirectory = currentDirectory;
        this.owner = owner;
        this.currentUser = currentUser;
        loadItems();
    }

    public void loadItems() {
        //Load directory
        File f = new File(owner.getFilesDir() + "/" + currentDirectory);
        //Get all files in directory
        File[] allFilesInDir = f.listFiles();

        files = new ArrayList<PupilFile>();
        if (f.exists() && allFilesInDir != null && allFilesInDir.length > 0) {
            //Loop through files, determine which files & folders user has access to
            for (int i = 0; i < allFilesInDir.length; i++) {
                //Verify file is actual file
                if (allFilesInDir[i].isFile()) {
                    //Verify all files are JSON files only
                    if (allFilesInDir[i].getName().endsWith("json")) {
                        try {
                            //Load the file
                            files.add(new PupilFile(owner.getFilesDir() + "/" + currentDirectory + "/" + allFilesInDir[i].getName(), owner));
                            //If user is not owner, remove this file from list of viewable files
                            if (!files.get(files.size() - 1).getOwner().equals(currentUser.getId())) {
                                files.remove(files.size() - 1);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.note_item, parent, false);
        ViewHolder itemHolder = new ViewHolder(item);
        return itemHolder;
    }

    //Set up UI functionality
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get current file from data set
        PupilFile currentFile = files.get(position);

        //Set icon for more user clarity
        if (currentFile.type.equals(PupilFile.PupilFileType.FILE)) {
            holder.itemIcon.setImageResource(FILE_ICON);
        } else if (currentFile.type.equals(PupilFile.PupilFileType.FOLDER)) {
            holder.itemIcon.setImageResource(FOLDER_ICON);
        }

        //Update display names and titles of files
        holder.itemDisplayName.setText(currentFile.getDisplayName());
        if (currentFile.type.equals(PupilFile.PupilFileType.FILE)) {
            holder.itemFileName.setText("\"" + currentFile.getTitle() + "\"");
        } else {
            holder.itemFileName.setText("");
        }
        //Provide delete button functionality (create alert dialog, asking user to confirm, delete if true)
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create confirmation dialog
                AlertDialog deleteDialog = AppHelper.createAlertDialogBuilder(view.getContext(), currentFile.type.deleteDialogTitle, currentFile.type.deleteDialogDesc)
                        .setPositiveButton(currentFile.type.deleteTag, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                //Create reference to actual file to be deleted
                                File toDelete = new File(view.getContext().getFilesDir(), currentFile.getPath() + currentFile.getFileName());
                                //Check if file exists
                                if (toDelete.exists()) {
                                    //Try to delete file
                                    if (toDelete.delete()) {
                                        //If success, inform user
                                        Snackbar.make(view, currentFile.type.deleteSuccess, Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        //If error, inform user
                                        Snackbar.make(view, currentFile.type.deleteFail, Snackbar.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //If it doesn't, inform user of error.
                                    Snackbar.make(view, currentFile.type.deleteFail, Snackbar.LENGTH_SHORT).show();
                                }
                                //Remove file and update adapter contents
                                files.remove(currentFile);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton(currentFile.type.cancelTag, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //if user cancels delete, inform of success
                                dialogInterface.cancel();
                                Snackbar.make(view, currentFile.type.cancelSuccessTag, Snackbar.LENGTH_LONG).show();
                            }
                        }).create();
                deleteDialog.show();
            }
        });

        //Set up click on recyclerview entry logic
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentFile.type) {
                    case FILE:
                        //If entry is FILE, load file into the editor and move to the editor fragment
                        NotesEditorFragment.currentFile = new PupilFile(UserAccountControl.currentLoggedInUser, currentFile);
                        AppHelper.moveToFragment(NotesEditorFragment.class, null);
                        break;
                    case FOLDER:
                        //Get folder path from file JSON
                        String newPathAddition = currentFile.getFileName().split("\\.")[0];

                        NotesAppFragment.currentPath += "/" + newPathAddition;
                        currentDirectory = NotesAppFragment.currentPath;

                        NotesAppFragment.updateAdapter();
                        break;
                    case NONE:
                        break;
                }
            }
        });
    }

    //Required as part of RecyclerView.Adapter implementation
    @Override
    public int getItemCount() {
        return files.size();
    }

    //Simple class that binds UI to the Adapter & RecyclerView (mandatory as per implementation)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //UI element references
        public LinearLayout itemLayout;
        public ImageView itemIcon;
        public TextView itemDisplayName;
        public TextView itemFileName;
        public ImageButton deleteButton;

        //Default constructor
        public ViewHolder(View v) {
            super(v);
            //Load UI element references
            this.itemIcon = (ImageView) v.findViewById(R.id.noteIcon);
            this.itemDisplayName = (TextView) v.findViewById(R.id.itemDisplayName);
            this.itemFileName = (TextView) v.findViewById(R.id.itemFileName);
            this.deleteButton = (ImageButton) v.findViewById(R.id.deleteItemButton);
            this.itemLayout = (LinearLayout) v.findViewById(R.id.quizQuestionItemLayout);
        }
    }
}
