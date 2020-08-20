package com.example.C196.viewModel;

import android.app.Application;

import com.example.C196.database.AppRepository;
import com.example.C196.models.Course;
import com.example.C196.models.Term;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CourseViewModel extends AndroidViewModel {

    Executor executor = Executors.newSingleThreadExecutor();

    public MutableLiveData<Course> mLiveCourse = new MutableLiveData<>();
    public MutableLiveData<Term> mLiveTerm = new MutableLiveData<>();

    public int coursesInTerm;

    private AppRepository repository;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Course>> coursesByTermId;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AppRepository(application);
        this.allCourses = repository.getAllCourses();
    }

    public void insert(Course course) {
        repository.insertCourse(course);
    }

    public void update(Course course){
        repository.updateCourse(course);
    }

    public void delete(Course course){
        repository.deleteCourse(course);
    }

    public void deleteAllCourses(){
        repository.deleteAllCourses();
    }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }

    public void courseById(final int courseId) {
        executor.execute(() -> {
            Course course = repository.getCourseById(courseId);
            mLiveCourse.postValue(course);
        });
    }

    public LiveData<List<Course>> getCoursesByTermId(int termId){
        this.coursesByTermId = repository.getCoursesByTermId(termId);
        return coursesByTermId;
    }


    public int getCourseCountInTerm(int termId) throws ExecutionException, InterruptedException {
        this.coursesInTerm = repository.getCourseCountInTerm(termId);
        return coursesInTerm;
    }

    public void getTerm(int termId){
        executor.execute(() -> {
            Term term = repository.getTerm(termId);
            mLiveTerm.postValue(term);
        });
    }

    public void deleteCoursesByTermId(int termId){
        repository.deleteCoursesByTermId(termId);
    }
}
