package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class MovieCreateActivity extends AppCompatActivity {

    private static final String TAG = "paok";
    private String TITLE_KEY = "title of the movie";
    private String DATE_KEY = "date of the movie";
    private String DESCR_KEY = "description of the movie";

    private String title ;
    private String date;
    private String description;

    private Button saveMovieButton;
    private EditText titleText;
    private Button dateButton;
    private EditText descText;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_create);

        saveMovieButton = findViewById(R.id.saveMovieButton);
        titleText = findViewById(R.id.title_text);
        dateButton = findViewById(R.id.date_button);
        descText = findViewById(R.id.desc_text);

        saveMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleText.getText().toString();
                date = dateButton.getText().toString();
                description = descText.getText().toString();
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(description)) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra(TITLE_KEY, title);
                    replyIntent.putExtra(DATE_KEY, date);
                    replyIntent.putExtra(DESCR_KEY, description);

                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }

    public void datePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.show();
    }
}