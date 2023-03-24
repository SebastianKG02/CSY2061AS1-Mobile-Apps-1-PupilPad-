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

public class NotesAppFragment extends Fragment {
    public static String currentPath;
    private static PupilFileAdapter adapter;
    private View masterView;
    private ViewGroup viewGroup;
    private Button newFileButton;
    private Button newFolderButton;
    private Button backDirButton;
    private Button exitButton;
    private RecyclerView recycler;

    public NotesAppFragment() {
        super(R.layout.fragment_notes_app);
    }

    public static void updateAdapter() {
        adapter.loadItems();
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b) {
        masterView = inflater.inflate(R.layout.fragment_notes_app, vg, false);
        viewGroup = vg;
        return masterView;
    }

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
                            PupilFile newFile = PupilFile.createFileAt(currentPath, fileName.getText().toString(), UserAccountControl.currentLoggedInUser, UserAccountControl.mOwner);

                            if (!fileDisplayName.getText().toString().isEmpty()) {
                                newFile.setDisplayName(fileDisplayName.getText().toString());
                            }

                            if (!fileDisplayContent.getText().toString().isEmpty()) {
                                newFile.setTitle(fileDisplayContent.getText().toString());
                            }

                            newFile.save(getActivity(), false);

                            adapter.loadItems();
                            adapter.notifyDataSetChanged();

                            Snackbar.make(v, R.string.notes_app_new_file_success, Snackbar.LENGTH_LONG).show();
                        } catch (Exception e) {
                            dialogInterface.dismiss();
                            Snackbar.make(v, R.string.notes_app_new_file_error, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Snackbar.make(v, R.string.notes_app_new_file_cancel, Snackbar.LENGTH_SHORT).show();
                    }
                });
                //Show alertDialog
                builder.show();
            }
        });

        newFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                            PupilFile folder = PupilFile.createFolderAt(currentPath, folderName.getText().toString(), UserAccountControl.currentLoggedInUser, UserAccountControl.mOwner);
                            folder.setDisplayName(folderName.getText().toString());
                            folder.save(getActivity(), true);

                            adapter.loadItems();
                            adapter.notifyDataSetChanged();

                            Snackbar.make(v, R.string.notes_app_new_file_success, Snackbar.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            dialogInterface.cancel();
                            Snackbar.make(v, R.string.notes_app_new_folder_error, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(R.string.delete_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Snackbar.make(v, R.string.notes_app_new_folder_cancel, Snackbar.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        backDirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] fullPathSplit = NotesAppFragment.currentPath.split("/");
                if (fullPathSplit.length != 1) {
                    NotesAppFragment.currentPath = "";
                    for (int i = 0; i < fullPathSplit.length - 1; i++) {
                        NotesAppFragment.currentPath += fullPathSplit[i];
                        if (i != fullPathSplit.length - 2) {
                            NotesAppFragment.currentPath += "/";
                        }
                    }
                    PupilFileAdapter.currentDirectory = NotesAppFragment.currentPath;
                    updateAdapter();
                } else {
                    Snackbar.make(v, R.string.notes_app_cant_go_back, Snackbar.LENGTH_SHORT);
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.back(getFragment());
            }
        });
    }

    private Fragment getFragment() {
        return this;
    }
}
