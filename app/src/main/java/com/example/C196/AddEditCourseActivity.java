package com.example.C196;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.C196.models.CourseStatus;
import com.example.C196.utilities.TextFormatter;
import com.example.C196.viewModel.CourseViewModel;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AddEditCourseActivity extends AppCompatActivity {
    public static final String EXTRA_COURSE_ID =
            "com.example.C196.EXTRA_COURSE_ID";
    public static final String EXTRA_TERM_ID =
            "com.example.C196.EXTRA_TERM_ID";
    public static final String EXTRA_COURSE_TITLE =
            "com.example.C196.EXTRA_COURSE_TITLE";
    public static final String EXTRA_COURSE_START_DATE =
            "com.example.C196.EXTRA_COURSE_START_DATE";
    public static final String EXTRA_COURSE_END_DATE =
            "com.example.C196.EXTRA_COURSE_END_DATE";
    public static final String EXTRA_COURSE_STATUS =
            "com.example.C196.EXTRA_COURSE_STATUS";
    public static final String EXTRA_COURSE_NOTES =
            "com.example.C196.EXTRA_COURSE_NOTES";

    public int updateDate;

    public static final int UPDATE_START_DATE = 1;
    public static final int UPDATE_END_DATE = 2;

    private final Calendar calendar = Calendar.getInstance();

    private int courseId;
    private int termId;

    private EditText editCourseTitle;
    private EditText editCourseStartDate;
    private EditText editCourseEndDate;
    private Spinner editCourseStatus;
    private EditText editCourseNotes;

    CourseViewModel courseViewModel;

    private ArrayAdapter<CourseStatus> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);

        editCourseEndDate = findViewById(R.id.add_course_end_date);
        editCourseNotes = findViewById(R.id.add_course_notes);
        editCourseStartDate = findViewById(R.id.add_course_start_date);
        editCourseStatus = findViewById(R.id.add_course_status);
        editCourseTitle = findViewById(R.id.add_course_title);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        arrayAdapter = new ArrayAdapter<CourseStatus>(this, android.R.layout.simple_spinner_item, CourseStatus.values());
        editCourseStatus.setAdapter(arrayAdapter);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        courseViewModel.mLiveCourse.observe(this, course -> {
            editCourseTitle.setText(course.getTitle());
            editCourseStartDate.setText(TextFormatter.dpFormat.format(course.getStartDate()));
            editCourseEndDate.setText(TextFormatter.dpFormat.format(course.getAnticipatedEndDate()));
            int position = arrayAdapter.getPosition(course.getStatus());
            editCourseStatus.setSelection(position);
            editCourseNotes.setText(course.getNotes());
        });

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_COURSE_ID)){
            setTitle("Edit Course");
            courseId = intent.getIntExtra(EXTRA_COURSE_ID, -1);
            termId = intent.getIntExtra(EXTRA_TERM_ID, -1);
            courseViewModel.courseById(courseId);
        } else {
            setTitle("Add Course");
            courseId = -1;
            termId = intent.getIntExtra(EXTRA_TERM_ID, -1);
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(updateDate);
            }
        };

        editCourseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEditCourseActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                updateDate = UPDATE_START_DATE;
            }
        });

        editCourseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEditCourseActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                updateDate = UPDATE_END_DATE;
            }
        });
    }

    private void updateLabel(int i){
        switch (i) {
            case UPDATE_START_DATE:
                editCourseStartDate.setText(TextFormatter.dpFormat.format(calendar.getTime()));
                break;
            case UPDATE_END_DATE:
                editCourseEndDate.setText(TextFormatter.dpFormat.format(calendar.getTime()));
                break;
            default:
                break;
        }
    }

    public void saveCourse(){
        String title = editCourseTitle.getText().toString();
        String startDate = editCourseStartDate.getText().toString();
        String endDate = editCourseEndDate.getText().toString();
        String status = editCourseStatus.getSelectedItem().toString();
        String notes = editCourseNotes.getText().toString();

        if(title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty()){
            Toast.makeText(this, "Please add Title, start and end dates, and a status", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_COURSE_TITLE, title);
        data.putExtra(EXTRA_COURSE_START_DATE, startDate);
        data.putExtra(EXTRA_COURSE_END_DATE, endDate);
        data.putExtra(EXTRA_COURSE_STATUS, status);
        data.putExtra(EXTRA_COURSE_NOTES, notes);

        int id = getIntent().getIntExtra(EXTRA_COURSE_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_COURSE_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_course:
                saveCourse();
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
