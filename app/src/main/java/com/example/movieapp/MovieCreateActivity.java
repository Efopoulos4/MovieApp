package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MovieCreateActivity extends AppCompatActivity {

    private String title ;
    private String year;
    private String description;

    private Button saveMovieButton;
    private EditText titleText;
    private EditText yearText;
    private EditText descText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_create);

        saveMovieButton = findViewById(R.id.saveMovieButton);
        titleText = findViewById(R.id.title_text);
        yearText = findViewById(R.id.year_text);
        descText = findViewById(R.id.desc_text);

        saveMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleText.getText().toString();
                year = yearText.getText().toString();
                description = descText.getText().toString();
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(year) || TextUtils.isEmpty(description)) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    replyIntent.putExtra("TITLE", title);
                    replyIntent.putExtra("YEAR", Integer.parseInt(year));
                    replyIntent.putExtra("DESC", description);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

    }
}