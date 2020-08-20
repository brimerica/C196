package com.example.C196.database;

import com.example.C196.models.Assessment;
import com.example.C196.models.AssessmentType;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AssessmentDao {

    @Insert
    void insert(Assessment assessment);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Assessment> assessments);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("DELETE FROM assessment_table")
    void deleteAllAssessments();

    @Query("SELECT * FROM assessment_table ORDER BY dueDate DESC")
    LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE courseId = :courseId ORDER BY dueDate DESC")
    LiveData<List<Assessment>> getAssessmentsByCourseId(int courseId);

    @Query("DELETE FROM assessment_table WHERE courseId = :courseId")
    void deleteAssessmentByCourseId(int courseId);

    @Query("SELECT * FROM assessment_table WHERE id = :assessmentId")
    Assessment getAssessmentById(int assessmentId);

    @Query("SELECT * FROM assessment_table WHERE assessmentType = :type")
    LiveData<List<Assessment>> getAssessmentType(AssessmentType type);
}
