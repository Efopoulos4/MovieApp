package com.example.movieapp;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "movie_table")
public class Movie {


    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "year")
    private int year;
    @ColumnInfo(name = "description")
    private String description;

    public Movie(String title, int year, String description){
        this.title = title;
        this.year = year;
        this.description = description;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public int getYear(){
        return year;
    }
}
