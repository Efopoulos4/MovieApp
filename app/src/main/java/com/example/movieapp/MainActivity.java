package com.example.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.onEditListener {


    private String TAG = "paok";

    private String TITLE_KEY = "title of the movie";
    private String DATE_KEY = "date of the movie";
    private String DESCR_KEY = "description of the movie";
    private String IMAGE_KEY = "image key";
    private String ID_KEY = "id key";

    private MovieViewModel mMovieViewModel;
    private List<Movie> moviesList;
    private RecyclerView mRecyclerView;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        final MovieListAdapter adapter = new MovieListAdapter(this, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        mMovieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                moviesList = movies;
                adapter.setMovies(movies);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MovieCreateActivity.class);
                startActivityForResult(intent, 0);
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String title = data.getStringExtra(TITLE_KEY);
            String date = data.getStringExtra(DATE_KEY);
            String desc = data.getStringExtra(DESCR_KEY);
            String imageString = data.getStringExtra(IMAGE_KEY);
            Movie movie = new Movie(title, date, desc, imageString);
            mMovieViewModel.insert(movie);
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            String title = data.getStringExtra(TITLE_KEY);
            String date = data.getStringExtra(DATE_KEY);
            String desc = data.getStringExtra(DESCR_KEY);
            String imageString = data.getStringExtra(IMAGE_KEY);
            int id = data.getIntExtra(ID_KEY, 0);
            Movie movie = new Movie(title, date, desc, imageString);
            mMovieViewModel.update(movie, id, title, date, desc, imageString);

        } else {
            Toast.makeText(this, "You did not save any movie", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAll(View view) {
        mMovieViewModel.deleteAll();
        moviesList.clear();
    }

    @Override
    public void onEditClickListener(int position) {
        this.position = position;
        Intent intent = new Intent(this, MovieCreateActivity.class);
        intent.putExtra(TITLE_KEY, moviesList.get(position).getTitle());
        intent.putExtra(DATE_KEY, moviesList.get(position).getDate());
        intent.putExtra(DESCR_KEY, moviesList.get(position).getDescription());
        intent.putExtra(IMAGE_KEY, moviesList.get(position).getImageString());
        intent.putExtra(ID_KEY, moviesList.get(position).getId());
        startActivityForResult(intent, 1);
    }
}

