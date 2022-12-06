package com.example.movieapp;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    public void insert(Movie movie);

    @Query("DELETE FROM movie_table")
    public void deleteAll();

    @Query("SELECT * from movie_table ORDER BY title ASC")
    LiveData<List<Movie>> getAllMovies();
}