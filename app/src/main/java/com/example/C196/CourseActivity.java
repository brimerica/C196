package com.example.C196;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.C196.models.Course;
import com.example.C196.models.CourseStatus;
import com.example.C196.utilities.CourseStatusConverter;
import com.example.C196.utilities.TextFormatter;
import com.example.C196.viewModel.AssessmentViewModel;
import com.example.C196.viewModel.CourseViewAdapter;
import com.example.C196.viewModel.CourseViewModel;
import com.example.C196.viewModel.MentorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    public static final int ADD_COURSE_REQUEST = 1;
    public static final int EDIT_COURSE_REQUEST = 2;
    public static final int VIEW_COURSE_REQUEST = 3;

    private CourseViewModel courseViewModel;

    private int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent fromTermIntent = getIntent();
        setTitle(fromTermIntent.getStringExtra(AddEditTermActivity.EXTRA_TERM_TITLE) + " Courses");
        termId = fromTermIntent.getIntExtra(AddEditCourseActivity.EXTRA_TERM_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CourseViewAdapter adapter = new CourseViewAdapter();
        recyclerView.setAdapter(adapter);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getCoursesByTermId(termId).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                adapter.setCourses(courses);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int currentCourseId = adapter.getCourseAt(viewHolder.getAdapterPosition()).getId();
                Course currentCourse = adapter.getCourseAt(viewHolder.getAdapterPosition());
                deleteAssessmentsMentorsByCourseId(currentCourseId);
                courseViewModel.delete(currentCourse);
                Toast.makeText(CourseActivity.this, "Course removed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnCourseClickListener(new CourseViewAdapter.OnCourseCLickListener() {

            @Override
            public void onCourseClick(Course course) {
                Intent intent = new Intent(CourseActivity.this, CourseInfoActivity.class);
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_ID, course.getId()); //int
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE, course.getTitle());  //String
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_STATUS, course.getStatus()); //CourseStatus
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_NOTES, course.getNotes()); //String
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE, course.getStartDate()); //Date
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_END_DATE, course.getAnticipatedEndDate()); //Date
                intent.putExtra(AddEditCourseActivity.EXTRA_TERM_ID, course.getTermId());
                startActivityForResult(intent, VIEW_COURSE_REQUEST);
            }

            @Override
            public void onEditCourseClick(Course course) {
                Intent intent = new Intent(CourseActivity.this, AddEditCourseActivity.class);
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_ID, course.getId()); //int
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE, course.getTitle());  //String
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_STATUS, course.getStatus()); //CourseStatus
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_NOTES, course.getNotes()); //String
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE, course.getStartDate()); //Date
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_END_DATE, course.getAnticipatedEndDate()); //Date
                intent.putExtra(AddEditCourseActivity.EXTRA_TERM_ID, course.getTermId());
                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }
        });

        FloatingActionButton buttonAddCourse = findViewById(R.id.button_add_course);
        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseActivity.this, AddEditCourseActivity.class);
                intent.putExtra(AddEditCourseActivity.EXTRA_TERM_ID, -1);
                startActivityForResult(intent, ADD_COURSE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK){
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
                courseViewModel.insert(course);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if(requestCode == EDIT_COURSE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditCourseActivity.EXTRA_COURSE_ID, -1);

            if(id == -1) {
                Toast.makeText(this, "Course cannot be updated", Toast.LENGTH_SHORT).show();
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
                course.setId(id);
                courseViewModel.update(course);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Nothing changed", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAssessmentsMentorsByCourseId(int courseId){
        AssessmentViewModel assessmentViewModel;
        MentorViewModel mentorViewModel;

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        mentorViewModel = new ViewModelProvider(this).get(MentorViewModel.class);

        assessmentViewModel.deleteAssessmentByCourseId(courseId);
        mentorViewModel.deleteMentorsByCourseId(courseId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_term_courses:
                courseViewModel.deleteCoursesByTermId(termId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
