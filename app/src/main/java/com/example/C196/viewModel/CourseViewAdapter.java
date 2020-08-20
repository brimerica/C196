package com.example.C196.viewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.C196.R;
import com.example.C196.models.Course;
import com.example.C196.utilities.TextFormatter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.CourseViewHolder> {

    private List<Course> courses = new ArrayList<>();
    private OnCourseCLickListener listener;

    public List<Course> coursesByTermId = new ArrayList<>();

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(courseItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course currentCourse = courses.get(position);
        String formattedStartDate = TextFormatter.appFormat.format(currentCourse.getStartDate());
        String formattedEndDate = TextFormatter.appFormat.format(currentCourse.getAnticipatedEndDate());
        holder.textViewTitle.setText(currentCourse.getTitle());
        holder.textViewStartDate.setText(formattedStartDate);
        holder.textViewEndDate.setText(formattedEndDate);
        holder.textViewStatus.setText(currentCourse.getStatus().toString());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public Course getCourseAt(int position) {
        return courses.get(position);
    }

    public void setCourses(List<Course> courses){
        this.courses = courses;
        //need to update yet
        notifyDataSetChanged();;
    }

    public void setCoursesByTermId(List<Course> coursesByTermId){
        this.coursesByTermId = coursesByTermId;
        notifyDataSetChanged();
    }

    public Course getCourses(int position){
        return courses.get(position);
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewStartDate;
        private TextView textViewEndDate;
        private TextView textViewStatus;
        private ImageView imageViewEditBtn;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.course_view_title);
            this.textViewStartDate = itemView.findViewById(R.id.course_view_start_date);
            this.textViewEndDate = itemView.findViewById(R.id.course_view_end_date);
            this.textViewStatus = itemView.findViewById(R.id.course_view_status);
            this.imageViewEditBtn = itemView.findViewById(R.id.edit_course_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onCourseClick(courses.get(position));
                    }
                }
            });

            imageViewEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onEditCourseClick(courses.get(position));
                    }
                }
            });
        }
    }

    public interface OnCourseCLickListener{
        void onCourseClick(Course course);
        void onEditCourseClick(Course course);
    }

    public void setOnCourseClickListener(OnCourseCLickListener listener) {
        this.listener = listener;
    }
}
