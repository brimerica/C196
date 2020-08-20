package com.example.C196.models;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_table")
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Date startDate;
    private Date anticipatedEndDate;
    private CourseStatus status;
    private String notes;
    private int termId;

    @Ignore
    public Course(int id, String title, Date startDate, Date anticipatedEndDate, CourseStatus status, String notes, int termId) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.anticipatedEndDate = anticipatedEndDate;
        this.status = status;
        this.notes = notes;
        this.termId = termId;
    }

    public Course(String title, Date startDate, Date anticipatedEndDate, CourseStatus status, String notes, int termId) {
        this.title = title;
        this.startDate = startDate;
        this.anticipatedEndDate = anticipatedEndDate;
        this.status = status;
        this.notes = notes;
        this.termId = termId;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getAnticipatedEndDate() {
        return anticipatedEndDate;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }
}