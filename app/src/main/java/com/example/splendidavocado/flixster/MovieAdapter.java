package com.example.splendidavocado.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.splendidavocado.flixster.models.Config;
import com.example.splendidavocado.flixster.models.GlideApp;
import com.example.splendidavocado.flixster.models.Movie;
import com.example.splendidavocado.flixster.models.MyGlideAppModule;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Viewholder> {

    // list of movies
    ArrayList<Movie> movies;

    Config config;
    Context context;


    // initialize with list


    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // creates and inflates a new view
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // get the context and create an inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // return a new viewHolder


        return new Viewholder(movieView);
    }

    // binds an inflated view
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        // get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());


        // determine orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


        // build url for poster image
        String imageUrl = null;
        // load image using glide

        // if portrait mode, load poster image


        if (isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        } else
        {
            // load bsckdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        // get correct placeholder for orientation

        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView= isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop
        GlideApp.with(context)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(radius, margin))

                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);

    }

    // returns the number of items on the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the viewholder as a static inner class
    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // trsack view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;


        public Viewholder(View itemView) {
            super(itemView);
            // lookup view objects by id

            ButterKnife.bind(this, itemView);
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView)  itemView.findViewById(R.id.ivBackdropImage);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);


            }
        }
    }
}