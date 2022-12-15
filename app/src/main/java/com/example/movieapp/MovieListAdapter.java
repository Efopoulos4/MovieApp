package com.example.movieapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int VIEW_TYPE_LOADING = 1;
    final int VIEW_TYPE_ITEM = 0;
    private final LayoutInflater mInflater;
    private List<Movie> mMovies;
    private Context context;
    private onEditListener onEditListener;

    public MovieListAdapter(Context context, onEditListener onEditListener) {
        this.onEditListener = onEditListener;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
            return new MovieViewHolder(itemView, onEditListener);
        }else {
            View loadingView = mInflater.inflate(R.layout.loading_item, parent, false);
            return new LoadingHolder(loadingView);
        }
    }

    /**
     * We separate if the holder is instance of Movie or LoadingView
     * @param holder
     * @param position
     */
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            populateItemRows((MovieViewHolder) holder, position);
        } else {
            showLoadingView((LoadingHolder) holder, position);
        }
    }

    /**
     * if its instance of Movie we fill the fields with the values we have passed passed on the list
     * @param holder
     * @param position
     */
    private void populateItemRows(MovieViewHolder holder, int position) {
        Movie current = mMovies.get(position);
        holder.imageView.setImageURI(Uri.parse(current.getImageString()));
        holder.titleTextView.setText(current.getTitle());
        holder.yearTextView.setText(current.getDate());
        holder.descriptionTextView.setText(current.getDescription());
    }


    private void showLoadingView(LoadingHolder viewHolder, int position) {
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(mMovies.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView titleTextView;
        private final TextView yearTextView;
        private final TextView descriptionTextView;
        onEditListener onEditListener;

        private MovieViewHolder(View itemView, onEditListener onEditListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            yearTextView = itemView.findViewById(R.id.year_text_view);
            descriptionTextView = itemView.findViewById(R.id.descrtiption_text_view);
            this.onEditListener = onEditListener;
            itemView.setOnClickListener(this);
        }

        /**
         * When the cardView is clicked we handle the action from MainActivity
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            onEditListener.onEditClickListener(getAdapterPosition());
        }
    }

    public interface onEditListener {
        void onEditClickListener(int position);
    }
}