package com.example.C196;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.C196.models.Mentor;
import com.example.C196.viewModel.MentorViewAdapter;
import com.example.C196.viewModel.MentorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MentorActivity extends AppCompatActivity {

    public static final int ADD_MENTOR_REQUEST = 1;
    public static final int EDIT_MENTOR_REQUEST = 2;

    MentorViewModel mentorViewModel;

    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton buttonAddMentor = findViewById(R.id.button_add_mentor);
        buttonAddMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MentorActivity.this, AddEditMentorActivity.class);
                startActivityForResult(intent, ADD_MENTOR_REQUEST);
            }
        });

        Intent fromCourseIntent = getIntent();
        setTitle(fromCourseIntent.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE) + " Mentors");
        courseId = fromCourseIntent.getIntExtra(AddEditMentorActivity.EXTRA_COURSE_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_mentor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        MentorViewAdapter adapter = new MentorViewAdapter();
        recyclerView.setAdapter(adapter);

        mentorViewModel = new ViewModelProvider(this).get(MentorViewModel.class);
        mentorViewModel.getMentorsByCourseId(courseId).observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(List<Mentor> mentors) {
                adapter.setMentors(mentors);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mentorViewModel.delete(adapter.getMentorAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MentorActivity.this, "Mentor removed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnMentorClickListener(new MentorViewAdapter.OnMentorClickListener() {
            @Override
            public void onEditMentorClick(Mentor mentor) {
                Intent intent = new Intent(MentorActivity.this, AddEditMentorActivity.class);
                intent.putExtra(AddEditMentorActivity.EXTRA_MENTOR_ID, mentor.getId());
                intent.putExtra(AddEditMentorActivity.EXTRA_MENTOR_FIRST_NAME, mentor.getFirstName());
                intent.putExtra(AddEditMentorActivity.EXTRA_MENTOR_PHONE, mentor.getPhoneNumber());
                intent.putExtra(AddEditMentorActivity.EXTRA_MENTOR_EMAIL, mentor.getEmail());
                intent.putExtra(AddEditMentorActivity.EXTRA_COURSE_ID, mentor.getCourseId());
                startActivityForResult(intent, EDIT_MENTOR_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_MENTOR_REQUEST && resultCode == RESULT_OK) {
            String firstName = data.getStringExtra(AddEditMentorActivity.EXTRA_MENTOR_FIRST_NAME);
            String lastName = data.getStringExtra(AddEditMentorActivity.EXTRA_MENTOR_LAST_NAME);
            String phone = data.getStringExtra(AddEditMentorActivity.EXTRA_MENTOR_PHONE);
            String email = data.getStringExtra(AddEditMentorActivity.EXTRA_MENTOR_EMAIL);

            Mentor mentor = new Mentor(firstName, lastName, phone, email, courseId);
            mentorViewModel.insert(mentor);

        } else if (requestCode == EDIT_MENTOR_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditMentorActivity.EXTRA_MENTOR_ID, -1);
            if (id == -1) {
                return;
            }

            String firstName = data.getStringExtra(AddEditMentorActivity.EXTRA_MENTOR_FIRST_NAME);
            String lastName = data.getStringExtra(AddEditMentorActivity.EXTRA_MENTOR_LAST_NAME);
            String phone = data.getStringExtra(AddEditMentorActivity.EXTRA_MENTOR_PHONE);
            String email = data.getStringExtra(AddEditMentorActivity.EXTRA_MENTOR_EMAIL);

            Mentor mentor = new Mentor(firstName, lastName, phone, email, courseId);
            mentor.setId(id);
            mentorViewModel.update(mentor);

        } else {
            Toast.makeText(this, "Nothing changed", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mentor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_course_mentors:
                mentorViewModel.deleteMentorsByCourseId(courseId);
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
