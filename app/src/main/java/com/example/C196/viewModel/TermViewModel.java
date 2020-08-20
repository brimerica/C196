package com.example.C196.viewModel;

import android.app.Application;

import com.example.C196.database.AppRepository;
import com.example.C196.models.Term;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TermViewModel extends AndroidViewModel {

    Executor executor = Executors.newSingleThreadExecutor();

    public MutableLiveData<Term> mLiveTerm = new MutableLiveData<>();

    private AppRepository repository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AppRepository(application);
        this.allTerms = repository.getAllTerms();
    }

    public void insert(Term term){
        repository.insertTerm(term);
    }

    public void update(Term term){
        repository.updateTerm(term);
    }

    public void delete(Term term){
        repository.deleteTerm(term);
    }

    public void deleteAllTerms(){
        repository.deleteAllTerms();
    }

    public LiveData<List<Term>> getAllTerms(){
        return allTerms;
    }

    public void getTerm(int termId){
        executor.execute(() -> {
            Term term = repository.getTerm(termId);
            mLiveTerm.postValue(term);
        });
    }

}