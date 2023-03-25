package com.github.sebastiankg02.csy2061.as1.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.NotesAppFragment;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.QuizAppFragment;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.UserProfileAppFragment;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;

public class MainMenuFragment extends Fragment {
    //References to UI elements within Fragment
    private ImageButton notesButton;
    private ImageButton quizButton;
    private ImageButton certsButton;
    private ImageButton profileButton;
    private ImageButton classMgmtButton;
    private ImageButton adminButton;
    private ImageButton exitButton;
    private LinearLayout masterLayout;

    //Default constructor
    public MainMenuFragment() {
        super(R.layout.fragment_main_menu);
    }

    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);
        AppHelper.fragmentManager = getActivity().getSupportFragmentManager();

        //Get references to UI elements
        masterLayout = (LinearLayout) this.getActivity().findViewById(R.id.mainMenuMasterLayout);
        notesButton = (ImageButton) this.getActivity().findViewById(R.id.notesAppButton);
        quizButton = (ImageButton) this.getActivity().findViewById(R.id.quizAppButton);
        certsButton = (ImageButton) this.getActivity().findViewById(R.id.certAppButton);
        profileButton = (ImageButton) this.getActivity().findViewById(R.id.profileAppButton);
        classMgmtButton = (ImageButton) this.getActivity().findViewById(R.id.classMgmtButton);
        adminButton = (ImageButton) this.getActivity().findViewById(R.id.adminAppButton);
        exitButton = (ImageButton) this.getActivity().findViewById(R.id.exitButton);

        //If user is less than ADMINISTRATOR, remove admin button access
        if (UserAccountControl.currentLoggedInUser.getRole().getAccessLevel() < 3) {
            masterLayout.removeView((View) adminButton);
            //If user is less than MODERATOR, remove class management button access
            if (UserAccountControl.currentLoggedInUser.getRole().getAccessLevel() < 2) {
                masterLayout.removeView((View) classMgmtButton);
                //If user is less than USER, remove all but profile and exit buttons
                if (UserAccountControl.currentLoggedInUser.getRole().getAccessLevel() < 1) {
                    masterLayout.removeView((View) notesButton);
                    masterLayout.removeView((View) quizButton);
                    masterLayout.removeView((View) certsButton);
                }
            }
        }

        //Provide exit button functionality
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        //Provide profile button functionality
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.moveToFragment(UserProfileAppFragment.class, null);
            }
        });

        //Provide notes button functionality
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.moveToFragment(NotesAppFragment.class, null);
            }
        });

        //Provide quiz button functionality
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.moveToFragment(QuizAppFragment.class, null);
            }
        });
        //TODO: Implement more apps!
    }
}
