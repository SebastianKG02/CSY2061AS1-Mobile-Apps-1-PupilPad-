package com.github.sebastiankg02.csy2061.as1.fragments.apps;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.quiz.Certificate;
import com.github.sebastiankg02.csy2061.as1.quiz.Quiz;
import com.github.sebastiankg02.csy2061.as1.quiz.QuizAdapter;
import com.github.sebastiankg02.csy2061.as1.quiz.QuizAnswer;
import com.github.sebastiankg02.csy2061.as1.quiz.QuizQuestion;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.Arrays;

/**
 * Main Class for displaying available Quizzes - controls UI functionality for the fragment
 */
public class QuizAppFragment extends Fragment {
    //RecyclerView adapter for displaying quizzes
    private static QuizAdapter adapter;
    private View masterView;
    private ViewGroup viewGroup;
    //UI component references
    private RecyclerView recycler;
    private Button wipeButton;
    private Button backButton;

    //Default constructor
    public QuizAppFragment() {
        super(R.layout.fragment_quiz_menu);
    }

    //Inflate view to ensure UI components are loaded and displayed correctly
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b) {
        masterView = inflater.inflate(R.layout.fragment_quiz_menu, vg, false);
        viewGroup = vg;
        return masterView;
    }

    //Set up UI functionality
    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        //Get reference to RecyclerView
        recycler = (RecyclerView) masterView.findViewById(R.id.quizMenuRecycler);
        /*
            Create adapter, populate with quizzes
            TODO: load from files!
            TODO: add custom quizzes!
        */
        adapter = new QuizAdapter(new Quiz[]{
                new Quiz("Capitals of the World", new QuizQuestion[]{
                        QuizQuestion.makeQuestion("What is the capital city of England?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("London", true),
                                QuizAnswer.makeAnswer("Lisbon", false),
                                QuizAnswer.makeAnswer("Manchester", false)
                        }),
                        QuizQuestion.makeQuestion("What is the capital city of Poland?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("Bydgoszcz", false),
                                QuizAnswer.makeAnswer("Warszawa", true),
                                QuizAnswer.makeAnswer("Porto", false)
                        }),
                        QuizQuestion.makeQuestion("Kentucky is the capital city of the U.S.A", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("True", false),
                                QuizAnswer.makeAnswer("False", true),
                                QuizAnswer.makeAnswer("Neither - it is a state.", false)
                        }),
                        QuizQuestion.makeQuestion("What is the capital of France?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("Germany", false),
                                QuizAnswer.makeAnswer("Paris", true)
                        })
                }),
                new Quiz("Maths (Addition)", new QuizQuestion[]{
                        QuizQuestion.makeQuestion("What is 2 + 2?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("4", true),
                                QuizAnswer.makeAnswer("6", false),
                                QuizAnswer.makeAnswer("12", false)
                        }),
                        QuizQuestion.makeQuestion("What is 12 + 98?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("100", false),
                                QuizAnswer.makeAnswer("120", false),
                                QuizAnswer.makeAnswer("110", true)
                        }),
                        QuizQuestion.makeQuestion("What is -23 + 43?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("20", true),
                                QuizAnswer.makeAnswer("10", false),
                                QuizAnswer.makeAnswer("30", false)
                        }),
                        QuizQuestion.makeQuestion("What is 100 + -100?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("0", true),
                                QuizAnswer.makeAnswer("200", false)
                        })
                }),
                new Quiz("Maths (Subtraction)", new QuizQuestion[]{
                        QuizQuestion.makeQuestion("What is 1 - 4?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("-3", true),
                                QuizAnswer.makeAnswer("5", false),
                                QuizAnswer.makeAnswer("-2", false),
                                QuizAnswer.makeAnswer("-4", false)
                        }),
                        QuizQuestion.makeQuestion("What is 10 - 26?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("-10", false),
                                QuizAnswer.makeAnswer("-16", true),
                                QuizAnswer.makeAnswer("-36", false),
                                QuizAnswer.makeAnswer("36", false)
                        }),
                        QuizQuestion.makeQuestion("What is 4 - -6?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("7", false),
                                QuizAnswer.makeAnswer("11", false),
                                QuizAnswer.makeAnswer("52", false),
                                QuizAnswer.makeAnswer("1000", false),
                                QuizAnswer.makeAnswer("10", true)
                        }),
                        QuizQuestion.makeQuestion("If I have six apples, and I eat four, how many apples do I have left?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("Two", true),
                                QuizAnswer.makeAnswer("Six", false)
                        }),
                        QuizQuestion.makeQuestion("If I have 65p, and I want to buy two bananas.\nThe closest shop sells bananas for 15p each.\nHow many pence will I have left?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("30p", false),
                                QuizAnswer.makeAnswer("35p", true)
                        })
                }),
                new Quiz("Computer Science - Data Types", new QuizQuestion[]{
                        QuizQuestion.makeQuestion("What is a boolean?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("A simple value that can only be true or false", true),
                                QuizAnswer.makeAnswer("A simple value that can only be 1 or 0", false)
                        }),
                        QuizQuestion.makeQuestion("Is the number 7 most likely an integer or a float?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("An integer", true),
                                QuizAnswer.makeAnswer("A float", false)
                        }),
                        QuizQuestion.makeQuestion("How many bits are in a byte?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("4", false),
                                QuizAnswer.makeAnswer("16", false),
                                QuizAnswer.makeAnswer("8", true),
                                QuizAnswer.makeAnswer("2", false),
                                QuizAnswer.makeAnswer("10", false),
                                QuizAnswer.makeAnswer("32", false)
                        }),
                        QuizQuestion.makeQuestion("What is a signed integer?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("A number that has been signed by a famous person", false),
                                QuizAnswer.makeAnswer("A whole number that can either be positive or negative in value", true),
                                QuizAnswer.makeAnswer("Any number", false)
                        }),
                        QuizQuestion.makeQuestion("What is an unsigned integer?", new QuizAnswer[]{
                                QuizAnswer.makeAnswer("A whole number that can only be positive in value", true),
                                QuizAnswer.makeAnswer("A number that has not been signed by a famous person yet", false),
                                QuizAnswer.makeAnswer("A value that can either be a whole number or a floating-point number", false)
                        })
                })
        });
        //Set recycler view options up
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler.setAdapter(adapter);

        //Provide wipe button functionality (delete all scores for this user only)
        wipeButton = (Button) masterView.findViewById(R.id.quizWipeAllButton);
        wipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = AppHelper.createAlertDialogBuilder(getContext(), R.string.quiz_app_menu_wipe_all_title, R.string.quiz_app_menu_wipe_all_desc)
                        .setPositiveButton(R.string.quiz_app_menu_wipe_all_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                try {
                                    //Loop through loaded quizzes
                                    for (Quiz q : adapter.quizzes) {
                                        //Load certificate, update score for current user
                                        Certificate.getCertificate(q.quizName, getActivity()).setUserScore(UserAccountControl.currentLoggedInUser, 0).save(getActivity());
                                        //Update adapter
                                        adapter.notifyDataSetChanged();
                                        //Notify user of success
                                        Snackbar.make(view, R.string.quiz_app_menu_wipe_all_success, Snackbar.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    //Notify user of error
                                    Snackbar.make(view, R.string.quiz_app_menu_wipe_all_error, Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton(R.string.delete_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                //Notify user of successful cancelation
                                Snackbar.make(view, R.string.quiz_app_menu_wipe_all_cancel, Snackbar.LENGTH_SHORT).show();
                            }
                        }).create();
                dialog.show();
            }
        });

        //Provide back button functionality
        backButton = (Button) masterView.findViewById(R.id.quizMenuBack);
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
}
