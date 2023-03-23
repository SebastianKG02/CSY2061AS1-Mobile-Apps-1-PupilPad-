package com.github.sebastiankg02.csy2061.as1.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;

public class UserInfoFragment extends Fragment {
    private View masterView;
    private TextView userFullNameField;
    private TextView userStageInfoField;

    public UserInfoFragment(Bundle b){
        super(R.layout.fragment_user_info);
    }

    public UserInfoFragment() {
        super(R.layout.fragment_user_info);
        onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b){
        masterView = inflater.inflate(R.layout.fragment_user_info, vg, false);
        return masterView;
    }

    @Override
    public void onViewCreated(View v, Bundle b){
        super.onViewCreated(v, b);
        Log.i("UPI", "Updating Fields..");
        userFullNameField = (TextView) masterView.findViewById(R.id.userFullName);
        userStageInfoField = (TextView) masterView.findViewById(R.id.userStageRole);

        userFullNameField.setText(UserAccountControl.currentLoggedInUser.getProfile().getNameAsSingleString());
        userStageInfoField.setText(getActivity().getString(UserAccountControl.currentLoggedInUser.getRole().getLongRoleID()) + "\n" + getActivity().getString(UserAccountControl.currentLoggedInUser.getProfile().getKeyStage().getLongEducationTag()));
    }

    @Override
    public void onStart(){
        super.onStart();
    }

}
