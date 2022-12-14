package com.example.movieapp;

import android.app.Application;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieDao mMovieDao;
    private LiveData<List<Movie>> mAllMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mAllMovies = mMovieDao.getAllMovies();
    }

    /**
     * returns all the movies saved in the dataBase
     * @return
     */
    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }

    public void insert(Movie movie) {
        new insertAsyncTask(mMovieDao).execute(movie);
    }

    public void update(Movie movie, int id, String title, String date, String desc, String imageString) {
        new updateAsyncTask(mMovieDao, id, title, date, desc, imageString).execute(movie);
    }

    public void deleteAll() {
        new deleteAsyncTask(mMovieDao).execute();
    }

    /**
     * Async Task for UPDATE action
     */
    private static class updateAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao mAsyncTaskDao;
        private int id;
        private String title;
        private String date;
        private String desc;
        private String imageString;

        updateAsyncTask(MovieDao dao, int id, String title, String date, String desc, String imageString) {
            this.id = id;
            this.title = title;
            this.date = date;
            this.desc = desc;
            this.imageString = imageString;
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            mAsyncTaskDao.update(id, title, date, desc, imageString);
            return null;
        }
    }

    /**
     * Async Task for INSERT action
     */
    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            mAsyncTaskDao.insert(movies[0]);
            return null;
        }
    }

    /**
     * Async Task for DELETE action
     */
    private static class deleteAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        deleteAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
