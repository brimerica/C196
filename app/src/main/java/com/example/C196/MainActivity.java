package com.example.C196;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.C196.models.Assessment;
import com.example.C196.models.Course;
import com.example.C196.models.Term;
import com.example.C196.utilities.AssessmentTypeConverter;
import com.example.C196.utilities.CourseStatusConverter;
import com.example.C196.utilities.MyReceiver;
import com.example.C196.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private TextView termTotal;
    private TextView courseTotal;
    private TextView assessmentTotal;
    private TextView courseInProgress;
    private TextView courseDropped;
    private TextView coursePlanToTake;
    private TextView courseCompleted;
    private TextView assessmentObjective;
    private TextView assessmentPerformance;
    private Button mainButton;

    private List<Term> termList = new ArrayList<>();
    private List<Course> courseList = new ArrayList<>();
    private List<Course> courseProgressList = new ArrayList<>();
    private List<Course> courseCompletedList = new ArrayList<>();
    private List<Course> courseDroppedList = new ArrayList<>();
    private List<Course> courseFutureList = new ArrayList<>();
    private List<Assessment> assessmentList = new ArrayList<>();
    private List<Assessment> assessmentPerformanceList = new ArrayList<>();
    private List<Assessment> assessmentObjectiveList = new ArrayList<>();

    public static final String EXTRA_NOTE_MESSAGE = "EXTRA_NOTE_MESSAGE";
    public static final String EXTRA_NOTE_TITLE = "EXTRA_NOTE_TITLE";
    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Dashboard");

        termTotal = findViewById(R.id.main_term_view_total);
        courseTotal = findViewById(R.id.main_course_view_total);
        assessmentTotal = findViewById(R.id.main_assessment_view_total);
        mainButton = findViewById(R.id.main_view_go_btn);
        courseInProgress = findViewById(R.id.main_courses_progress_total);
        courseCompleted = findViewById(R.id.main_courses_completed_total);
        courseDropped = findViewById(R.id.main_courses_dropped_total);
        coursePlanToTake = findViewById(R.id.main_courses_future_total);
        assessmentObjective = findViewById(R.id.main_assessment_objective_total);
        assessmentPerformance = findViewById(R.id.main_assessment_performance_total);

        loadViewModel();
        setButtons();
    }

    public void loadViewModel(){

        //initialize Main Activity Model View
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        //observe term objects and set count on textView
        Observer<List<Term>> termObserver = terms -> {
            termList.clear();
            termList.addAll(terms);
            termTotal.setText(Integer.toString(termList.size()));
        };

        //observe all course objects and set count on textView
        Observer<List<Course>> courseObserver = courses -> {
            courseList.clear();
            courseList.addAll(courses);
            checkCourseDates(courseList);
            courseTotal.setText(Integer.toString(courseList.size()));
        };

        //observe course objects with a status of In Progress and set count on textView
        Observer<List<Course>> courseInProgressObserver = courses -> {
            courseProgressList.clear();
            courseProgressList.addAll(courses);
            courseInProgress.setText(Integer.toString(courseProgressList.size()));
        };

        //observe course objects with a status of Completed and set count on textView
        Observer<List<Course>> courseCompletedObserver = courses -> {
            courseCompletedList.clear();
            courseCompletedList.addAll(courses);
            courseCompleted.setText(Integer.toString(courseCompletedList.size()));
        };

        //observe course objects with a status of Plan to Take and set count on textView
        Observer<List<Course>> courseFutureObserver = courses -> {
            courseFutureList.clear();
            courseFutureList.addAll(courses);
            coursePlanToTake.setText(Integer.toString(courseFutureList.size()));
        };

        //observe course objects with a status of Dropped and set count on textView
        Observer<List<Course>> courseDroppedObserver = courses -> {
            courseDroppedList.clear();
            courseDroppedList.addAll(courses);
            courseDropped.setText(Integer.toString(courseDroppedList.size()));
        };

        //observe assessment objects and set count on textView
        Observer<List<Assessment>> assessmentObserver = assessments -> {
            assessmentList.clear();
            assessmentList.addAll(assessments);
            checkAssessmentDates(assessmentList);
            assessmentTotal.setText(Integer.toString(assessmentList.size()));
        };

        //observe assessment objects and set count on textView
        Observer<List<Assessment>> assessmentPerformanceObserver = assessments -> {
            assessmentPerformanceList.clear();
            assessmentPerformanceList.addAll(assessments);
            assessmentPerformance.setText(Integer.toString(assessmentPerformanceList.size()));
        };

        //observe assessment objects and set count on textView
        Observer<List<Assessment>> assessmentObjectiveObserver = assessments -> {
            assessmentObjectiveList.clear();
            assessmentObjectiveList.addAll(assessments);
            assessmentObjective.setText(Integer.toString(assessmentObjectiveList.size()));
        };

        mainViewModel.getTerms().observe(this, termObserver);

        mainViewModel.getCourses().observe(this, courseObserver);
        mainViewModel.getCourseStatus(CourseStatusConverter.fromStringToCourseStatus("In Progress")).observe(this, courseInProgressObserver);
        mainViewModel.getCourseStatus(CourseStatusConverter.fromStringToCourseStatus("Completed")).observe(this, courseCompletedObserver);
        mainViewModel.getCourseStatus(CourseStatusConverter.fromStringToCourseStatus("Plan to Take")).observe(this, courseFutureObserver);
        mainViewModel.getCourseStatus(CourseStatusConverter.fromStringToCourseStatus("Dropped")).observe(this, courseDroppedObserver);

        mainViewModel.getAssessments().observe(this, assessmentObserver);
        mainViewModel.getAssessmentType(AssessmentTypeConverter.fromStringToAssessmentType("Performance")).observe(this, assessmentPerformanceObserver);
        mainViewModel.getAssessmentType(AssessmentTypeConverter.fromStringToAssessmentType("Objective")).observe(this, assessmentObjectiveObserver);
    }

    public void setButtons(){
        //set Main button
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.main_view_go_btn:
                        Intent intent = new Intent(MainActivity.this, TermActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void checkCourseDates(List<Course> currentCourses){
        String title;
        String message;

        for(Course course: currentCourses){
            if(DateUtils.isToday(course.getStartDate().getTime())){
                title = "New Course";
                message = course.getTitle() + " is starting today!";
                sendAlert(title, message);
                System.out.println("Start Date called");
            } else if(DateUtils.isToday(course.getAnticipatedEndDate().getTime())){
                title = "Course ending";
                message = course.getTitle() + " is ending today!";
                sendAlert(title, message);
                System.out.println("End Date Called");
            }
        }

    }

    public void checkAssessmentDates(List<Assessment> currentAssessments){
        String title;
        String message;

        for(Assessment assessment: currentAssessments){
            if(DateUtils.isToday(assessment.getDueDate().getTime())){
                title = "Assessment Due";
                message = assessment.getName() + " is due today!";
                sendAlert(title, message);
                System.out.println("Assessment Due called");
            }
        }
    }

    public void sendAlert(String title, String message){
        Intent intent = new Intent(MainActivity.this, MyReceiver.class);
        intent.putExtra(EXTRA_NOTE_TITLE, title);
        intent.putExtra(EXTRA_NOTE_MESSAGE, message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+500, pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_all_generated_data:
                mainViewModel.setGeneratedData();
                Toast.makeText(this, "Sample Data Generated", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete_all_generated_data:
                mainViewModel.deleteAllData();
                Toast.makeText(this, "All Data removed", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
