package com.github.sebastiankg02.csy2061.as1;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.activity_main);
        AppHelper.fragmentManager = getSupportFragmentManager();
    }

}