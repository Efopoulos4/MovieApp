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

    @Query("UPDATE movie_table SET title= :newTitle, date= :newDate, description= :newDesc, imageString= :newImageString  WHERE id =:id")
    public void update(int id, String newTitle, String newDate, String newDesc, String newImageString);

    @Query("SELECT * from movie_table ORDER BY id ASC")
    LiveData<List<Movie>> getAllMovies();
}