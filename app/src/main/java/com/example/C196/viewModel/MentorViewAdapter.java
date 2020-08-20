package com.example.C196.viewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.C196.R;
import com.example.C196.models.Mentor;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MentorViewAdapter extends RecyclerView.Adapter<MentorViewAdapter.MentorViewHolder> {

    private List<Mentor> mentors = new ArrayList<>();
    private OnMentorClickListener listener;

    @NonNull
    @Override
    public MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mentorItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentor_item, parent, false);
        return new MentorViewHolder(mentorItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorViewHolder holder, int position) {
        Mentor currentMentor = mentors.get(position);
        holder.textViewName.setText(currentMentor.getFirstName() + " " + currentMentor.getLastName());
        holder.textViewEmail.setText(currentMentor.getEmail());
        holder.textViewPhone.setText(currentMentor.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public void setMentors(List<Mentor> mentors){
        this.mentors = mentors;
        notifyDataSetChanged();
    }

    public Mentor getMentorAt(int position){
        return mentors.get(position);
    }


    public class MentorViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textViewPhone;
        private TextView textViewEmail;
        private ImageView editMentorBtn;

        public MentorViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.mentor_view_name);
            this.textViewPhone = itemView.findViewById(R.id.mentor_view_phone);
            this.textViewEmail = itemView.findViewById(R.id.mentor_view_email);
            this.editMentorBtn = itemView.findViewById(R.id.edit_mentor_btn);

            editMentorBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onEditMentorClick(mentors.get(position));
                    }
                }
            });
        }
    }

    public interface OnMentorClickListener {
        void onEditMentorClick(Mentor mentor);
    }

    public void setOnMentorClickListener(OnMentorClickListener listener) {
        this.listener = listener;
    }

}
