package com.example.moviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.R;
import com.example.moviesapp.pojo.MovieModel;
import com.example.moviesapp.utils.Credentials;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MovieModel> mMovie;
    private OnMovieListener onMovieListener;
    private static final int DISPLAY_POP=1;
    private static final int DISPLAY_SEARCH=2;
    //constructor.
    public MovieRecyclerView( OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=null;
        if (viewType == DISPLAY_SEARCH){
                   view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_list_item,parent,false);
                   return new MovieViewHolder(view,onMovieListener);
        }else{
                    view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.popular_movie_layout,parent,false);
                    return new PopularMovieViewHolder(view, onMovieListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);
        if (itemViewType==DISPLAY_SEARCH){
            ((MovieViewHolder)holder).title.setText(mMovie.get(position).getTitle());

            //image view by Glide library
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/"+mMovie.get(position).getPoster_path())
                    .into(((MovieViewHolder)holder).image);
            //vote average is over 10,and our rating bar is over 5 stars:dividing by 2
            ((MovieViewHolder)holder).ratingBar.setRating((mMovie.get(position).getVote_average())/2);
        }
        else {
            ((PopularMovieViewHolder)holder).titlePop.setText(mMovie.get(position).getTitle());

            //image view by Glide library
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/"+mMovie.get(position).getPoster_path())
                    .into(((PopularMovieViewHolder)holder).imagePop);
            //vote average is over 10,and our rating bar is over 5 stars:dividing by 2
            ((PopularMovieViewHolder)holder).ratingBarPop.setRating((mMovie.get(position).getVote_average())/2);
        }
    }

    @Override
    public int getItemCount() {
        if (mMovie != null){
           return mMovie.size();
        }
        return 0;
    }
    //Set movies
    public void setmMovie(List<MovieModel> mMovie) {
        this.mMovie = mMovie;
        notifyDataSetChanged();
    }

    // Getting the id  of the  movie Clicked.
    public MovieModel getSelectedMovie(int position){
        if (mMovie!=null){
            if (mMovie.size()>0){
                return mMovie.get(position);
            }
        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
        if (Credentials.POPULAR){
            return DISPLAY_POP;
        }else {
            return DISPLAY_SEARCH;
        }

    }

    //inner class View Holder
    static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView image;
        RatingBar ratingBar;
        //click listener.
        OnMovieListener onMovieListener;

        public MovieViewHolder(@NonNull View itemView,OnMovieListener onMovieListener) {
            super(itemView);
            this.onMovieListener = onMovieListener;
            image = itemView.findViewById(R.id.movie_img);
            title=itemView.findViewById(R.id.movie_title);
            ratingBar=itemView.findViewById(R.id.rating_bar);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onMovieListener.onMovieClick(getAdapterPosition());

        }
    }


// inner Class popular View Holder
static class PopularMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titlePop;
        ImageView imagePop;
        RatingBar ratingBarPop;
        //click listener.
        OnMovieListener onMovieListener;
        public PopularMovieViewHolder(View view, OnMovieListener onMovieListener) {
            super(view);
            this.onMovieListener = onMovieListener;
            imagePop = itemView.findViewById(R.id.movie_imgPop);
            titlePop=itemView.findViewById(R.id.movie_titlePop);
            ratingBarPop=itemView.findViewById(R.id.rating_barPop);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieListener.onMovieClick(getAdapterPosition());

        }
    }


}
