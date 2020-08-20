package com.example.C196;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.C196.viewModel.MentorViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AddEditMentorActivity extends AppCompatActivity {

    public static final String EXTRA_COURSE_ID =
            "com.example.C196.EXTRA_COURSE_ID";
    public static final String EXTRA_MENTOR_ID =
            "com.example.C196.EXTRA_MENTOR_ID";
    public static final String EXTRA_MENTOR_FIRST_NAME =
            "com.example.C196.EXTRA_MENTOR_FIRST_NAME";
    public static final String EXTRA_MENTOR_LAST_NAME =
            "com.example.C196.EXTRA_MENTOR_LAST_NAME";
    public static final String EXTRA_MENTOR_PHONE =
            "com.example.C196.EXTRA_MENTOR_PHONE";
    public static final String EXTRA_MENTOR_EMAIL =
            "com.example.C196.EXTRA_MENTOR_EMAIL";

    private int courseId;
    private int mentorId;

    private EditText editMentorFirstName;
    private EditText editMentorLastName;
    private EditText editMentorPhoneNumber;
    private EditText editMentorEmail;

    private MentorViewModel mentorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_mentor);

        editMentorFirstName = findViewById(R.id.mentor_edit_first_name);
        editMentorLastName = findViewById(R.id.mentor_edit_last_name);
        editMentorPhoneNumber = findViewById(R.id.mentor_edit_phone);
        editMentorEmail = findViewById(R.id.mentor_edit_email);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mentorViewModel = new ViewModelProvider(this).get(MentorViewModel.class);
        mentorViewModel.mLiveMentor.observe(this, mentor -> {
            editMentorFirstName.setText(mentor.getFirstName());
            editMentorLastName.setText(mentor.getLastName());
            editMentorEmail.setText(mentor.getEmail());
            editMentorPhoneNumber.setText(mentor.getPhoneNumber());
        });

        Intent intentFromMentor = getIntent();
        if(intentFromMentor.hasExtra(EXTRA_MENTOR_ID)) {
            setTitle("Edit Mentor");
            mentorId = intentFromMentor.getIntExtra(AddEditMentorActivity.EXTRA_MENTOR_ID, -1);
            courseId = intentFromMentor.getIntExtra(AddEditMentorActivity.EXTRA_COURSE_ID, -1);
            mentorViewModel.mentorById(mentorId);
        } else {
            setTitle("Add Mentor");
            mentorId = -1;
            courseId = intentFromMentor.getIntExtra(EXTRA_COURSE_ID, -1);
        }

    }

    public void saveMentor(){
        String firstName = editMentorFirstName.getText().toString();
        String lastName = editMentorLastName.getText().toString();
        String email = editMentorEmail.getText().toString();
        String phone = editMentorPhoneNumber.getText().toString();

        if(firstName.trim().isEmpty() || lastName.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()) {
            Toast.makeText(this, "Please add Names, email, and a phone", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_MENTOR_FIRST_NAME, firstName);
        data.putExtra(EXTRA_MENTOR_LAST_NAME, lastName);
        data.putExtra(EXTRA_MENTOR_EMAIL, email);
        data.putExtra(EXTRA_MENTOR_PHONE, phone);

        int id = getIntent().getIntExtra(EXTRA_MENTOR_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_MENTOR_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_mentor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_mentor:
                saveMentor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
