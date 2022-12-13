package com.example.movieapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "imageString")
    private String imageString;

    public Movie(String title, String date, String description, String imageString){
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageString = imageString;
    }
    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getDate(){
        return date;
    }

    public String getImageString(){
        return imageString;
    }

}
