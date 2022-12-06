package com.example.movieapp;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "movie_table")
public class Movie {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "year")
    private int mYear;
    @ColumnInfo(name = "description")
    private String mDescription;

    public Movie(String title, int year, String description){
        this.mTitle = title;
        this.mYear = year;
        this.mDescription = description;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getDescription(){
        return mDescription;
    }

    public int getYear(){
        return mYear;
    }
}
