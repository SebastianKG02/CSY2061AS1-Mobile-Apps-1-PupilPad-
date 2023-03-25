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

/**
 * Adapter for the QuizPlayFragment RecyclerView - displays QuizAnswer objects and handles UI and user input to these QuizAnswer objects
 */
public class QuizQuestionAdapter extends RecyclerView.Adapter<QuizQuestionAdapter.ViewHolder> {
    //All answers as loaded by QuizPlayFragment
    public static QuizAnswer[] answers;
    //Global flag for radioButton appearance functionality
    public static boolean hasSelected = false;
    //Global flag for answer points awards system (this is checked against Answers by index and then verified as either a correct or incorrect response)
    public static int indexSelected = 0;

    //Default constructor, takes QuizAnswer data from QuizQuestion
    public QuizQuestionAdapter(QuizAnswer[] data) {
        //Initialise variables
        answers = new QuizAnswer[]{};
        answers = data;
        hasSelected = false;
        indexSelected = 0;
    }

    //Inflate view to ensure UI components are loaded and displayed correctly
    @NonNull
    @Override
    public QuizQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.quiz_question_item, parent, false);
        ViewHolder itemHolder = new ViewHolder(item);
        return itemHolder;
    }

    //Set up UI functionality
    @Override
    public void onBindViewHolder(@NonNull QuizQuestionAdapter.ViewHolder holder, int position) {
        //Load current answer from answers array
        QuizAnswer currentAnswer = answers[position];

        //Update QuizPlayFragment question content
        QuizPlayFragment.updateQuestion(QuizPlayFragment.currentQuestion.question, QuizPlayFragment.currentQuestionID + 1);

        //Set radio button text to be answer text
        holder.button.setText(currentAnswer.content);
        //Provide radio button functionality
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update currently selected answer
                hasSelected = true;
                indexSelected = holder.getAdapterPosition();

                //Loop through all answers and ensure ONLY currently selected index is checked
                for (int i = 0; i < answers.length; i++) {
                    if (i != indexSelected) {
                        ((ViewHolder) QuizPlayFragment.recycler.findViewHolderForAdapterPosition(i)).button.setChecked(false);
                    }
                }
            }
        });

        //Ensure that when moving from adapter data set to next data set that all answers are correctly checked
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

    //RecyclerView.Adapter mandatory method
    @Override
    public int getItemCount() {
        return answers.length;
    }

    //Simple class that binds UI to the Adapter & RecyclerView (mandatory as per implementation)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //UI element references
        public LinearLayout masterLayout;
        public RadioButton button;

        //Default constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Load references to UI elements
            masterLayout = (LinearLayout) itemView.findViewById(R.id.quizQuestionTextLayout);
            button = (RadioButton) itemView.findViewById(R.id.quizQuestionRadio);
        }
    }
}
