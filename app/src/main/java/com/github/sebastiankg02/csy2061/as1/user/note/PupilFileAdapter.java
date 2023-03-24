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
import android.widget.Toast;

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

public class PupilFileAdapter extends RecyclerView.Adapter<PupilFileAdapter.ViewHolder> {
    public static final int FOLDER_ICON = android.R.drawable.ic_menu_agenda;
    public static final int FILE_ICON = android.R.drawable.ic_menu_edit;
    public static String currentDirectory;
    public ArrayList<PupilFile> files;
    private Activity owner;
    private User currentUser;

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

        Log.i("NOTES", "Loading Notes from " + owner.getFilesDir() + "/" + currentDirectory);
        Log.i("NOTES", "FileStatus: " + f.exists() + "\nFiles?: " + allFilesInDir);

        if (f.exists() && allFilesInDir != null && allFilesInDir.length > 0) {
            //Loop through files, determine which files & folders user has access to
            for (int i = 0; i < allFilesInDir.length; i++) {
                //Verify file is actual file
                if (allFilesInDir[i].isFile()) {
                    //Verify all files are JSON files only
                    Log.i("NOTES", allFilesInDir[i].getName());
                    if (allFilesInDir[i].getName().endsWith("json")) {
                        try {
                            //Load the file
                            Log.i("NOTES", "Loading: " + currentDirectory + "/" + allFilesInDir[i].getName());
                            files.add(new PupilFile(currentDirectory + "\\", owner.getFilesDir() + "/" + currentDirectory + "/" + allFilesInDir[i].getName(), owner));
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PupilFile currentFile = files.get(position);

        if (currentFile.type.equals(PupilFile.PupilFileType.FILE)) {
            holder.itemIcon.setImageResource(FILE_ICON);
        } else if (currentFile.type.equals(PupilFile.PupilFileType.FOLDER)) {
            holder.itemIcon.setImageResource(FOLDER_ICON);
        }

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
                AlertDialog deleteDialog = AppHelper.createAlertDialogBuilder(view.getContext(), currentFile.type.deleteDialogTitle, currentFile.type.deleteDialogDesc)
                        .setPositiveButton(currentFile.type.deleteTag, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                File toDelete = new File(view.getContext().getFilesDir(), currentFile.getPath() + currentFile.getFileName());
                                if (toDelete.exists()) {
                                    if (toDelete.delete()) {
                                        Snackbar.make(view, currentFile.type.deleteSuccess, Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        Snackbar.make(view, currentFile.type.deleteFail, Snackbar.LENGTH_LONG).show();
                                    }
                                } else {
                                    Snackbar.make(view, currentFile.type.deleteFail, Snackbar.LENGTH_LONG).show();
                                }
                                files.remove(currentFile);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton(currentFile.type.cancelTag, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(view.getContext(), currentFile.type.cancelSuccessTag, Toast.LENGTH_LONG).show();
                            }
                        }).create();
                deleteDialog.show();
            }
        });

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "TODO: Implement this!", Toast.LENGTH_SHORT).show();
                switch (currentFile.type) {
                    case FILE:
                        NotesEditorFragment.currentFile = new PupilFile(UserAccountControl.currentLoggedInUser, currentFile);
                        AppHelper.moveToFragment(NotesEditorFragment.class, null);
                        break;
                    case FOLDER:
                        //Get folder path from file JSON
                        Log.i("AH", "Attempting to move to new folder: " + currentFile.getFileName().split("\\.").length);
                        String newPathAddition = currentFile.getFileName().split("\\.")[0];
                        NotesAppFragment.currentPath += "/" + newPathAddition;
                        currentDirectory = NotesAppFragment.currentPath;
                        Log.i("AH", "Entering new folder! [" + NotesAppFragment.currentPath + "]");
                        NotesAppFragment.updateAdapter();
                        break;
                    case NONE:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public ImageView itemIcon;
        public TextView itemDisplayName;
        public TextView itemFileName;
        public ImageButton deleteButton;

        public ViewHolder(View v) {
            super(v);
            this.itemIcon = (ImageView) v.findViewById(R.id.noteIcon);
            this.itemDisplayName = (TextView) v.findViewById(R.id.itemDisplayName);
            this.itemFileName = (TextView) v.findViewById(R.id.itemFileName);
            this.deleteButton = (ImageButton) v.findViewById(R.id.deleteItemButton);
            this.itemLayout = (LinearLayout) v.findViewById(R.id.noteItemLayout);
        }
    }
}
