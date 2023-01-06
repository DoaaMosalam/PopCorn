package com.example.moviesapp.response;

import com.example.moviesapp.pojo.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieResponse {
    //finding movie object.
    @SerializedName("results")
    @Expose
    private MovieModel movie;
//getting MovieModel.
    public MovieModel getMovie() {
        return movie;
    }

    //To String
    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
