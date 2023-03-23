package com.github.sebastiankg02.csy2061.as1.fragments.apps;

import android.content.DialogInterface;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;

public class NotesAppFragment extends Fragment {

    private View masterView;
    private ViewGroup viewGroup;
    private Button newFileButton;
    private Button backDirButton;
    private Button exitButton;
    private RecyclerView recycler;

    private static String currentPath;

    public NotesAppFragment(){
        super(R.layout.fragment_notes_app);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b){
        masterView = inflater.inflate(R.layout.fragment_notes_app, vg, false);
        viewGroup = vg;
        return masterView;
    }
    
    @Override
    public void onViewCreated(View v, Bundle b){
        super.onViewCreated(v, b);

        //Ensure path is populated
        if(currentPath == null || currentPath.isEmpty()){
            currentPath = "notes";
        }

        //Get references to views used by this fragment
        newFileButton = (Button) masterView.findViewById(R.id.newFileButton);
        backDirButton = (Button) masterView.findViewById(R.id.previousFolderButton);
        exitButton = (Button) masterView.findViewById(R.id.notesAppExit);
        recycler = (RecyclerView) masterView.findViewById(R.id.notesAppRecycler);

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
                EditText fileDisplayContent =(EditText) inf.findViewById(R.id.newFileDialogTitle);
                builder.setView(inf).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            dialogInterface.dismiss();
                            PupilFile.createFileAt(currentPath, fileName.getText().toString(), UserAccountControl.currentLoggedInUser, UserAccountControl.mOwner);
                            Snackbar.make(v, R.string.notes_app_new_file_success, Snackbar.LENGTH_LONG).show();

                        } catch (Exception e) {
                            dialogInterface.dismiss();
                            Snackbar.make(v, R.string.notes_app_new_file_error, Snackbar.LENGTH_LONG).show();
                            Log.e("NOTES", "Error while making new file "+currentPath+"/"+fileName.getText().toString() +": " + e.getMessage());
                            throw new RuntimeException(e);
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

        recycler = (RecyclerView) masterView.findViewById(R.id.notesAppRecycler);
        PupilFileAdapter adapter = new PupilFileAdapter(currentPath, UserAccountControl.mOwner, UserAccountControl.currentLoggedInUser);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler.setAdapter(adapter);
    }
}
