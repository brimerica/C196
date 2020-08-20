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

import com.example.C196.models.AssessmentType;
import com.example.C196.utilities.TextFormatter;
import com.example.C196.viewModel.AssessmentViewModel;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AddEditAssessmentActivity extends AppCompatActivity {
    public static final String EXTRA_COURSE_ID =
            "com.example.C196.EXTRA_COURSE_ID";
    public static final String EXTRA_ASSESSMENT_ID =
            "com.example.C196.EXTRA_ASSESSMENT_ID";
    public static final String EXTRA_ASSESSMENT_NAME =
            "com.example.C196.EXTRA_ASSESSMENT_NAME";
    public static final String EXTRA_ASSESSMENT_TYPE =
            "com.example.C196.EXTRA_ASSESSMENT_TYPE";
    public static final String EXTRA_ASSESSMENT_DUE_DATE =
            "com.example.C196.EXTRA_ASSESSMENT_DUE_DATE";

    public int updateDate;

    public static final int UPDATE_DUE_DATE = 1;

    private final Calendar calendar = Calendar.getInstance();

    private int courseId;
    private int assessmentId;

    private EditText editAssessmentName;
    private Spinner editAssessmentType;
    private EditText editAssessmentDueDate;

    private AssessmentViewModel assessmentViewModel;

    private ArrayAdapter<AssessmentType> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_assessment);

        editAssessmentName = findViewById(R.id.assessment_edit_title);
        editAssessmentType = findViewById(R.id.assessment_edit_type);
        editAssessmentDueDate = findViewById(R.id.assessment_edit_due_date);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        arrayAdapter = new ArrayAdapter<AssessmentType>(this, android.R.layout.simple_spinner_item, AssessmentType.values());
        editAssessmentType.setAdapter(arrayAdapter);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);

        assessmentViewModel.mLiveAssessment.observe(this, assessment -> {
            editAssessmentName.setText(assessment.getName());
            editAssessmentDueDate.setText(TextFormatter.dpFormat.format(assessment.getDueDate()));
            int position = arrayAdapter.getPosition(assessment.getAssessmentType());
            editAssessmentType.setSelection(position);
        });

        Intent intentFromAssessment = getIntent();
        if(intentFromAssessment.hasExtra(EXTRA_ASSESSMENT_ID)){
            setTitle("Edit Assessment");
            courseId = intentFromAssessment.getIntExtra(EXTRA_COURSE_ID, -1);
            assessmentId = intentFromAssessment.getIntExtra(EXTRA_ASSESSMENT_ID, -1);
            assessmentViewModel.assessmentById(assessmentId);
        } else {
            setTitle("Add Assessment");
            assessmentId = -1;
            courseId = intentFromAssessment.getIntExtra(EXTRA_COURSE_ID, -1);
        }

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(updateDate);
            }
        };

        editAssessmentDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEditAssessmentActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                updateDate = UPDATE_DUE_DATE;
            }
        });
    }

    private void updateLabel(int i){
        switch (i) {
            case UPDATE_DUE_DATE:
                editAssessmentDueDate.setText(TextFormatter.dpFormat.format(calendar.getTime()));
                break;
            default:
                break;
        }
    }

    public void saveAssessment(){
        String name = editAssessmentName.getText().toString();
        String dueDate = editAssessmentDueDate.getText().toString();
        String type = editAssessmentType.getSelectedItem().toString();

        if(name.trim().isEmpty() || dueDate.trim().isEmpty() || type.trim().isEmpty()){
            Toast.makeText(this, "Please add Name, due dates, and a type", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_ASSESSMENT_NAME, name);
        data.putExtra(EXTRA_ASSESSMENT_DUE_DATE, dueDate);
        data.putExtra(EXTRA_ASSESSMENT_TYPE, type);

        int id = getIntent().getIntExtra(EXTRA_ASSESSMENT_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ASSESSMENT_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_assessment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_assessment:
                saveAssessment();
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
