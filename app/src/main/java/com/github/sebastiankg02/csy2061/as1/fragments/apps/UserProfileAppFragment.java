package com.github.sebastiankg02.csy2061.as1.fragments.apps;

import static com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper.createAlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.user.User;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class UserProfileAppFragment extends Fragment {
    //References to UI elements
    private LinearLayout masterLayout;
    private TextInputEditText fullNameField;
    private TextInputEditText birthdateField;
    private Button saveProfileButton;
    private TextInputEditText currentPasswordField;
    private TextInputEditText newPasswordField;
    private TextInputEditText confirmNewPasswordField;
    private Button setNewPasswordButton;
    private Button backButton;

    //Default constructor
    public UserProfileAppFragment() {
        super(R.layout.fragment_user_profile);
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        //Initialise reference to master layout, provides Snackbar functionality
        masterLayout = (LinearLayout) this.getActivity().findViewById(R.id.masterLayout);

        //Reference to current logged in user (speeds up data get)
        User current = UserAccountControl.currentLoggedInUser;

        //Initialise profile fields and populate with existing data from profile
        fullNameField = (TextInputEditText) this.getActivity().findViewById(R.id.fullNameInputField);
        fullNameField.setText(current.getProfile().getNameAsSingleString());
        birthdateField = (TextInputEditText) this.getActivity().findViewById(R.id.birthdateInputField);
        birthdateField.setText(current.getProfile().getBirthdateAsString());

        //Initialise button reference
        saveProfileButton = (Button) this.getActivity().findViewById(R.id.saveProfileInfo);
        //Provide save button functionality
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get input data
                String fullName = fullNameField.getText().toString();
                String birthdate = birthdateField.getText().toString();

                //If full name field not empty, not equal to current name
                if (!fullName.isEmpty() && !fullName.equals(current.getProfile().getNameAsSingleString())) {
                    //Set new user name, if unsuccessful, inform user of issue via alertDialog
                    if (!UserAccountControl.currentLoggedInUser.getProfile().setName(fullName.trim())) {
                        createFragmentAlertDialog(R.string.user_profile_invalid_full_name_title, R.string.user_profile_invalid_full_name_desc, R.string.ok);
                    } else {
                        //If successfully set new full name, display accomplishment message & update full name field
                        Snackbar.make((View) masterLayout, R.string.user_profile_edit_success, Snackbar.LENGTH_SHORT);
                        fullNameField.setText(UserAccountControl.currentLoggedInUser.getProfile().getNameAsSingleString());
                    }
                    //If not, inform user that change needs to be present
                } else {
                    Snackbar.make((View) masterLayout, R.string.user_profile_no_change, Snackbar.LENGTH_LONG);
                }

                //If birthdate field not empty, not equal to current birthdate
                if (!birthdate.isEmpty() && !birthdate.equals(current.getProfile().getBirthdateAsString())) {
                    //Set new brithdate, if unsuccessful, inform user of issue via alertDialog
                    if (!UserAccountControl.currentLoggedInUser.getProfile().setBirthdate(birthdate)) {
                        createFragmentAlertDialog(R.string.user_profile_invalid_birthdate_title, R.string.user_profile_invalid_birthdate_desc, R.string.ok);
                    } else {
                        //If successfully set new birthdate, inform user of success, update birthdate field
                        Snackbar.make((View) masterLayout, R.string.user_profile_edit_success, Snackbar.LENGTH_SHORT);
                        birthdateField.setText(UserAccountControl.currentLoggedInUser.getProfile().getBirthdateAsString());
                    }
                    //If not, inform user that change needs to be present
                } else {
                    Snackbar.make((View) masterLayout, R.string.user_profile_no_change, Snackbar.LENGTH_LONG);
                }

                //Update user database & JSON, save file
                UserAccountControl.saveJSON(true);
            }
        });

        //Get references to new password UI elements
        currentPasswordField = (TextInputEditText) this.getActivity().findViewById(R.id.currentPWInputField);
        newPasswordField = (TextInputEditText) this.getActivity().findViewById(R.id.newPWInputField);
        confirmNewPasswordField = (TextInputEditText) this.getActivity().findViewById(R.id.confirmPWInputField);

        //Provide savePassword button functionality
        setNewPasswordButton = (Button) this.getActivity().findViewById(R.id.saveNewPassword);
        setNewPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPW = currentPasswordField.getText().toString();
                String newPW = newPasswordField.getText().toString();
                String newPWConfirm = confirmNewPasswordField.getText().toString();

                //Check if current password is not empty
                if (currentPW.isEmpty()) {
                    //If it is empty, inform user of issue
                    createFragmentAlertDialog(R.string.user_password_current_pw_invalid_title, R.string.user_password_current_pw_invalid_desc, R.string.ok);
                } else {
                    //Check if current password matches password on file
                    if (UserAccountControl.currentLoggedInUser.getPassword().equals(currentPW.trim())) {
                        //Check if both new passwords are equal
                        if (newPW.trim().equals(newPWConfirm.trim())) {
                            //Check if new password is valid
                            if (UserAccountControl.currentLoggedInUser.validatePassword(newPW.trim()).isPasswordValid()) {
                                //Check if new password is different to existing password
                                if (!UserAccountControl.currentLoggedInUser.checkPasswordSimilarity(newPW.trim())) {
                                    //Process password change
                                    UserAccountControl.currentLoggedInUser.setNewPassword(newPW.trim());
                                    UserAccountControl.saveJSON(true);
                                    Snackbar.make((View) masterLayout, R.string.user_password_success, Snackbar.LENGTH_LONG).show();
                                } else {
                                    //Inform user that new password must be different to existing password
                                    createFragmentAlertDialog(R.string.user_password_new_nochange_title, R.string.user_password_new_nochange_desc, R.string.ok);
                                }
                            } else {
                                //Inform user that new password is not valid
                                createFragmentAlertDialog(R.string.user_password_new_not_valid_title, R.string.requirements, R.string.ok);
                            }
                        } else {
                            //If not, inform user of error
                            createFragmentAlertDialog(R.string.user_password_new_noconfirm_title, R.string.user_password_new_noconfirm_desc, R.string.ok);
                        }
                    } else {
                        //If not, inform user that their current password is incorrect
                        createFragmentAlertDialog(R.string.user_password_current_pw_incorrect_title, R.string.user_password_current_pw_incorrect_desc, R.string.ok);
                    }
                }
            }
        });

        //Provide back button functionality
        backButton = (Button) this.getActivity().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.back(getFragment());
            }
        });

    }

    //Provide reference to this class
    private Fragment getFragment() {
        return this;
    }

    //Create AlertDialog without reference to this fragment, used internally
    private void createFragmentAlertDialog(int title, int message, int okID) {
        createAlertDialog(this, title, message, okID);
    }
}
