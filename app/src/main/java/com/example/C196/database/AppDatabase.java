package com.example.C196.database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.C196.utilities.AssessmentTypeConverter;
import com.example.C196.utilities.CourseStatusConverter;
import com.example.C196.models.Assessment;
import com.example.C196.models.Course;
import com.example.C196.models.Mentor;
import com.example.C196.models.Term;
import com.example.C196.utilities.DateConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Term.class, Course.class, Assessment.class, Mentor.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class, AssessmentTypeConverter.class, CourseStatusConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();
    public abstract MentorDao mentorDao();

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }






    //The following 3 methods are used to create data on start up (Term's in this case)

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
            //new PopulateCourseAsyncTask(instance).execute();
        }
    };

//    public static class PopulateCourseAsyncTask extends AsyncTask<Void, Void, Void> {
//        private CourseDao courseDao;
//
//        private PopulateCourseAsyncTask(AppDatabase db){
//            courseDao = db.courseDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            courseDao.insert(new Course("Test Course Name", toDate("01/01/2020"), toDate("02/01/2020"), CourseStatus.PLAN_TO_TAKE, "Test Note here.", 1));
//            return null;
//        }
//    }

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;

        private PopulateDbAsyncTask(AppDatabase db){
            termDao = db.termDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.insert(new Term("Term 1", toDate("01-01-2020"), toDate("02-01-2020")));
            termDao.insert(new Term("Term 2", toDate("02-01-2020"), toDate("03-01-2020")));
            termDao.insert(new Term("Term 3", toDate("03-01-2020"), toDate("04-01-2020")));
            return null;
        }
    }

    //Temporary method for testing
    public static Date toDate(String s) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date date = format.parse(s);
            return date;
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
