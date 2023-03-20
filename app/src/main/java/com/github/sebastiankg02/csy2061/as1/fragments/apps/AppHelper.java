package com.github.sebastiankg02.csy2061.as1.fragments.apps;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.fragments.MainMenuFragment;

public class AppHelper {
    public static void createAlertDialog(Fragment a, int title, int message, int okID){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(a.getContext());

        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);

        alertBuilder.setPositiveButton(okID, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(okID == R.string.contToMain){
                    moveToFragment(a, MainMenuFragment.class, null);
                }
            }
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    public static void moveToFragment(Fragment owner, Class<? extends Fragment> destination, Bundle b){
        FragmentManager manager = owner.getActivity().getSupportFragmentManager();
        FragmentTransaction move = manager.beginTransaction();
        move.replace(R.id.mainContainer, destination, b).addToBackStack(owner.getClass().getName());
        move.commit();
    }

    public static void back(Fragment owner){
        owner.getActivity().getSupportFragmentManager().popBackStackImmediate();
    }
}
