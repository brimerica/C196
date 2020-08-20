package com.example.C196.viewModel;

import android.app.Application;

import com.example.C196.database.AppRepository;
import com.example.C196.models.Mentor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MentorViewModel extends AndroidViewModel {

    Executor executor = Executors.newSingleThreadExecutor();

    public MutableLiveData<Mentor> mLiveMentor = new MutableLiveData<>();

    private AppRepository repository;
    private LiveData<List<Mentor>> allMentors;
    private LiveData<List<Mentor>> mentorsByCourseId;


    public MentorViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AppRepository(application);
        this.allMentors = repository.getAllMentors();
    }

    public void insert(Mentor mentor){
        repository.insertMentor(mentor);
    }

    public void update(Mentor mentor){
        repository.updateMentor(mentor);
    }

    public void delete(Mentor mentor){
        repository.deleteMentor(mentor);
    }

    public void deleteAllMentors(){
        repository.deleteAllMentors();
    }

    public void mentorById(final int mentorId){
        executor.execute(() -> {
            Mentor mentor = repository.mentorById(mentorId);
            mLiveMentor.postValue(mentor);
        });
    }

    public LiveData<List<Mentor>> getMentorsByCourseId(int courseId){
        this.mentorsByCourseId = repository.getMentorByCourseId(courseId);
        return mentorsByCourseId;
    }

    public void deleteMentorsByCourseId(int courseId){
        repository.deleteMentorsByCourseId(courseId);
    }
}
