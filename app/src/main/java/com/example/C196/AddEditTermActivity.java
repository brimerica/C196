package com.example.C196;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.C196.models.Term;
import com.example.C196.utilities.TextFormatter;
import com.example.C196.viewModel.TermViewModel;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class AddEditTermActivity extends AppCompatActivity {
    public static final String EXTRA_TERM_ID =
            "com.example.C196.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE =
            "com.example.C196.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START_DATE =
            "com.example.C196.EXTRA_TERM_START_DATE";
    public static final String EXTRA_TERM_END_DATE =
            "com.example.C196.EXTRA_TERM_END_DATE";

    public int updateDate;

    public static final int UPDATE_START_DATE = 1;
    public static final int UPDATE_END_DATE = 2;

    private final Calendar calendar = Calendar.getInstance();

    private Term currentTerm;
    private int termId;

    private EditText editTermTitle;
    private EditText editTermStartDate;
    private EditText editTermEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_term);

        editTermTitle = findViewById(R.id.add_term_title);
        editTermStartDate = findViewById(R.id.add_start_date);
        editTermEndDate = findViewById(R.id.add_end_date);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        TermViewModel termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.mLiveTerm.observe(this, term -> {
            editTermTitle.setText(term.getTitle());
            editTermStartDate.setText(TextFormatter.dpFormat.format(term.getStartDate()));
            editTermEndDate.setText(TextFormatter.dpFormat.format(term.getEndDate()));
        });

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_TERM_ID)) {
            setTitle("Edit Term");
            termId = intent.getIntExtra(EXTRA_TERM_ID, -1);
            termViewModel.getTerm(termId);
        } else {
            setTitle("Add Term");
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(updateDate);
            }
        };

        editTermStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEditTermActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                updateDate = UPDATE_START_DATE;
            }
        });

        editTermEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEditTermActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                updateDate = UPDATE_END_DATE;
            }
        });

    }

    private void updateLabel(int i){
        switch (i) {
            case UPDATE_START_DATE:
                editTermStartDate.setText(TextFormatter.dpFormat.format(calendar.getTime()));
                break;
            case UPDATE_END_DATE:
                editTermEndDate.setText(TextFormatter.dpFormat.format(calendar.getTime()));
                break;
            default:
                break;
        }
    }

    private void saveTerm() {
        String title = editTermTitle.getText().toString();
        String startDate = editTermStartDate.getText().toString();
        String endDate = editTermEndDate.getText().toString();

        //Verify each field is filled out
        if(title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty()){
            Toast.makeText(this, "Please add Title and start and end dates", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TERM_TITLE, title);
        data.putExtra(EXTRA_TERM_START_DATE, startDate);
        data.putExtra(EXTRA_TERM_END_DATE, endDate);

        int id = getIntent().getIntExtra(EXTRA_TERM_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_TERM_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_term_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_term:
                saveTerm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
