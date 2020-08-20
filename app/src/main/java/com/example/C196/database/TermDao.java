package com.example.C196.database;

import com.example.C196.models.Term;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TermDao {

    @Insert
    void insert(Term term);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Term> terms);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("DELETE FROM term_table")
    void deleteAllTerms();

    @Query("SELECT * FROM term_table ORDER BY startDate")
    LiveData<List<Term>> getAllTerms();

    @Query("SELECT * FROM term_table WHERE id = :id LIMIT 1")
    Term getTerm(int id);

}
