package com.example.C196;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.C196.models.Course;
import com.example.C196.models.Term;
import com.example.C196.utilities.TextFormatter;
import com.example.C196.viewModel.CourseViewModel;
import com.example.C196.viewModel.TermViewAdapter;
import com.example.C196.viewModel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TermActivity extends AppCompatActivity {
    public static final int ADD_TERM_REQUEST = 1;
    public static final int EDIT_TERM_REQUEST = 2;

    private List<Course> courseData = new ArrayList<>();

    private TermViewModel termViewModel;

    private int numOfCoursesInTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_term);
        setTitle("Term Listing");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton buttonAddTerm = findViewById(R.id.button_add_term);
        buttonAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermActivity.this, AddEditTermActivity.class);
                startActivityForResult(intent, ADD_TERM_REQUEST);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view_term);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TermViewAdapter adapter = new TermViewAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                adapter.setTerms(terms);
                //adapter.submitList(terms);
            }
        });

        //Method to delete a term by swiping left or right on that card
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Term deleteTerm = adapter.getTermAt(viewHolder.getAdapterPosition());
                try {
                    numOfCoursesInTerm = coursesInTerm(deleteTerm.getId());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(numOfCoursesInTerm <= 0){
                    termViewModel.delete(deleteTerm);
                    //adapter.notifyDataSetChanged();
                    Toast.makeText(TermActivity.this, "Term removed", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(TermActivity.this, "There are courses associated with this Term. Cannot Remove.", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        //Method to see listed courses in clicked term
        adapter.setOnTermClickListener(new TermViewAdapter.OnTermClickListener() {
            @Override
            public void onTermClick(Term term) {
                Intent intent = new Intent(TermActivity.this, CourseActivity.class);
                intent.putExtra(AddEditTermActivity.EXTRA_TERM_ID, term.getId()); //int
                intent.putExtra(AddEditTermActivity.EXTRA_TERM_TITLE, term.getTitle());  //String
                intent.putExtra(AddEditTermActivity.EXTRA_TERM_START_DATE, term.getStartDate());  //Date
                intent.putExtra(AddEditTermActivity.EXTRA_TERM_END_DATE, term.getEndDate());  //Date
                startActivityForResult(intent, ADD_TERM_REQUEST);
            }

            @Override
            public void onEditTermClick(Term term) {
                Intent intent = new Intent(TermActivity.this, AddEditTermActivity.class);
                intent.putExtra(AddEditTermActivity.EXTRA_TERM_ID, term.getId()); //int
                intent.putExtra(AddEditTermActivity.EXTRA_TERM_TITLE, term.getTitle());  //String
                intent.putExtra(AddEditTermActivity.EXTRA_TERM_START_DATE, term.getStartDate());  //Date
                intent.putExtra(AddEditTermActivity.EXTRA_TERM_END_DATE, term.getEndDate());  //Date
                startActivityForResult(intent, EDIT_TERM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddEditTermActivity.EXTRA_TERM_TITLE);
            String sStartDate = data.getStringExtra(AddEditTermActivity.EXTRA_TERM_START_DATE);
            String sEndDate = data.getStringExtra(AddEditTermActivity.EXTRA_TERM_END_DATE);
            try {
                Date startDate = TextFormatter.dpFormat.parse(sStartDate);
                Date endDate = TextFormatter.dpFormat.parse(sEndDate);

                Term term = new Term(title, startDate, endDate);
                termViewModel.insert(term);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if(requestCode == EDIT_TERM_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditTermActivity.EXTRA_TERM_ID, -1);

            if(id == -1) {
                Toast.makeText(this, "Term cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            setTitle("Course list for " + data.getStringExtra(AddEditTermActivity.EXTRA_TERM_TITLE));
            String title = data.getStringExtra(AddEditTermActivity.EXTRA_TERM_TITLE);
            String sStartDate = data.getStringExtra(AddEditTermActivity.EXTRA_TERM_START_DATE);
            String sEndDate = data.getStringExtra(AddEditTermActivity.EXTRA_TERM_END_DATE);
            try {
                Date startDate = TextFormatter.dpFormat.parse(sStartDate);
                Date endDate = TextFormatter.dpFormat.parse(sEndDate);

                Term term = new Term(title, startDate, endDate);
                term.setId(id);
                termViewModel.update(term);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Nothing done", Toast.LENGTH_SHORT).show();
        }
    }

    public int coursesInTerm(int termId) throws ExecutionException, InterruptedException {
        CourseViewModel courseViewModel;
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        return courseViewModel.getCourseCountInTerm(termId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.term_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_terms:
                termViewModel.deleteAllTerms();
                Toast.makeText(this, "All Terms Removed", Toast.LENGTH_SHORT).show();
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
