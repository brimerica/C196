package com.example.C196.viewModel;

import android.app.Application;

import com.example.C196.database.AppRepository;
import com.example.C196.models.Assessment;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AssessmentViewModel extends AndroidViewModel {

    Executor executor = Executors.newSingleThreadExecutor();

    public MutableLiveData<Assessment> mLiveAssessment = new MutableLiveData<>();

    private AppRepository repository;
    private LiveData<List<Assessment>> allAssessments;
    private LiveData<List<Assessment>> assessmentByCourseId;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AppRepository(application);
        this.allAssessments = repository.getAllAssessments();
    }

    public void insert(Assessment assessment){
        repository.insertAssessment(assessment);
    }

    public void update(Assessment assessment){
        repository.updateAssessment(assessment);
    }

    public void delete(Assessment assessment){
        repository.deleteAssessment(assessment);
    }

    public void deleteAllAssessments(){
        repository.deleteAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments(){
        return allAssessments;
    }

    public void assessmentById(final int assessmentId){
        executor.execute(() -> {
            Assessment assessment = repository.getAssessmentById(assessmentId);
            mLiveAssessment.postValue(assessment);
        });
    }

    public LiveData<List<Assessment>> getAssessmentByCourseId(int courseId){
        this.assessmentByCourseId = repository.getAssessmentByCourseId(courseId);
        return assessmentByCourseId;
    }

    public void deleteAssessmentByCourseId(int courseId){
        repository.deleteAssessmentByCourseId(courseId);
    }
}