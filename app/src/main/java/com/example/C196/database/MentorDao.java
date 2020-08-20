package com.example.C196.database;

import com.example.C196.models.Mentor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MentorDao {

    @Insert
    void insert(Mentor mentor);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Mentor> mentors);

    @Delete
    void delete(Mentor mentor);

    @Update
    void update(Mentor mentor);

    @Query("DELETE FROM mentor_table")
    void deleteAllMentors();

    @Query("SELECT * FROM mentor_table ORDER BY lastName")
    LiveData<List<Mentor>> getAllMentors();

    @Query("SELECT * FROM mentor_table WHERE courseId = :courseId ORDER BY lastName")
    LiveData<List<Mentor>> getMentorsByCourseId(int courseId);

    @Query("SELECT * FROM mentor_table WHERE id = :mentorId")
    Mentor getMentorById(int mentorId);

    @Query("DELETE FROM mentor_table WHERE courseId = :courseId")
    void deleteMentorsByCourseId(int courseId);
}
