package com.github.sebastiankg02.csy2061.as1.fragments.apps;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.user.note.PupilFile;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

/**
 * Managing class for the Notes Editor - controls UI functionality
 */
public class NotesEditorFragment extends Fragment {
    //Current file being edited, can be accessed App-wide to facilitate cross-class functionality
    public static PupilFile currentFile;
    //Main view for Fragment
    private View masterView;
    //References to UI components
    private TextView filePath;
    private EditText fileTitle;
    private EditText editor;
    private Button saveButton;
    private Button backButton;

    //Default constructor
    public NotesEditorFragment() {
        super(R.layout.fragment_note_edit);
    }

    //Inflate view to ensure UI components are loaded and displayed correctly
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b) {
        masterView = inflater.inflate(R.layout.fragment_note_edit, vg, false);
        return masterView;
    }

    //Set up UI functionality
    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        //Get references to views used by this fragment
        filePath = (TextView) masterView.findViewById(R.id.fileCurrentlyEditingPath);
        fileTitle = (EditText) masterView.findViewById(R.id.fileTitle);
        editor = (EditText) masterView.findViewById(R.id.noteEditor);
        saveButton = (Button) masterView.findViewById(R.id.saveButton);
        backButton = (Button) masterView.findViewById(R.id.notesEditorExit);

        //Update UI elements for current file
        filePath.setText(currentFile.getPath() + currentFile.getFileName() + "\t[" + currentFile.getDisplayName() + "]");
        fileTitle.setText(currentFile.getTitle());
        editor.setText(currentFile.getContents());

        //Provide save button functionality
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do not allow untitled notes, if title is empty use default "Unnamed note", otherwise update title
                if (fileTitle.getText().toString().isEmpty()) {
                    currentFile.setTitle("Unnamed Note");
                } else {
                    currentFile.setTitle(fileTitle.getText().toString());
                }

                //Update file contents
                currentFile.setContents(editor.getText().toString().trim());

                try {
                    //Save file, inform user of success or failure
                    if (!currentFile.save(getActivity()).equals(AppHelper.FileSystemReturn.ERROR)) {
                        Snackbar.make(v, R.string.notes_edit_save_success, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(v, R.string.notes_edit_save_error, Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //Inform user of saving error
                    Snackbar.make(v, R.string.notes_edit_save_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //Provide back button functionality
        //Display AlertDialog confirming user wants to exit if no changes made to file content
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check for any changes in file and contents
                if (!currentFile.getContents().equals(editor.getText().toString())) {
                    //Initialise AlertDialog Builder
                    AlertDialog.Builder builder = AppHelper.createAlertDialogBuilder(v.getContext(), R.string.notes_edit_exit_dialog_title, R.string.notes_edit_exit_dialog_desc);
                    builder.setPositiveButton(R.string.notes_edit_exit_dialog_save_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //Do not allow untitled notes, if title is empty use default "Unnamed note", otherwise update title
                            if (fileTitle.getText().toString().isEmpty()) {
                                currentFile.setTitle("Unnamed Note");
                            } else {
                                currentFile.setTitle(fileTitle.getText().toString());
                            }

                            //Update file contents
                            currentFile.setContents(editor.getText().toString().trim());

                            try {
                                //Save file, inform user of success or failure
                                if (!currentFile.save(getActivity()).equals(AppHelper.FileSystemReturn.ERROR)) {
                                    Snackbar.make(v, R.string.notes_edit_save_success, Snackbar.LENGTH_SHORT).show();
                                    //Go back to NotesApp after saving contents
                                    AppHelper.back(getFragment());
                                } else {
                                    //Inform user of saving error
                                    Snackbar.make(v, R.string.notes_edit_save_error, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                //Inform user of saving error
                                Snackbar.make(v, R.string.notes_edit_save_error, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }).setNeutralButton(R.string.notes_edit_exit_dialog_exit_only, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Go back to NotesApp without saving content
                            dialogInterface.dismiss();
                            AppHelper.back(getFragment());
                        }
                    }).setNegativeButton(R.string.delete_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Cancel dialog
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                } else {
                    //If no changes detected, go to NotesApp
                    AppHelper.back(getFragment());
                }
            }
        });
    }

    //Get this class (for back button)
    private Fragment getFragment() {
        return this;
    }
}
