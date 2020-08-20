package com.example.C196.utilities;

import com.example.C196.models.AssessmentType;

import androidx.room.TypeConverter;

public class AssessmentTypeConverter {

    @TypeConverter
    public static String fromAssessmentTypeToString(AssessmentType assessmentType) {
        switch(assessmentType){
            case PERFORMANCE:
                return "Performance";
            default:
                return "Objective";
        }
    }

    @TypeConverter
    public static AssessmentType fromStringToAssessmentType(String assessmentType) {

        if(assessmentType.equals("Performance")){
            return AssessmentType.PERFORMANCE;
        } else {
            return AssessmentType.OBJECTIVE;
        }
    }

}
