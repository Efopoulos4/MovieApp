package com.example.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {



    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView yearTextView;
        private final TextView descrTextView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            yearTextView = itemView.findViewById(R.id.year_text_view);
            descrTextView = itemView.findViewById(R.id.descrtiption_text_view);

        }
    }

    private final LayoutInflater mInflater;
    private List<Movie> mMovies;

    public MovieListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie current = mMovies.get(position);
        holder.titleTextView.setText(current.getTitle());
        holder.yearTextView.setText(String.valueOf(current.getYear()));
        holder.descrTextView.setText(current.getDescription());
    }

    void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        return 0;
    }


}
