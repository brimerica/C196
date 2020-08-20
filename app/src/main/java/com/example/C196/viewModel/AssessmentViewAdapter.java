package com.example.C196.viewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.C196.R;
import com.example.C196.models.Assessment;
import com.example.C196.utilities.TextFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AssessmentViewAdapter extends RecyclerView.Adapter<AssessmentViewAdapter.AssessmentViewHolder> {

    private List<Assessment> assessments = new ArrayList<>();
    private OnAssessmentClickListener listener;

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View assessmentItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentViewHolder(assessmentItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        Assessment currentAssessment = assessments.get(position);
        String formattedDueDate = TextFormatter.appFormat.format(currentAssessment.getDueDate());
        holder.textViewName.setText(currentAssessment.getName());
        holder.textViewDueDate.setText(formattedDueDate);
        holder.textViewType.setText(currentAssessment.getAssessmentType().toString());
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public Assessment getAssessmentAt(int position){
        return assessments.get(position);
    }

    public void setAssessments(List<Assessment> assessments){
        this.assessments = assessments;
        notifyDataSetChanged();
    }

    public class AssessmentViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textViewDueDate;
        private TextView textViewType;
        private ImageView editAssessmentBtn;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.assessment_view_name);
            this.textViewDueDate = itemView.findViewById(R.id.assessment_view_due_date);
            this.textViewType = itemView.findViewById(R.id.assessment_view_type);
            this.editAssessmentBtn = itemView.findViewById(R.id.edit_assessment_btn);

            editAssessmentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onEditAssessmentClick(assessments.get(position));
                    }
                }
            });
        }
    }

    public interface OnAssessmentClickListener{
        void onEditAssessmentClick(Assessment assessment);
    }

    public void setOnAssessmentClickListener(OnAssessmentClickListener listener){
        this.listener = listener;
    }

}
