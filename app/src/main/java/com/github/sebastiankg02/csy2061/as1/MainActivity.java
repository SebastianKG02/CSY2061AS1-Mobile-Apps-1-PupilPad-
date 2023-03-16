package com.github.sebastiankg02.csy2061.as1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText usernameField;
    private TextInputEditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            UserAccountControl.init("/u.json", this);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        usernameField = (TextInputEditText) findViewById(R.id.usernameField);
        passwordField = (TextInputEditText) findViewById(R.id.passwordField);

        if(usernameField.getText() == null){
            Log.w("Main", "bro");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        UserAccountControl.saveJSON(true);
    }

    private void createAlertDialog(int title, int message, int okID){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);

        alertBuilder.setPositiveButton(okID, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    public void loginCheck(View v){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        if(username.isEmpty() || password.isEmpty()){
            createAlertDialog(R.string.login_failed, R.string.invalid_details, R.string.ok);
        } else {
            if(UserAccountControl.login(username, password)){
                createAlertDialog(R.string.login_success, R.string.login_success, R.string.ok);
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
}