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

public class NotesEditorFragment extends Fragment {
    public static PupilFile currentFile;

    private View masterView;
    private TextView filePath;
    private EditText fileTitle;
    private EditText editor;
    private Button saveButton;
    private Button backButton;

    public NotesEditorFragment() {
        super(R.layout.fragment_note_edit);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b) {
        masterView = inflater.inflate(R.layout.fragment_note_edit, vg, false);
        return masterView;
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        filePath = (TextView) masterView.findViewById(R.id.fileCurrentlyEditingPath);
        fileTitle = (EditText) masterView.findViewById(R.id.fileTitle);
        editor = (EditText) masterView.findViewById(R.id.noteEditor);
        saveButton = (Button) masterView.findViewById(R.id.saveButton);
        backButton = (Button) masterView.findViewById(R.id.notesEditorExit);

        filePath.setText(currentFile.getPath() + "/" + currentFile.getFileName() + "\t[" + currentFile.getDisplayName() + "]");
        fileTitle.setText(currentFile.getTitle());
        editor.setText(currentFile.getContents());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileTitle.getText().toString().isEmpty()) {
                    currentFile.setTitle("Unnamed Note");
                } else {
                    currentFile.setTitle(fileTitle.getText().toString());
                }

                currentFile.setContents(editor.getText().toString().trim());

                try {
                    if (!currentFile.save(getActivity(), true).equals(AppHelper.FileSystemReturn.ERROR)) {
                        Snackbar.make(v, R.string.notes_edit_save_success, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(v, R.string.notes_edit_save_error, Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Snackbar.make(v, R.string.notes_edit_save_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentFile.getContents().equals(editor.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext()).setTitle(R.string.notes_edit_exit_dialog_title).setMessage(R.string.notes_edit_exit_dialog_desc);
                    builder.setPositiveButton(R.string.notes_edit_exit_dialog_save_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (fileTitle.getText().toString().isEmpty()) {
                                currentFile.setTitle("Unnamed Note");
                            } else {
                                currentFile.setTitle(fileTitle.getText().toString());
                            }

                            currentFile.setContents(editor.getText().toString().trim());

                            try {
                                if (!currentFile.save(getActivity(), true).equals(AppHelper.FileSystemReturn.ERROR)) {
                                    Snackbar.make(v, R.string.notes_edit_save_success, Snackbar.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                    AppHelper.back(getFragment());
                                } else {
                                    Snackbar.make(v, R.string.notes_edit_save_error, Snackbar.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Snackbar.make(v, R.string.notes_edit_save_error, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }).setNeutralButton(R.string.notes_edit_exit_dialog_exit_only, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            AppHelper.back(getFragment());
                        }
                    }).setNegativeButton(R.string.delete_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                } else {
                    AppHelper.back(getFragment());
                }
            }
        });
    }

    private Fragment getFragment() {
        return this;
    }
}
