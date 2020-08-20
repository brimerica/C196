package com.example.C196;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.C196.models.Course;
import com.example.C196.models.CourseStatus;
import com.example.C196.utilities.CourseStatusConverter;
import com.example.C196.utilities.TextFormatter;
import com.example.C196.viewModel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class CourseInfoActivity extends AppCompatActivity {

    public static final int ASSESSMENT_BUTTON_REQUEST = 1;
    public static final int MENTOR_BUTTON_REQUEST = 2;


    private TextView tvCourseTitle;
    private TextView tvCourseStatus;
    private TextView tvCourseDates;
    private TextView tvCourseNote;
    private Button btnAssessments;
    private Button btnMentors;
    private FloatingActionButton btnShareNote;

    public int courseId;
    public int termId;

    public Course currentCourse;

    CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvCourseTitle = findViewById(R.id.course_info_title);
        tvCourseStatus = findViewById(R.id.course_info_status);
        tvCourseDates = findViewById(R.id.course_info_date);
        tvCourseNote = findViewById(R.id.course_info_notes);
        btnAssessments = findViewById(R.id.course_info_assessment_btn);
        btnMentors = findViewById(R.id.course_info_mentor_btn);
        btnShareNote = findViewById(R.id.course_note_share_btn);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.mLiveCourse.observe(this, course -> {
            tvCourseTitle.setText(course.getTitle());
            tvCourseStatus.setText(course.getStatus().toString());
            tvCourseDates.setText(TextFormatter.appFormat.format(course.getStartDate()) + " - " + TextFormatter.appFormat.format(course.getAnticipatedEndDate()));
            tvCourseNote.setText(course.getNotes());
            currentCourse = course;
        });

        Intent intentFromCourse = getIntent();
        courseId = intentFromCourse.getIntExtra(AddEditCourseActivity.EXTRA_COURSE_ID, -1);
        termId = intentFromCourse.getIntExtra(AddEditCourseActivity.EXTRA_TERM_ID, -1);
        courseViewModel.courseById(courseId);
        setTitle("Course Details");

        btnShareNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.course_note_share_btn:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, currentCourse.getNotes());
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "My notes for " + currentCourse.getTitle());
                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                    default:
                        break;
                }
            }
        });

        btnAssessments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.course_info_assessment_btn:
                        Intent courseIntent = new Intent(CourseInfoActivity.this, AssessmentActivity.class);
                        courseIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_ID, courseId);
                        courseIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE, currentCourse.getTitle());
                        courseIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE, currentCourse.getStartDate());
                        courseIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_END_DATE, currentCourse.getAnticipatedEndDate());
                        courseIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_NOTES, currentCourse.getNotes());
                        courseIntent.putExtra(AddEditCourseActivity.EXTRA_TERM_ID, currentCourse.getTermId());
                        startActivityForResult(courseIntent, ASSESSMENT_BUTTON_REQUEST);
                        break;
                    default:
                        break;
                }
            }
        });

        btnMentors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.course_info_mentor_btn:
                        Intent mentorIntent = new Intent(CourseInfoActivity.this, MentorActivity.class);
                        mentorIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_ID, courseId);
                        mentorIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE, currentCourse.getTitle());
                        mentorIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE, currentCourse.getStartDate());
                        mentorIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_END_DATE, currentCourse.getAnticipatedEndDate());
                        mentorIntent.putExtra(AddEditCourseActivity.EXTRA_COURSE_NOTES, currentCourse.getNotes());
                        mentorIntent.putExtra(AddEditCourseActivity.EXTRA_TERM_ID, currentCourse.getTermId());
                        startActivityForResult(mentorIntent, MENTOR_BUTTON_REQUEST);
                    default:
                        break;
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ASSESSMENT_BUTTON_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditCourseActivity.EXTRA_COURSE_ID, -1);

            if(id == -1) {
                return;
            }
            String title = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE);
            String sStartDate = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE);
            String sEndDate = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_END_DATE);
            String sStatus = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_STATUS);
            String note = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_NOTES);

            try {
                Date startDate = TextFormatter.dpFormat.parse(sStartDate);
                Date endDate = TextFormatter.dpFormat.parse(sEndDate);
                CourseStatus status = CourseStatusConverter.fromStringToCourseStatus(sStatus);

                Course course = new Course(title, startDate, endDate, status, note, termId);
                courseViewModel.update(course);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if(requestCode == MENTOR_BUTTON_REQUEST && resultCode == RESULT_OK){

            int id = data.getIntExtra(AddEditCourseActivity.EXTRA_COURSE_ID, -1);

            if(id == -1) {
                return;
            }
            String title = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE);
            String sStartDate = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE);
            String sEndDate = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_END_DATE);
            String sStatus = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_STATUS);
            String note = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_NOTES);

            try {
                Date startDate = TextFormatter.dpFormat.parse(sStartDate);
                Date endDate = TextFormatter.dpFormat.parse(sEndDate);
                CourseStatus status = CourseStatusConverter.fromStringToCourseStatus(sStatus);

                Course course = new Course(title, startDate, endDate, status, note, termId);
                courseViewModel.update(course);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if(resultCode == RESULT_CANCELED) {
            return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
