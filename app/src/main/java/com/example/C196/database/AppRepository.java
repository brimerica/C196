package com.example.C196.database;

import android.app.Application;
import android.os.AsyncTask;

import com.example.C196.models.Assessment;
import com.example.C196.models.AssessmentType;
import com.example.C196.models.Course;
import com.example.C196.models.CourseStatus;
import com.example.C196.models.Mentor;
import com.example.C196.models.Term;
import com.example.C196.utilities.GenerateData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class AppRepository {

    private Executor executor = Executors.newSingleThreadExecutor();;

    private TermDao termDao;
    private LiveData<List<Term>> allTerms;
    private Term term;

    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Course>> coursesByTermId;
    private LiveData<List<Course>> courseStatus;

    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> allAssessments;
    private LiveData<List<Assessment>> assessmentsByCourseId;
    private LiveData<List<Assessment>> assessmentType;

    private MentorDao mentorDao;
    private LiveData<List<Mentor>> allMentors;
    private LiveData<List<Mentor>> mentorsByCourseId;

    public AppRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        termDao = database.termDao();
        allTerms = termDao.getAllTerms();
        courseDao = database.courseDao();
        allCourses = courseDao.getAllCourses();
        assessmentDao = database.assessmentDao();
        allAssessments = assessmentDao.getAllAssessments();
        mentorDao = database.mentorDao();
        allMentors = mentorDao.getAllMentors();
    }

    public void setGeneratedData(){
        GenerateData.setGeneratedTerms();
        GenerateData.setGeneratedCourses();
        GenerateData.setGeneratedAssessments();
        GenerateData.setGeneratedMentors();
        executor.execute(() -> termDao.insertAll(GenerateData.getGeneratedTerms()));
        executor.execute(() -> courseDao.insertAll(GenerateData.getGeneratedCourses()));
        executor.execute(() -> assessmentDao.insertAll(GenerateData.getGeneratedAssessments()));
        executor.execute(() -> mentorDao.insertAll(GenerateData.getGeneratedMentors()));
    }

    public void deleteAllData(){
        deleteAllTerms();
        deleteAllCourses();
        deleteAllAssessments();
        deleteAllMentors();
    }

    //******************
    //Term methods start
    //******************

    public void insertTerm(Term term){
        new InsertTermAsyncTask(termDao).execute(term);
    }

    public void updateTerm(Term term){
        new UpdateTermAsyncTask(termDao).execute(term);
    }

    public void deleteTerm(Term term){
        new DeleteTermAsyncTask(termDao).execute(term);
    }

    public void deleteAllTerms(){
        new DeleteAllTermsAsyncTask(termDao).execute();
    }

    public Term getTerm(int termId){
        term = termDao.getTerm(termId);
        return term;
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }


    //******************
    //Course methods start
    //******************



    public void insertCourse(Course course){
        new InsertCourseAsyncTask(courseDao).execute(course);
    }

    public void updateCourse(Course course){
        new UpdateCourseAsyncTask(courseDao).execute(course);
    }

    public void deleteCourse(Course course){
        new DeleteCourseAsyncTask(courseDao).execute(course);
    }

    public void deleteAllCourses(){
        new DeleteAllCourseAsyncTask(courseDao).execute();
    }

    public void deleteCoursesByTermId(int termId){
        new DeleteCoursesByTermIdAsyncTask(courseDao, termId).execute();
    }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }

    public LiveData<List<Course>> getCourseStatus(CourseStatus status){
        courseStatus = courseDao.getCourseStatus(status);
        return courseStatus;
    }

    public Course getCourseById(final int courseId){
        return courseDao.getCourseById(courseId);
    }

    public LiveData<List<Course>> getCoursesByTermId(int termId){
        coursesByTermId = courseDao.getCoursesByTermId(termId);
        return coursesByTermId;
    }

    public Integer getCourseCountInTerm(int termId) throws ExecutionException, InterruptedException {
        return new CourseCountByTermAsyncTask(courseDao, termId).execute().get();
    }


    //******************
    //Assessment methods start
    //******************


    public void insertAssessment(Assessment assessment){
        new InsertAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void updateAssessment(Assessment assessment){
        new UpdateAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void deleteAssessment(Assessment assessment){
        new DeleteAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void deleteAllAssessments(){
        new DeleteAllAssessmentsAsyncTask(assessmentDao).execute();
    }

    public void deleteAssessmentByCourseId(int courseId){
        new DeleteAssessmentsByCourseIdAsyncTask(assessmentDao, courseId).execute();
    }

    public LiveData<List<Assessment>> getAllAssessments(){
        return allAssessments;
    }

    public Assessment getAssessmentById(final int assessmentId){
        return assessmentDao.getAssessmentById(assessmentId);
    }

    public LiveData<List<Assessment>> getAssessmentByCourseId(int courseId){
        assessmentsByCourseId = assessmentDao.getAssessmentsByCourseId(courseId);
        return assessmentsByCourseId;
    }

    public LiveData<List<Assessment>> getAssessmentType(AssessmentType type){
        assessmentType = assessmentDao.getAssessmentType(type);
        return assessmentType;
    }


    //******************
    //Mentor methods start
    //******************


    public void insertMentor(Mentor mentor){
        new InsertMentorAsyncTask(mentorDao).execute(mentor);
    }

    public void updateMentor(Mentor mentor){
        new UpdateMentorAsyncTask(mentorDao).execute(mentor);
    }

    public void deleteMentor(Mentor mentor){
        new DeleteMentorAsyncTask(mentorDao).execute(mentor);
    }

    public void deleteAllMentors(){
        new DeleteAllMentorAsyncTask(mentorDao).execute();
    }

    public void deleteMentorsByCourseId(int courseId){
        new DeleteMentorsByCourseIdAsyncTask(mentorDao, courseId).execute();
    }

    public LiveData<List<Mentor>> getAllMentors(){
        return allMentors;
    }

    public Mentor mentorById(int mentorId){
        return mentorDao.getMentorById(mentorId);
    }

    public LiveData<List<Mentor>> getMentorByCourseId(int courseId){
        mentorsByCourseId = mentorDao.getMentorsByCourseId(courseId);
        return mentorsByCourseId;
    }


    //******************
    //Term Async Tasks start
    //******************


    private static class InsertTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private InsertTermAsyncTask(TermDao termDao){
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.insert(terms[0]);
            return null;
        }
    }

    private static class UpdateTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private UpdateTermAsyncTask(TermDao termDao){
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.update(terms[0]);
            return null;
        }
    }

    private static class DeleteTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private DeleteTermAsyncTask(TermDao termDao){
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.delete(terms[0]);
            return null;
        }
    }

    private static class DeleteAllTermsAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;

        private DeleteAllTermsAsyncTask(TermDao termDao){
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.deleteAllTerms();
            return null;
        }
    }


    //******************
    //Course Async Tasks start
    //******************



    private static class InsertCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private InsertCourseAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.insert(courses[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private UpdateCourseAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.update(courses[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private DeleteCourseAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.delete(courses[0]);
            return null;
        }
    }

    private static class DeleteAllCourseAsyncTask extends AsyncTask<Void, Void, Void> {
        private CourseDao courseDao;

        private DeleteAllCourseAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            courseDao.deleteAllCourses();
            return null;
        }
    }

    private static class DeleteCoursesByTermIdAsyncTask extends AsyncTask<Void, Void, Void> {
        private CourseDao courseDao;
        private int termId;

        private DeleteCoursesByTermIdAsyncTask(CourseDao courseDao, int termId){
            this.courseDao = courseDao;
            this.termId = termId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            courseDao.deleteCoursesByTermId(termId);
            return null;
        }
    }

    private static class CourseCountByTermAsyncTask extends AsyncTask<Void, Void, Integer> {
        private CourseDao courseDao;
        private int termId;

        private CourseCountByTermAsyncTask(CourseDao courseDao, int termId){
            this.courseDao = courseDao;
            this.termId = termId;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return courseDao.getCourseCountInTerm(termId);
        }
    }


    //******************
    //Assessment Async Tasks start
    //******************


    private static class InsertAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private InsertAssessmentAsyncTask(AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.insert(assessments[0]);
            return null;
        }
    }

    private static class UpdateAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private UpdateAssessmentAsyncTask(AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.update(assessments[0]);
            return null;
        }
    }

    private static class DeleteAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private DeleteAssessmentAsyncTask(AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.delete(assessments[0]);
            return null;
        }
    }

    private static class DeleteAllAssessmentsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AssessmentDao assessmentDao;

        private DeleteAllAssessmentsAsyncTask(AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assessmentDao.deleteAllAssessments();
            return null;
        }
    }

    private static class DeleteAssessmentsByCourseIdAsyncTask extends AsyncTask<Void, Void, Void> {
        private AssessmentDao assessmentDao;
        private int courseId;

        private DeleteAssessmentsByCourseIdAsyncTask(AssessmentDao assessmentDao, int courseId){
            this.assessmentDao = assessmentDao;
            this.courseId = courseId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assessmentDao.deleteAssessmentByCourseId(courseId);
            return null;
        }
    }


        //************
        //Mentor Async Tasks
        //************



    private static class InsertMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private InsertMentorAsyncTask(MentorDao mentorDao){
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.insert(mentors[0]);
            return null;
        }
    }

    private static class UpdateMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private UpdateMentorAsyncTask(MentorDao mentorDao){
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.update(mentors[0]);
            return null;
        }
    }

    private static class DeleteMentorAsyncTask extends AsyncTask<Mentor, Void, Void> {
        private MentorDao mentorDao;

        private DeleteMentorAsyncTask(MentorDao mentorDao){
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Mentor... mentors) {
            mentorDao.delete(mentors[0]);
            return null;
        }
    }

    private static class DeleteAllMentorAsyncTask extends AsyncTask<Void, Void, Void> {
        private MentorDao mentorDao;

        private DeleteAllMentorAsyncTask(MentorDao mentorDao){
            this.mentorDao = mentorDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mentorDao.deleteAllMentors();
            return null;
        }
    }

    private static class DeleteMentorsByCourseIdAsyncTask extends AsyncTask<Void, Void, Void> {
        private MentorDao mentorDao;
        private int courseId;

        private DeleteMentorsByCourseIdAsyncTask(MentorDao mentorDao, int courseId){
            this.mentorDao = mentorDao;
            this.courseId = courseId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mentorDao.deleteMentorsByCourseId(courseId);
            return null;
        }
    }
}
