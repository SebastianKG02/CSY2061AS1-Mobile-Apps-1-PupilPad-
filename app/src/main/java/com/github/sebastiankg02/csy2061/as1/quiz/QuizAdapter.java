package com.github.sebastiankg02.csy2061.as1.quiz;

import android.util.Log;
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

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    public ArrayList<Quiz> quizzes;

    public QuizAdapter(Quiz[] data) {
        quizzes = new ArrayList<Quiz>();
        for (Quiz q : data) {
            quizzes.add(q);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.quiz_item, parent, false);
        ViewHolder itemHolder = new ViewHolder(item);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz currentQuiz = quizzes.get(position);

        holder.quizName.setText(currentQuiz.quizName);
        try {
            Certificate c = Certificate.getCertificate(currentQuiz.quizName, UserAccountControl.mOwner);
            Log.i("QA", String.valueOf(c.scores.get(UserAccountControl.currentLoggedInUser)));
            Log.i("QA", String.valueOf(c.getUserScore(UserAccountControl.currentLoggedInUser)));
            String t = String.valueOf(c.getUserScore(UserAccountControl.currentLoggedInUser));

            if (t.equals("-1")) {
                t = "0";
            }

            holder.quizScore.setText("Your last score: " + t + "/" + String.valueOf(currentQuiz.questions.size()));
        } catch (JSONException e) {
            holder.quizScore.setText("Could not retrieve your score.");
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizPlayFragment.currentQuiz = new Quiz(currentQuiz);
                QuizPlayFragment.currentQuestionID = 0;
                QuizPlayFragment.currentQuestionAnswersCorrect = 0;
                AppHelper.moveToFragment(QuizPlayFragment.class, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView quizScore;
        public TextView quizName;

        public ViewHolder(View v) {
            super(v);
            this.itemLayout = (LinearLayout) v.findViewById(R.id.quizQuestionTextLayout);
            this.quizName = (TextView) v.findViewById(R.id.quizItemName);
            this.quizScore = (TextView) v.findViewById(R.id.quizItemBestScore);
        }
    }
}
