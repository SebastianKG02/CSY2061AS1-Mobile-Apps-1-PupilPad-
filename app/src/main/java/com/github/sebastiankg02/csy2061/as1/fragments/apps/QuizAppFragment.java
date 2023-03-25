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

public class QuizAppFragment extends Fragment {
    private static QuizAdapter adapter;
    private View masterView;
    private ViewGroup viewGroup;
    private RecyclerView recycler;
    private Button wipeButton;
    private Button backButton;

    public QuizAppFragment() {
        super(R.layout.fragment_quiz_menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b) {
        masterView = inflater.inflate(R.layout.fragment_quiz_menu, vg, false);
        viewGroup = vg;
        return masterView;
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        recycler = (RecyclerView) masterView.findViewById(R.id.quizMenuRecycler);
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
                })
        });
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler.setAdapter(adapter);

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
                                    for (Quiz q : adapter.quizzes) {
                                        Certificate.getCertificate(q.quizName, getActivity()).setUserScore(UserAccountControl.currentLoggedInUser, 0).save(getActivity());
                                        adapter.notifyDataSetChanged();
                                        Snackbar.make(view, R.string.quiz_app_menu_wipe_all_success, Snackbar.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Snackbar.make(view, R.string.quiz_app_menu_wipe_all_error, Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton(R.string.delete_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Snackbar.make(view, R.string.quiz_app_menu_wipe_all_cancel, Snackbar.LENGTH_SHORT).show();
                            }
                        }).create();
                dialog.show();
            }
        });

        backButton = (Button) masterView.findViewById(R.id.quizMenuBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.back(getFragment());
            }
        });
    }

    private Fragment getFragment() {
        return this;
    }
}
