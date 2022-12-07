package com.example.movieapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.example.movieapp.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "paok";

    private String TITLE_KEY = "title of the movie";
    private String YEAR_KEY = "year of the movie";
    private String DESCR_KEY = "description of the movie";

    private ActivityMainBinding binding;
    private MovieViewModel mMovieViewModel;
    private List<Movie> moviesList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        final MovieListAdapter adapter = new MovieListAdapter(this);
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
            int year = data.getIntExtra(YEAR_KEY, 0);
            String desc = data.getStringExtra(DESCR_KEY);
            Movie movie = new Movie(title, year, desc);
            mMovieViewModel.insert(movie);
        } else {
            Toast.makeText(this, "You did not save any movie", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAll(View view) {
        mMovieViewModel.deleteAll();
        moviesList.clear();
    }

}

