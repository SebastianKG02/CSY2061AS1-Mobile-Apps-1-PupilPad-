package com.github.sebastiankg02.csy2061.as1.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.user.User;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;

public class UserInfoFragment extends Fragment {
    private TextView userFullNameField;
    private TextView userStageInfoField;

    public UserInfoFragment(Bundle b){
        super(R.layout.fragment_user_info);
    }

    public UserInfoFragment(){
        super(R.layout.fragment_user_info);
    }

    @Override
    public void onViewCreated(View v, Bundle b){
        super.onViewCreated(v, b);

        userFullNameField = (TextView) this.getActivity().findViewById(R.id.userFullName);
        userStageInfoField = (TextView) this.getActivity().findViewById(R.id.userStageRole);

        User currentUser = UserAccountControl.getCurrentLoggedInUser();

        userFullNameField.setText(currentUser.getProfile().getNameAsSingleString());
        userStageInfoField.setText(getActivity().getString(currentUser.getRole().getLongRoleID()) + "\n" + getActivity().getString(currentUser.getProfile().getKeyStage().getLongEducationTag()));
    }
}
