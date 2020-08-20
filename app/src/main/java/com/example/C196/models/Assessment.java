package com.example.C196.models;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessment_table")
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Date dueDate;
    private AssessmentType assessmentType;
    private int courseId;

    @Ignore
    public Assessment(int id, String name, Date dueDate, AssessmentType assessmentType, int courseId) {
        this.id = id;
        this.name = name;
        this.dueDate = dueDate;
        this.assessmentType = assessmentType;
        this.courseId = courseId;
    }

    public Assessment(String name, Date dueDate, AssessmentType assessmentType, int courseId) {
        this.name = name;
        this.dueDate = dueDate;
        this.assessmentType = assessmentType;
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }
}
