package com.example.C196.database;

import com.example.C196.models.Course;
import com.example.C196.models.CourseStatus;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CourseDao {

    @Insert
    void insert(Course course);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Course> courses);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("DELETE FROM course_table")
    void deleteAllCourses();

    @Query("SELECT * FROM course_table WHERE id = :id")
    Course getCourseById(int id);

    @Query("SELECT * FROM course_table ORDER BY startDate")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM course_table WHERE termId = :termId ORDER BY startDate")
    LiveData<List<Course>> getCoursesByTermId(int termId);

    @Query("SELECT COUNT(*) FROM course_table WHERE termId = :termId")
    int getCourseCountInTerm(int termId);

    @Query("DELETE FROM course_table WHERE termId = :termId")
    void deleteCoursesByTermId(int termId);

    @Query("SELECT * FROM course_table WHERE status = :status")
    LiveData<List<Course>> getCourseStatus(CourseStatus status);
}
