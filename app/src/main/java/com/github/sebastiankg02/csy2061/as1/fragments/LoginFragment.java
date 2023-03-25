package com.github.sebastiankg02.csy2061.as1.fragments;

import static com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper.createAlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.io.IOException;

//Handles logging in - first fragment displayed to user
public class LoginFragment extends Fragment {
    //References to UI elements
    private TextInputEditText usernameField;
    private TextInputEditText passwordField;

    //Default constructor
    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialise UAC system
        try {
            UserAccountControl.init("u.json", this.getActivity());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        //Get references to UI elements
        usernameField = (TextInputEditText) this.getActivity().findViewById(R.id.usernameField);
        passwordField = (TextInputEditText) this.getActivity().findViewById(R.id.passwordField);

        //Provide loginButton functionality
        Button loginButton = (Button) getActivity().findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get entered text from fields
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                //Check if username & password not empty
                if (username.isEmpty() || password.isEmpty()) {
                    //Inform user of fail
                    createFragmentAlertDialog(R.string.login_failed, R.string.invalid_details, R.string.ok);
                } else {
                    //Attempt to log in
                    if (UserAccountControl.login(username, password)) {
                        //Inform user of success
                        createFragmentAlertDialog(R.string.login_success, R.string.login_success, R.string.contToMain);
                    } else {
                        //Inform user of fail
                        createFragmentAlertDialog(R.string.login_failed, R.string.invalid_details, R.string.ok);
                    }
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        //Save user JSON
        UserAccountControl.saveJSON(true);
    }

    //Helper method to create AlertDialogs
    public void createFragmentAlertDialog(int title, int message, int okID) {
        createAlertDialog(this, title, message, okID);
    }
}
