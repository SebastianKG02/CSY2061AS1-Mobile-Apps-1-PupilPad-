package com.github.sebastiankg02.csy2061.as1.fragments.apps;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;
import com.github.sebastiankg02.csy2061.as1.user.note.PupilFile;
import com.github.sebastiankg02.csy2061.as1.user.note.PupilFileAdapter;
import com.google.android.material.snackbar.Snackbar;

/**
 * Managing Class for the Notes App - controls UI functionality
 */
public class NotesAppFragment extends Fragment {
    //App-wide reference to current position within the App's internal storage from where notes are to be loaded
    public static String currentPath;
    //RecyclerView Adapter responsible for loading view elements and controlling their logic
    private static PupilFileAdapter adapter;
    //Fragment parent view, used to grab accurate references to UI elements
    private View masterView;
    //Parent ViewGroup
    private ViewGroup viewGroup;
    //References to UI buttons, to be loaded from masterView
    private Button newFileButton;
    private Button newFolderButton;
    private Button backDirButton;
    private Button exitButton;
    //Reference to UI component part of PupilFileAdapter
    private RecyclerView recycler;

    //Default constructor
    public NotesAppFragment() {
        super(R.layout.fragment_notes_app);
    }

    //External method to be used from within PupilFileAdapter - forces update when an element is deleted
    public static void updateAdapter() {
        adapter.loadItems();
        adapter.notifyDataSetChanged();
    }

    //Inflate view to ensure UI components are loaded and displayed correctly
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b) {
        masterView = inflater.inflate(R.layout.fragment_notes_app, vg, false);
        viewGroup = vg;
        return masterView;
    }

    //Set up UI functionality
    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        //Ensure path is populated
        if (currentPath == null || currentPath.isEmpty()) {
            currentPath = "notes";
        }

        //Get references to views used by this fragment
        newFileButton = (Button) masterView.findViewById(R.id.newFileButton);
        newFolderButton = (Button) masterView.findViewById(R.id.newFolderButton);
        backDirButton = (Button) masterView.findViewById(R.id.previousFolderButton);
        exitButton = (Button) masterView.findViewById(R.id.notesAppExit);
        recycler = (RecyclerView) masterView.findViewById(R.id.notesAppRecycler);

        //Set RecyclerView up to use PupilFileAdapter
        recycler = (RecyclerView) masterView.findViewById(R.id.notesAppRecycler);
        adapter = new PupilFileAdapter(currentPath, UserAccountControl.mOwner, UserAccountControl.currentLoggedInUser);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler.setAdapter(adapter);

        //Set new button functionality
        newFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext()).setTitle(R.string.notes_app_new_file);
                //Create view for custom AlertDialog
                View inf = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_new_file, viewGroup, false);
                //Set references to internal dialog EditTexts
                EditText fileName = (EditText) inf.findViewById(R.id.newFileDialogFileName);
                EditText fileDisplayName = (EditText) inf.findViewById(R.id.newFileDialogDisplayName);
                EditText fileDisplayContent = (EditText) inf.findViewById(R.id.newFileDialogTitle);
                builder.setView(inf).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            dialogInterface.dismiss();
                            //Create new PupilFile instance
                            PupilFile newFile = PupilFile.createFileAt(currentPath, fileName.getText().toString(), UserAccountControl.currentLoggedInUser, UserAccountControl.mOwner);

                            //Update newFile displayName if user has provided one, otherwise value from default constructor will be used
                            if (!fileDisplayName.getText().toString().isEmpty()) {
                                newFile.setDisplayName(fileDisplayName.getText().toString());
                            }

                            //Update newFile title if user has provided one, otherwise value from default constructor will be used
                            if (!fileDisplayContent.getText().toString().isEmpty()) {
                                newFile.setTitle(fileDisplayContent.getText().toString());
                            }

                            //Save changes to file
                            newFile.save(getActivity());
                            //Update recycler view contents via adapter
                            updateAdapter();
                            //Inform user of action completion
                            Snackbar.make(v, R.string.notes_app_new_file_success, Snackbar.LENGTH_LONG).show();
                        } catch (Exception e) {
                            dialogInterface.dismiss();
                            //Inform user that new file could not be made
                            Snackbar.make(v, R.string.notes_app_new_file_error, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        //Inform user that action has been cancelled
                        Snackbar.make(v, R.string.notes_app_new_file_cancel, Snackbar.LENGTH_SHORT).show();
                    }
                });
                //Show alertDialog
                builder.show();
            }
        });

        //New folder button logic
        newFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create custom AlertDialog for new folder creation
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext()).setTitle(R.string.notes_app_new_folder);
                View inf = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_new_folder, viewGroup, false);
                //Set reference to fileName EditText
                EditText folderName = (EditText) inf.findViewById(R.id.newFolderDialogFileName);
                folderName.setText("New Folder");
                builder.setView(inf).setPositiveButton(R.string.notes_app_new_folder, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            dialogInterface.dismiss();

                            //Create new folder (via json file, and physically)
                            PupilFile folder = PupilFile.createFolderAt(currentPath, folderName.getText().toString(), UserAccountControl.currentLoggedInUser, UserAccountControl.mOwner);
                            folder.setDisplayName(folderName.getText().toString());
                            folder.save(getActivity());

                            //Update RecyclerView adapter
                            updateAdapter();

                            //Inform user of action success
                            Snackbar.make(v, R.string.notes_app_new_file_success, Snackbar.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            dialogInterface.cancel();
                            //Inform user of action error
                            Snackbar.make(v, R.string.notes_app_new_folder_error, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(R.string.delete_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        //Inform user of successful action cancellation
                        Snackbar.make(v, R.string.notes_app_new_folder_cancel, Snackbar.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        //Provide back to previous folder button functionality
        backDirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Split current path into individual directories
                String[] fullPathSplit = NotesAppFragment.currentPath.split("/");
                //Ensure first element of path (/notes/) is never deleted
                if (fullPathSplit.length != 1) {
                    //Wipe current path
                    NotesAppFragment.currentPath = "";
                    //Loop through current path directories (less the bottom level directory)
                    for (int i = 0; i < fullPathSplit.length - 1; i++) {
                        //Add current directory to currentPath
                        NotesAppFragment.currentPath += fullPathSplit[i];
                        //Add path separators, apart from last addition to currentPath
                        if (i != fullPathSplit.length - 2) {
                            NotesAppFragment.currentPath += "/";
                        }
                    }
                    //Update adapter's directory
                    PupilFileAdapter.currentDirectory = NotesAppFragment.currentPath;
                    //Update adapter display contents
                    updateAdapter();
                } else {
                    //Notify user of impossible move
                    Snackbar.make(v, R.string.notes_app_cant_go_back, Snackbar.LENGTH_SHORT);
                }
            }
        });

        //Provide back button functionality
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.back(getFragment());
            }
        });
    }

    //Used for back button functionality, gets this Fragment's class
    private Fragment getFragment() {
        return this;
    }
}
