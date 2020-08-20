package com.example.C196.utilities;

import com.example.C196.models.CourseStatus;

import androidx.room.TypeConverter;

public class CourseStatusConverter {

    @TypeConverter
    public static String fromCourseStatusToString(CourseStatus courseStatus) {
        switch(courseStatus){
            case DROPPED:
                return "Dropped";
            case COMPLETED:
                return "Completed";
            case PLAN_TO_TAKE:
                return "Plan to Take";
            default:
                return "In Progress";
        }
    }

    @TypeConverter
    public static CourseStatus fromStringToCourseStatus(String courseStatus) {
        switch(courseStatus){
            case "Dropped":
                return CourseStatus.DROPPED;
            case "Completed":
                return CourseStatus.COMPLETED;
            case "Plan to Take":
                return CourseStatus.PLAN_TO_TAKE;
            default:
                return CourseStatus.IN_PROGRESS;
        }
    }

}
