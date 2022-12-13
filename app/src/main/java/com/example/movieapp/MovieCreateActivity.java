package com.example.movieapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;

public class MovieCreateActivity extends AppCompatActivity {

    private static final String TAG = "paok";

    private String TITLE_KEY = "title of the movie";
    private String DATE_KEY = "date of the movie";
    private String DESCR_KEY = "description of the movie";
    private String IMAGE_KEY = "image key";
    private String ID_KEY = "id key";

    private int id;
    private String title;
    private String date;
    private String description;

    private Button saveMovieButton;
    private EditText titleText;
    private Button dateButton;
    private EditText descText;

    private DatePickerDialog datePickerDialog;

    private ImageView image;
    private String imageString;
    private final int GALLERY_PHOTO_CODE = 2000;
    private final int TAKE_PHOTO_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_create);

        saveMovieButton = findViewById(R.id.saveMovieButton);
        titleText = findViewById(R.id.title_text);
        dateButton = findViewById(R.id.date_button);
        descText = findViewById(R.id.desc_text);
        image = findViewById(R.id.image);

        Intent intent = getIntent();
        titleText.setText(intent.getStringExtra(TITLE_KEY));
        dateButton.setText(intent.getStringExtra(DATE_KEY));
        descText.setText(intent.getStringExtra(DESCR_KEY));
        id = intent.getIntExtra(ID_KEY, 0);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_PHOTO_CODE);

            }
        });
        saveMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d(TAG, "onClick: ");
                title = titleText.getText().toString();
                date = dateButton.getText().toString();
                description = descText.getText().toString();
                Intent replyIntent = new Intent();
                if (!(TextUtils.isEmpty(title) && TextUtils.isEmpty(date) && TextUtils.isEmpty(description) && TextUtils.isEmpty(imageString))) {
//                    Log.d(TAG, "onClick: else true ");
                    replyIntent.putExtra(TITLE_KEY, title);
                    replyIntent.putExtra(DATE_KEY, date);
                    replyIntent.putExtra(DESCR_KEY, description);
                    replyIntent.putExtra(IMAGE_KEY, imageString);
                    replyIntent.putExtra(ID_KEY, id);
                    setResult(RESULT_OK, replyIntent);

                } else {
//                    Log.d(TAG, "onClick: if true ");
                    setResult(RESULT_CANCELED, replyIntent);
                }
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            imageString = data.getData().toString();
            Log.d(TAG, "onActivityResult: ");
        } catch (Exception e) {
            imageString = null;
            Log.d(TAG, "NULL: ");
        }
        if (resultCode == RESULT_OK) {
            image.setImageURI(data.getData());

        }
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