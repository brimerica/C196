package com.example.C196.utilities;

import com.example.C196.models.Assessment;
import com.example.C196.models.AssessmentType;
import com.example.C196.models.Course;
import com.example.C196.models.CourseStatus;
import com.example.C196.models.Mentor;
import com.example.C196.models.Term;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class GenerateData {

    public static final String TERM_TITLE = "Term Title";
    public static final String COURSE_TITLE = "Course Title";
    public static final String ASSESSMENT_TITLE = "Assessment Title";

    private static List<Term> generatedTerms = new ArrayList<>();
    private static List<Course> generatedCourses = new ArrayList<>();
    private static List<Assessment> generatedAssessments = new ArrayList<>();
    private static List<Mentor> generatedMentors = new ArrayList<>();

    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, diff);
        return cal.getTime();
    }

    //Generated Terms
    public static void setGeneratedTerms(){
        generatedTerms.add(new Term(100,TERM_TITLE + " 1", getDate(0), getDate(181)));
        generatedTerms.add(new Term(101,TERM_TITLE + " 2", getDate(182), getDate(365)));
        generatedTerms.add(new Term(102,TERM_TITLE + " 3", getDate(366), getDate(540)));
    }

    public static List<Term> getGeneratedTerms(){
        return generatedTerms;
    }

    //Generated Courses
    public static void setGeneratedCourses(){
        generatedCourses.add(new Course(100,COURSE_TITLE + " 1", getDate(0), getDate(30), CourseStatus.PLAN_TO_TAKE, "Generated Note 1", 100));
        generatedCourses.add(new Course(101, COURSE_TITLE + " 2", getDate(31), getDate(60), CourseStatus.IN_PROGRESS, "Generated Note 2", 100));
        generatedCourses.add(new Course(102, COURSE_TITLE + " 3", getDate(61), getDate(90), CourseStatus.COMPLETED, "Generated Note 3", 100));

        generatedCourses.add(new Course(103,COURSE_TITLE + " 1", getDate(182), getDate(212), CourseStatus.PLAN_TO_TAKE, "Generated Note 1", 101));
        generatedCourses.add(new Course(104, COURSE_TITLE + " 2", getDate(213), getDate(243), CourseStatus.IN_PROGRESS, "Generated Note 2", 101));
        generatedCourses.add(new Course(105, COURSE_TITLE + " 3", getDate(244), getDate(274), CourseStatus.DROPPED, "Generated Note 3", 101));

        generatedCourses.add(new Course(106,COURSE_TITLE + " 1", getDate(366), getDate(396), CourseStatus.DROPPED, "Generated Note 1", 102));
        generatedCourses.add(new Course(107, COURSE_TITLE + " 2", getDate(400), getDate(430), CourseStatus.IN_PROGRESS, "Generated Note 2", 102));
        generatedCourses.add(new Course(108, COURSE_TITLE + " 3", getDate(431), getDate(460), CourseStatus.COMPLETED, "Generated Note 3", 102));
    }

    public static List<Course> getGeneratedCourses(){
        return generatedCourses;
    }

    //Generated Assessment
    public static void setGeneratedAssessments(){
        generatedAssessments.add(new Assessment(100, ASSESSMENT_TITLE + " 1", getDate(15), AssessmentType.OBJECTIVE, 100));
        generatedAssessments.add(new Assessment(101, ASSESSMENT_TITLE + " 2", getDate(25), AssessmentType.PERFORMANCE, 100));

        generatedAssessments.add(new Assessment(102, ASSESSMENT_TITLE + " 1", getDate(40), AssessmentType.OBJECTIVE, 101));
        generatedAssessments.add(new Assessment(103, ASSESSMENT_TITLE + " 2", getDate(60), AssessmentType.PERFORMANCE, 101));

        generatedAssessments.add(new Assessment(104, ASSESSMENT_TITLE + " 1", getDate(68), AssessmentType.OBJECTIVE, 102));
        generatedAssessments.add(new Assessment(105, ASSESSMENT_TITLE + " 2", getDate(85), AssessmentType.PERFORMANCE, 102));

        generatedAssessments.add(new Assessment(106, ASSESSMENT_TITLE + " 1", getDate(200), AssessmentType.OBJECTIVE, 103));

        generatedAssessments.add(new Assessment(107, ASSESSMENT_TITLE + " 1", getDate(240), AssessmentType.OBJECTIVE, 104));

        generatedAssessments.add(new Assessment(108, ASSESSMENT_TITLE + " 1", getDate(270), AssessmentType.PERFORMANCE, 105));
        generatedAssessments.add(new Assessment(109, ASSESSMENT_TITLE + " 1", getDate(274), AssessmentType.OBJECTIVE, 105));

        generatedAssessments.add(new Assessment(110, ASSESSMENT_TITLE + " 1", getDate(390), AssessmentType.OBJECTIVE, 106));

        generatedAssessments.add(new Assessment(111, ASSESSMENT_TITLE + " 1", getDate(415), AssessmentType.OBJECTIVE, 107));

        generatedAssessments.add(new Assessment(112, ASSESSMENT_TITLE + " 1", getDate(450), AssessmentType.PERFORMANCE, 108));

    }
    public static List<Assessment> getGeneratedAssessments(){
        return generatedAssessments;
    }

    //Mentors
    public static void setGeneratedMentors(){
        generatedMentors.add(new Mentor(100, "Sherlock", "Holmes", "456-789-1230", "sherlock@bakerst.com", 100));
        generatedMentors.add(new Mentor(101, "Tom", "Ripley", "635-159-4875", "tom@fictional.com", 101));
        generatedMentors.add(new Mentor(102, "James", "Bond", "895-487-1542", "007@bond.com", 102));
        generatedMentors.add(new Mentor(103, "Bob", "Cratchit", "635-159-4875", "bob@.scroogefinacial.com", 103));
        generatedMentors.add(new Mentor(104, "Mary", "Poppins", "459-225-154", "mary@fictional.com", 104));
        generatedMentors.add(new Mentor(105, "Harry", "Potter", "124-556-2258", "harry@hogwarts.com", 105));
        generatedMentors.add(new Mentor(106, "Chris", "Robin", "885-669-4578", "chris@100acrewood.com", 106));
        generatedMentors.add(new Mentor(107, "Calvin", "Hobbes", "784-591-2630", "calvin@comics.com", 107));
        generatedMentors.add(new Mentor(108, "Han", "Solo", "559-989-1212", "han@solo.com", 108));
    }

    public static List<Mentor> getGeneratedMentors(){
        return generatedMentors;
    }

}
