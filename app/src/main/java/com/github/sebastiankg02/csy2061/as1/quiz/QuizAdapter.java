package com.github.sebastiankg02.csy2061.as1.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.QuizPlayFragment;
import com.github.sebastiankg02.csy2061.as1.user.UserAccountControl;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Adapter for the QuizAppFragment RecyclerView - displays Quiz objects and handles UI and user input to these Quiz objects
 */
public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    //Actual quizzes stored within the adapter
    public ArrayList<Quiz> quizzes;

    //Default constructor - takes an array of Quiz objects to generate view
    public QuizAdapter(Quiz[] data) {
        //Initialise local quiz storage
        quizzes = new ArrayList<Quiz>();
        //Loop through data and copy to local quiz storage variable
        for (Quiz q : data) {
            quizzes.add(q);
        }
    }

    //Inflate view to ensure UI components are loaded and displayed correctly
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.quiz_item, parent, false);
        ViewHolder itemHolder = new ViewHolder(item);
        return itemHolder;
    }

    //Set up UI functionality
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Load current quiz
        Quiz currentQuiz = quizzes.get(position);

        //Set display name of this quiz to match internal storage
        holder.quizName.setText(currentQuiz.quizName);
        try {
            //Load user score from certificate on file
            Certificate c = Certificate.getCertificate(currentQuiz.quizName, UserAccountControl.mOwner);
            //Convert loaded value from certificate into string
            String t = String.valueOf(c.getUserScore(UserAccountControl.currentLoggedInUser));

            //As certificate will return -1 for score if user has never completed quiz, adjust text to reflect this (set to 0)
            if (t.equals("-1")) {
                t = "0";
            }

            //Update UI text
            holder.quizScore.setText("Your last score: " + t + "/" + String.valueOf(currentQuiz.questions.size()));
        } catch (JSONException e) {
            //Inform user that there was an error retrieving their score
            holder.quizScore.setText("Could not retrieve your score.");
        }

        //When user taps on this adapter's elements, take them to the QuizPlay Fragment for this quiz
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Load quiz data into QuizPlayFragment
                QuizPlayFragment.currentQuiz = new Quiz(currentQuiz);
                QuizPlayFragment.currentQuestionID = 0;
                QuizPlayFragment.currentQuestionAnswersCorrect = 0;
                //Physically move to the QuizPlayFragment
                AppHelper.moveToFragment(QuizPlayFragment.class, null);
            }
        });
    }

    //Required as part of RecyclerView.Adapter implementation
    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    //Simple class that binds UI to the Adapter & RecyclerView (mandatory as per implementation)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //UI element references
        public LinearLayout itemLayout;
        public TextView quizScore;
        public TextView quizName;

        //Default constructor
        public ViewHolder(View v) {
            super(v);
            //Load UI element references
            this.itemLayout = (LinearLayout) v.findViewById(R.id.quizQuestionTextLayout);
            this.quizName = (TextView) v.findViewById(R.id.quizItemName);
            this.quizScore = (TextView) v.findViewById(R.id.quizItemBestScore);
        }
    }
}
