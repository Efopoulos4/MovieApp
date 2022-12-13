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

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private String TAG = "paok";
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
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new MovieViewHolder(itemView, onEditListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie current = mMovies.get(position);
        try{
            holder.imageView.setImageURI(Uri.parse(current.getImageString()));
        }catch (Exception e){
            holder.imageView.setImageURI(Uri.parse("https://picsum.photos/id/237/200/300"));
        }
        holder.titleTextView.setText(current.getTitle());
        holder.yearTextView.setText(current.getDate());
        holder.descrTextView.setText(current.getDescription());

    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        return 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView titleTextView;
        private final TextView yearTextView;
        private final TextView descrTextView;
        onEditListener onEditListener;

        private MovieViewHolder(View itemView, onEditListener onEditListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            yearTextView = itemView.findViewById(R.id.year_text_view);
            descrTextView = itemView.findViewById(R.id.descrtiption_text_view);
            this.onEditListener = onEditListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            onEditListener.onEditClickListener(getAdapterPosition());
        }
    }

    public interface onEditListener{
        void onEditClickListener(int position);
    }

}
