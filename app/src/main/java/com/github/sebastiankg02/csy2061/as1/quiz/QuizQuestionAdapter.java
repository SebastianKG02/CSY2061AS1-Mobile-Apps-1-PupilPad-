package com.github.sebastiankg02.csy2061.as1.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061.as1.R;
import com.github.sebastiankg02.csy2061.as1.fragments.apps.QuizPlayFragment;

import java.util.ArrayList;

public class QuizQuestionAdapter extends RecyclerView.Adapter<QuizQuestionAdapter.ViewHolder> {
    public static QuizAnswer[] answers;
    public static boolean hasSelected = false;
    public static int indexSelected = 0;
    private static ArrayList<Boolean> answersSelected;

    public QuizQuestionAdapter(QuizAnswer[] data) {
        answers = new QuizAnswer[]{};
        answers = data;
        hasSelected = false;
        indexSelected = 0;

        answersSelected = new ArrayList<Boolean>();
        for (int i = 0; i < answers.length; i++) {
            answersSelected.add(false);
        }
    }

    @NonNull
    @Override
    public QuizQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.quiz_question_item, parent, false);
        ViewHolder itemHolder = new ViewHolder(item);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuizQuestionAdapter.ViewHolder holder, int position) {
        QuizAnswer currentAnswer = answers[position];

        QuizPlayFragment.updateQuestion(QuizPlayFragment.currentQuestion.question, QuizPlayFragment.currentQuestionID + 1);

        holder.button.setText(currentAnswer.content);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hasSelected = true;
                answersSelected.set(holder.getAdapterPosition(), true);
                indexSelected = holder.getAdapterPosition();

                for (int i = 0; i < answers.length; i++) {
                    if (i != indexSelected) {
                        ((ViewHolder) QuizPlayFragment.recycler.findViewHolderForAdapterPosition(i)).button.setChecked(false);
                    }
                }
            }
        });

        if (hasSelected) {
            if (holder.getAdapterPosition() == indexSelected) {
                holder.button.setChecked(true);
            } else {
                holder.button.setChecked(false);
            }
        } else {
            holder.button.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return answers.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout masterLayout;
        public RadioButton button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            masterLayout = (LinearLayout) itemView.findViewById(R.id.quizQuestionTextLayout);
            button = (RadioButton) itemView.findViewById(R.id.quizQuestionRadio);
        }
    }
}
