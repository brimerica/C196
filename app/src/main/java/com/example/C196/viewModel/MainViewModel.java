package com.example.C196.viewModel;

import android.app.Application;

import com.example.C196.database.AppRepository;
import com.example.C196.models.Assessment;
import com.example.C196.models.AssessmentType;
import com.example.C196.models.Course;
import com.example.C196.models.CourseStatus;
import com.example.C196.models.Mentor;
import com.example.C196.models.Term;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<Term>> terms;
    private LiveData<List<Course>> courses;
    private LiveData<List<Course>> courseStatus;
    private LiveData<List<Assessment>> assessments;
    private LiveData<List<Assessment>> assessmentType;
    private LiveData<List<Mentor>> mentors;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        terms = repository.getAllTerms();
        courses = repository.getAllCourses();
        assessments = repository.getAllAssessments();
        mentors = repository.getAllMentors();
    }

    public LiveData<List<Term>> getTerms(){
        return terms;
    }

    public LiveData<List<Course>> getCourseStatus(CourseStatus status){
        courseStatus = repository.getCourseStatus(status);
        return courseStatus;
    }

    public LiveData<List<Course>> getCourses(){
        return courses;
    }

    public LiveData<List<Assessment>> getAssessments(){
        return assessments;
    }

    public LiveData<List<Assessment>> getAssessmentType(AssessmentType type){
        assessmentType = repository.getAssessmentType(type);
        return assessmentType;
    }

    public LiveData<List<Mentor>> getMentors() {
        return mentors;
    }

    public void setGeneratedData(){
        repository.setGeneratedData();
    }

    public void deleteAllData(){
        repository.deleteAllData();
    }

}
