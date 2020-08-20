package com.example.C196.models;

public enum AssessmentType {

    PERFORMANCE{
        @Override
        public String toString() {
            return "Performance";
        }
    },
    OBJECTIVE{
        @Override
        public String toString() {
            return "Objective";
        }
    };

}
