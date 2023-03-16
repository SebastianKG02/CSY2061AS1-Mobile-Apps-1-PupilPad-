package com.github.sebastiankg02.csy2061.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            UserAccountControl.init("/u.json", this);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        UserAccountControl.saveJSON(true);
    }
}