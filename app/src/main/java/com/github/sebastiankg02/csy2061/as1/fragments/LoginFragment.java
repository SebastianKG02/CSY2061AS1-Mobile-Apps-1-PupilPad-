package com.github.sebastiankg02.csy2061.as1.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.io.IOException;

public class LoginFragment extends Fragment {
    private TextInputEditText usernameField;
    private TextInputEditText passwordField;

    public LoginFragment(){
        super(R.layout.fragment_login);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Log.w("LOGIN", "Loading");
        try {
            UserAccountControl.init("/u.json", this.getActivity());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onViewCreated(View v, Bundle b){
        super.onViewCreated(v, b);

        usernameField = (TextInputEditText) this.getActivity().findViewById(R.id.usernameField);
        passwordField = (TextInputEditText) this.getActivity().findViewById(R.id.passwordField);

        Button loginButton = (Button) getActivity().findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                if(username.isEmpty() || password.isEmpty()){
                    createAlertDialog(R.string.login_failed, R.string.invalid_details, R.string.ok);
                } else {
                    if(UserAccountControl.login(username, password)){
                        createAlertDialog(R.string.login_success, R.string.login_success, R.string.cont);
                        try {
                            Log.i("UAC", "Login success! \n" +UserAccountControl.getCurrentLoggedInUser().toJSON().toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        createAlertDialog(R.string.login_failed, R.string.invalid_details, R.string.ok);
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        UserAccountControl.saveJSON(true);
    }

    private void createAlertDialog(int title, int message, int okID){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this.getContext());

        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);

        alertBuilder.setPositiveButton(okID, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(okID == R.string.cont){
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction moveTransaction = manager.beginTransaction();
                    moveTransaction.replace(R.id.mainContainer, MainMenuFragment.class, null);
                    moveTransaction.commit();
                }
            }
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }
}
