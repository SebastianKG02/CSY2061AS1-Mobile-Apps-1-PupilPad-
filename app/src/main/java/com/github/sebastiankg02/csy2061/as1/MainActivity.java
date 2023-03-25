package com.github.sebastiankg02.csy2061.as1;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;

public class MainActivity extends AppCompatActivity {

	//Define main entry point to app
    public MainActivity() {
        super(R.layout.activity_main);
		//Facilitate movement between fragments
        AppHelper.fragmentManager = getSupportFragmentManager();
    }

}