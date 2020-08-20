package com.example.C196;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.C196.models.Assessment;
import com.example.C196.models.AssessmentType;
import com.example.C196.utilities.AssessmentTypeConverter;
import com.example.C196.utilities.TextFormatter;
import com.example.C196.viewModel.AssessmentViewAdapter;
import com.example.C196.viewModel.AssessmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AssessmentActivity extends AppCompatActivity {

    public static final int ADD_ASSESSMENT_REQUEST = 1;
    public static final int EDIT_ASSESSMENT_REQUEST = 2;

    private AssessmentViewModel assessmentViewModel;

    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton buttonAddAssessment = findViewById(R.id.button_add_assessment);
        buttonAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentActivity.this, AddEditAssessmentActivity.class);
                startActivityForResult(intent, ADD_ASSESSMENT_REQUEST);
                Toast.makeText(AssessmentActivity.this, "Click to Add Assessment", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intentFromCourse = getIntent();
        setTitle(intentFromCourse.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE) + " Assessments");
        courseId = intentFromCourse.getIntExtra(AddEditAssessmentActivity.EXTRA_COURSE_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_assessment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        AssessmentViewAdapter adapter = new AssessmentViewAdapter();
        recyclerView.setAdapter(adapter);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAssessmentByCourseId(courseId).observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessments) {
                adapter.setAssessments(assessments);
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
                assessmentViewModel.delete(adapter.getAssessmentAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AssessmentActivity.this, "Assessment removed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnAssessmentClickListener(new AssessmentViewAdapter.OnAssessmentClickListener() {
            @Override
            public void onEditAssessmentClick(Assessment assessment) {
                Intent intent = new Intent(AssessmentActivity.this, AddEditAssessmentActivity.class);
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_ID, assessment.getId());
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_NAME, assessment.getName());
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE, assessment.getDueDate());
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE, assessment.getAssessmentType());
                intent.putExtra(AddEditAssessmentActivity.EXTRA_COURSE_ID, assessment.getCourseId());
                startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_ASSESSMENT_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_NAME);
            String sDueDate = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE);
            String sType = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE);

            try {
                Date dueDate = TextFormatter.dpFormat.parse(sDueDate);
                AssessmentType type = AssessmentTypeConverter.fromStringToAssessmentType(sType);

                Assessment assessment = new Assessment(title, dueDate, type, courseId);
                assessmentViewModel.insert(assessment);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if(requestCode == EDIT_ASSESSMENT_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_ID, -1);
            if(id == -1){
                Toast.makeText(this, "Assessment cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_NAME);
            String sDueDate = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE);
            String sType = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE);

            try {
                Date dueDate = TextFormatter.dpFormat.parse(sDueDate);
                AssessmentType type = AssessmentTypeConverter.fromStringToAssessmentType(sType);

                Assessment assessment = new Assessment(title, dueDate, type, courseId);
                assessment.setId(id);
                assessmentViewModel.update(assessment);
            }catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(this, "Nothing changed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.assessment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_course_assessments:
                assessmentViewModel.deleteAssessmentByCourseId(courseId);
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
