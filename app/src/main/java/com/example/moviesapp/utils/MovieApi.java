package com.example.moviesapp.utils;

import com.example.moviesapp.pojo.MovieModel;
import com.example.moviesapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    //search get movies
//    API_KEY = bfff23c4dd5f42fb49aeb61d18794d1a
//    https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
    @GET("3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );
    //Get popular movie.
   // https://api.themoviedb.org/3/movie/popular              ?api_key=bfff23c4dd5f42fb49aeb61d18794d1a&page=1
    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String key,
            @Query("page") int page
    );
// Making search with Id.
//    https://api.themoviedb.org/3/movie/550?api_key=bfff23c4dd5f42fb49aeb61d18794d1a
    //Remember  that movie _id = 550 is for Fight Club.
    @GET("/3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );

}
