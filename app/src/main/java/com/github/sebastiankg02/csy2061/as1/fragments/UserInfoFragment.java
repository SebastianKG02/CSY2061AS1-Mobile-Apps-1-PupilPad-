package com.github.sebastiankg02.csy2061.as1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;

//Simple fragment that loads user information
public class UserInfoFragment extends Fragment {
    private View masterView;
    //UI elements
    private TextView userFullNameField;
    private TextView userStageInfoField;

    //Default constructor
    public UserInfoFragment() {
        super(R.layout.fragment_user_info);
        onStart();
    }

    //Inflate view to ensure UI components are loaded and displayed correctly
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b) {
        masterView = inflater.inflate(R.layout.fragment_user_info, vg, false);
        return masterView;
    }

    //Set up UI functionality
    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);
        //Get references to UI elements
        userFullNameField = (TextView) masterView.findViewById(R.id.userFullName);
        userStageInfoField = (TextView) masterView.findViewById(R.id.userStageRole);

        //Set text of fields
        userFullNameField.setText(UserAccountControl.currentLoggedInUser.getProfile().getNameAsSingleString());
        userStageInfoField.setText(getActivity().getString(UserAccountControl.currentLoggedInUser.getRole().getLongRoleID()) + "\n" + getActivity().getString(UserAccountControl.currentLoggedInUser.getProfile().getKeyStage().getLongEducationTag()));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
