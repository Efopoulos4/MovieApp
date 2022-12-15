package com.example.movieapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.onEditListener {

    private String TITLE_KEY = "title of the movie";
    private String DATE_KEY = "date of the movie";
    private String DESCR_KEY = "description of the movie";
    private String IMAGE_KEY = "image key";
    private String ID_KEY = "id key";
    private String IS_FROM_GALLERY_ID = "is from gallery ?";

    private MovieViewModel movieViewModel;
    private List<Movie> moviesList;
    private RecyclerView recyclerView;
    private MovieListAdapter adapter;
    private boolean isLoading = false;
    private Button randButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MovieListAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                moviesList = movies;
                adapter.setMovies(movies);
            }
        });

        //fab starts the insert intent (requestCode = 0)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MovieCreateActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        //rand button just adds one new movie with default values
        randButton = findViewById(R.id.button_add_random);
        randButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "randTitle";
                String date = "30/5/2000";
                String desc = "randDesc";
                String imageString = "content://com.example.movieapp.provider/external_files/images/captured_image.jpg";
                Movie movie = new Movie(title, date, desc, imageString);
                movieViewModel.insert(movie);
            }
        });

        //Every time we scroll till the end of the recycler view we show a loading bar
        //and add 10 more Movies in the dataBase
        //This action is activated only if the screen is filled with cardViews in this case we need at least 5 movies (moviesList.size() > 4)
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == moviesList.size() - 1 && moviesList.size() > 4) {
                        getMoreData(10);
                        isLoading = true;
                    }
                }
            }
        });
    }

    /**
     * We add 10 more movies in the dataBase (which updates automatically the recycler view due to LiveData)
     * And we delay 2 seconds in order to appear the loading bar
     * @param num is the number of the movies we want to add
     */
    private void getMoreData(int num) {
        moviesList.add(null);
        recyclerView.post(new Runnable() {
            public void run() {
                adapter.notifyItemInserted(moviesList.size() - 1);
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (moviesList.size() > 0)
                    moviesList.remove(moviesList.size() - 1);
                for (int i = 0; i < num; i++) {
                    String title = "TITLE";
                    String date = "0/0/0";
                    String desc = "DESCRIPTION";
                    String imageString = "content://com.example.movieapp.provider/external_files/images/captured_image.jpg";
                    Movie movie = new Movie(title, date, desc, imageString);
                    movieViewModel.insert(movie);
                }
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    /**
     * onActivityResult receives the data we entered (title, date, description, image, id)
     * We make a new Movie object in order to insert a new one in the database
     * Or we update an existing object from the database
     *
     * @param requestCode checks if we insert or edited a movie
     * @param resultCode  checks if there is any empty field, only if there are no empty fields returns OK
     * @param data        we pass the title, date, description and image. The ID only if we want to edit a movie.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String title = data.getStringExtra(TITLE_KEY);
            String date = data.getStringExtra(DATE_KEY);
            String desc = data.getStringExtra(DESCR_KEY);
            String imageString = data.getStringExtra(IMAGE_KEY);
            int id = data.getIntExtra(ID_KEY, 0);
            Movie movie = new Movie(title, date, desc, imageString);
            if (requestCode == 0) { //The requestCode == 0 comes when we insert a Movie
                movieViewModel.insert(movie);
            } else if (requestCode == 1) { //The requestCode == 1 comes when we edit a Movie
                //Permission check
//                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) && (data.getBooleanExtra(IS_FROM_GALLERY_ID, true))) {
//                    getApplicationContext().getContentResolver().takePersistableUriPermission(
//                            Uri.parse(data.getStringExtra(IMAGE_KEY)),
//                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                }
                movieViewModel.update(movie, id, title, date, desc, imageString);
            }
        } else {
            Toast.makeText(this, "You did not save any movie", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * We enable the reset button only if there is no loading process running
     * @param view
     */
    public void deleteAll(View view) {
        if(!isLoading) {
            movieViewModel.deleteAll();
            moviesList.clear();
        }
    }

    /**
     * When we click a cardView, onEditClickListener is called.
     * We get the existing values of each field from the dataBase and pass them to the MovieCreateActivity
     *
     * @param position is the position of the cardView we clicked in the RecyclerView
     */
    @Override
    public void onEditClickListener(int position) {
        Intent intent = new Intent(this, MovieCreateActivity.class);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(TITLE_KEY, movieViewModel.getAllMovies().getValue().get(position).getTitle());
        intent.putExtra(DATE_KEY, movieViewModel.getAllMovies().getValue().get(position).getDate());
        intent.putExtra(DESCR_KEY, movieViewModel.getAllMovies().getValue().get(position).getDescription());
        intent.putExtra(IMAGE_KEY, movieViewModel.getAllMovies().getValue().get(position).getImageString());
        intent.putExtra(ID_KEY, movieViewModel.getAllMovies().getValue().get(position).getId());
        startActivityForResult(intent, 1);
    }
}