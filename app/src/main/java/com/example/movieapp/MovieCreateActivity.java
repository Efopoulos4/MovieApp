package com.example.movieapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.room.ColumnInfo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
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
        id = intent.getIntExtra(ID_KEY, 0);
        titleText.setText(intent.getStringExtra(TITLE_KEY));
        dateButton.setText(intent.getStringExtra(DATE_KEY));
        descText.setText(intent.getStringExtra(DESCR_KEY));
        if(intent.getStringExtra(IMAGE_KEY) != null) {
            image.setImageURI(Uri.parse(intent.getStringExtra(IMAGE_KEY)));
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog();

            }
        });

        saveMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleText.getText().toString();
                date = dateButton.getText().toString();
                description = descText.getText().toString();
                Intent replyIntent = new Intent();
                if (!(TextUtils.isEmpty(title) && TextUtils.isEmpty(date) && TextUtils.isEmpty(description) && TextUtils.isEmpty(imageString))) {
                    replyIntent.putExtra(TITLE_KEY, title);
                    replyIntent.putExtra(DATE_KEY, date);
                    replyIntent.putExtra(DESCR_KEY, description);
                    replyIntent.putExtra(IMAGE_KEY, imageString);
                    replyIntent.putExtra(ID_KEY, id);
                    setResult(RESULT_OK, replyIntent);

                } else {
                    setResult(RESULT_CANCELED, replyIntent);
                }
                finish();
            }
        });
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Upload Photo");
        myAlertDialog.setMessage("You want to choose a photo from Gallery or capture it from Camera?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_PHOTO_CODE);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, TAKE_PHOTO_CODE);

                    }
                });
        myAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            imageString = data.getData().toString();
        } catch (Exception e) {
            imageString = null;
        }
        if (resultCode == RESULT_OK) {
            Uri imageUri;
            if (requestCode == GALLERY_PHOTO_CODE) {
                Log.d(TAG, "GALLERY_PHOTO_CODE: ");
                image.setImageURI(data.getData());
            } else if (requestCode == TAKE_PHOTO_CODE) {
                Log.d(TAG, "TAKE_PHOTO_CODE: ");
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageUri = getUriFromBitmap(bitmap, MovieCreateActivity.this);
                image.setImageURI(imageUri);
                imageString = imageUri.toString();
            }
        }
    }

    private Uri getUriFromBitmap(Bitmap image, Context context) {
        File imageFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "captured_image.jpg");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(context.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
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