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
import com.github.sebastiankg02.csy2061.as1.user.User;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PupilFileAdapter extends RecyclerView.Adapter<PupilFileAdapter.ViewHolder>{
    private ArrayList<PupilFile> files;

    public static final int FOLDER_ICON = android.R.drawable.sym_def_app_icon;
    public static final int FILE_ICON = android.R.drawable.ic_menu_edit;

    public PupilFileAdapter(String currentDirectory, Activity owner, User currentUser){
        //Load directory
        File f = new File(owner.getFilesDir() + "/" + currentDirectory);
        //Get all files in directory
        File[] allFilesInDir = f.listFiles();

        files = new ArrayList<PupilFile>();

        Log.i("NOTES", "Loading Notes from "+owner.getFilesDir()+"/" + currentDirectory);
        Log.i("NOTES", "FileStatus: " + f.exists() +"\nFiles?: " + allFilesInDir);

        if(f.exists() && allFilesInDir != null && allFilesInDir.length > 0) {
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
                            files.add(new PupilFile(currentDirectory + "\\",owner.getFilesDir() + "/" + currentDirectory + "/" + allFilesInDir[i].getName(), owner));
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

        if(currentFile.type.equals(PupilFile.PupilFileType.FILE)){
            holder.itemIcon.setImageResource(FILE_ICON);
        }else if(currentFile.type.equals(PupilFile.PupilFileType.FOLDER)){
            holder.itemIcon.setImageResource(FOLDER_ICON);
        }

        holder.itemDisplayName.setText(currentFile.getDisplayName());
        holder.itemFileName.setText(currentFile.getFileName());

        //Provide delete button functionality (create alert dialog, asking user to confirm, delete if true)
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog deleteDialog = AppHelper.createAlertDialogBuilder(view.getContext(), R.string.delete_file_title, R.string.delete_file_desc)
                        .setPositiveButton(R.string.delete_file_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                File toDelete = new File(view.getContext().getFilesDir(), currentFile.getPath() + currentFile.getFileName());
                                if(toDelete.exists()){
                                    if(toDelete.delete()) {
                                        Toast.makeText(view.getContext(), R.string.delete_file_complete, Toast.LENGTH_LONG);
                                    } else {
                                        Toast.makeText(view.getContext(), R.string.delete_file_error, Toast.LENGTH_LONG);
                                    }
                                } else {
                                    Toast.makeText(view.getContext(), R.string.delete_file_error, Toast.LENGTH_LONG);
                                }
                            }
                        }).setNegativeButton(R.string.delete_file_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(view.getContext(), R.string.delete_file_cancel_complete, Toast.LENGTH_LONG);
                            }
                        }).create();
                deleteDialog.show();
            }
        });

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "TODO: Implement this!", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout itemLayout;
        public ImageView itemIcon;
        public TextView itemDisplayName;
        public TextView itemFileName;
        public ImageButton deleteButton;

        public ViewHolder(View v){
            super(v);
            this.itemIcon = (ImageView) v.findViewById(R.id.noteIcon);
            this.itemDisplayName = (TextView) v.findViewById(R.id.itemDisplayName);
            this.itemFileName = (TextView) v.findViewById(R.id.itemFileName);
            this.deleteButton = (ImageButton) v.findViewById(R.id.deleteItemButton);
            this.itemLayout = (LinearLayout) v.findViewById(R.id.noteItemLayout);
        }
    }
}
