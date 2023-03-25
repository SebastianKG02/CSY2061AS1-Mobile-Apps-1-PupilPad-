package com.github.sebastiankg02.csy2061.as1.fragments.apps;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.quiz.Certificate;
import com.github.sebastiankg02.csy2061.as1.quiz.Quiz;
import com.github.sebastiankg02.csy2061.as1.quiz.QuizQuestion;
import com.github.sebastiankg02.csy2061.as1.quiz.QuizQuestionAdapter;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

public class QuizPlayFragment extends Fragment {
    public static RecyclerView recycler;
    //Global current quiz object (as loaded by QuizAppFragment)
    public static Quiz currentQuiz;
    //Global current loaded question (as loaded internally)
    public static QuizQuestion currentQuestion;
    //Global current question ID
    public static int currentQuestionID;
    //Global amount of correct questions
    public static int currentQuestionAnswersCorrect = 0;
    //Persistent reference to adapter
    private static QuizQuestionAdapter adapter;
    //References to UI elements -------------------------------------
    private static TextView questionID;
    private static TextView quizQuestion;
    private View masterView;
    private ViewGroup viewGroup;
    private Button exitButton;
    private Button nextButton;

    //Default constructor
    public QuizPlayFragment() {
        super(R.layout.fragment_quiz_play);
    }

    //Global method used by QuizQuestionAdapter to update question elements within this fragment
    public static void updateQuestion(String q, int c) {
        questionID.setText("Question " + String.valueOf(c) + " out of " + String.valueOf(currentQuiz.questions.size()));
        quizQuestion.setText(q);
    }

    //Inflate view to ensure UI components are loaded and displayed correctly
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle b) {
        masterView = inflater.inflate(R.layout.fragment_quiz_play, vg, false);
        viewGroup = vg;
        return masterView;
    }

    //Set up UI functionality
    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        //Load UI element references from Fragment
        questionID = (TextView) masterView.findViewById(R.id.quizPlayID);
        quizQuestion = (TextView) masterView.findViewById(R.id.quizPlayQuestion);
        exitButton = (Button) masterView.findViewById(R.id.quizPlayBack);
        recycler = (RecyclerView) masterView.findViewById(R.id.quizPlayRecycler);
        nextButton = (Button) masterView.findViewById(R.id.quizPlayNext);

        //Set currentQuestion from quiz
        currentQuestion = currentQuiz.questions.get(currentQuestionID);
        //Create QuizQuestionAdapter and populate with question
        adapter = new QuizQuestionAdapter(currentQuestion.answers);

        //Set up recyclerView, apply adapter
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler.setAdapter(adapter);

        //Update question contents
        questionID.setText("Question " + String.valueOf(currentQuestionID + 1) + " out of " + String.valueOf(currentQuiz.questions.size()));
        quizQuestion.setText(currentQuestion.question);

        //Provide exit button functionality
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create AlertDialog
                AlertDialog dialog = AppHelper.createAlertDialogBuilder(getContext(), R.string.notes_edit_exit_dialog_title, R.string.quiz_app_play_exit_dialog_desc)
                        .setPositiveButton(R.string.notes_edit_exit_dialog_save_exit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                try {
                                    //Save current questions in certificate
                                    Certificate.getCertificate(currentQuiz.quizName, getActivity()).setUserScore(UserAccountControl.currentLoggedInUser, currentQuestionAnswersCorrect);
                                    //Inform user of success
                                    Snackbar.make(view, R.string.quiz_app_play_exit_score_saved, Snackbar.LENGTH_SHORT).show();
                                    //Exit to QuizApp
                                    AppHelper.back(getFragment());
                                } catch (JSONException e) {
                                    //Inform user of error
                                    Snackbar.make(view, R.string.quiz_app_play_exit_score_error, Snackbar.LENGTH_SHORT).show();
                                    //Exit to QuizApp
                                    AppHelper.back(getFragment());
                                }
                            }
                        }).setNegativeButton(R.string.notes_edit_exit_dialog_exit_only, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Inform user of success in not saving
                                Snackbar.make(view, R.string.quiz_app_play_exit_score_no_save, Snackbar.LENGTH_SHORT).show();
                                dialogInterface.cancel();
                                //Exit to QuizApp
                                AppHelper.back(getFragment());
                            }
                        }).create();
                dialog.show();
            }
        });

        //Provide next question button functionality
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If last question, inform user of final score, save score
                if (currentQuestionID == currentQuiz.questions.size() - 1) {
                    checkAnswer();
                    AlertDialog dialog = new AlertDialog.Builder(getContext()).setTitle(R.string.quiz_app_question_dialog_title)
                            .setMessage("You scored " + String.valueOf(currentQuestionAnswersCorrect) + " out of a total of " + String.valueOf(currentQuiz.questions.size()) + " total questions!")
                            .setPositiveButton(R.string.quiz_app_question_dialog_save_exit, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    //Save user score and go back to quiz app (load certificate)
                                    try {
                                        Certificate.getCertificate(currentQuiz.quizName, getActivity()).setUserScore(UserAccountControl.currentLoggedInUser, currentQuestionAnswersCorrect).save(getActivity());
                                        //Inform user of success
                                        Snackbar.make(view, R.string.quiz_app_play_exit_score_saved, Snackbar.LENGTH_SHORT).show();
                                        //Exit back to QuizApp
                                        AppHelper.back(getFragment());
                                    } catch (JSONException e) {
                                        //Inform user of error
                                        Snackbar.make(view, R.string.quiz_app_play_exit_score_error, Snackbar.LENGTH_SHORT).show();
                                        //Exit back to QuizApp
                                        AppHelper.back(getFragment());
                                    }
                                }
                            }).setNegativeButton(R.string.quiz_app_question_dialog_exit_only, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Inform user of score being not saved
                                    Snackbar.make(view, R.string.quiz_app_play_exit_score_no_save, Snackbar.LENGTH_SHORT).show();
                                    dialogInterface.cancel();
                                    //Exit back to QuizApp
                                    AppHelper.back(getFragment());
                                }
                            }).create();
                    dialog.show();
                } else {
                    checkAnswer();
                }
            }
        });
    }

    //Method that checks for answer correctness
    private void checkAnswer() {
        //Check if user has made selection
        if (adapter.hasSelected) {
            //Check if user has made correct choice
            if (adapter.answers[QuizQuestionAdapter.indexSelected].isCorrect) {
                //If correct, inform user of correct choice, increase score
                Snackbar.make(masterView, R.string.quiz_app_play_correct, Snackbar.LENGTH_SHORT).show();
                currentQuestionAnswersCorrect++;
            } else {
                //If not, inform user of incorrect choice
                Snackbar.make(masterView, R.string.quiz_app_play_incorrect, Snackbar.LENGTH_SHORT).show();
            }

            //If there is a next question
            if (currentQuestionID != currentQuiz.questions.size() - 1) {
                //Push next question to adapter
                currentQuestionID += 1;
                currentQuestion = currentQuiz.questions.get(currentQuestionID);
                adapter.answers = currentQuestion.answers;
                adapter.hasSelected = false;
                //Force adapter to update its' data set
                adapter.notifyDataSetChanged();
            }

        } else {
            //If not, inform they have to make selection before next question
            Snackbar.make(masterView, R.string.quiz_app_play_no_choice, Snackbar.LENGTH_SHORT).show();
        }
    }

    //Provide reference to this class
    private Fragment getFragment() {
        return this;
    }
}
