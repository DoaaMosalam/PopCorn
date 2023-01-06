package com.example.moviesapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviesapp.pojo.MovieModel;
import com.example.moviesapp.request.MovieApiClient;

import java.util.List;

public class MovieRepository {
    //this class is acting repository.
    private static MovieRepository instance;
  private MovieApiClient movieApiClient;
  private String mQuery;
  private int mPageNumber;
//getter from instance.
    public static MovieRepository getInstance() {
        if (instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository(){
     movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }

    public LiveData<List<MovieModel>> getPop(){
        return movieApiClient.getMoviesPop();
    }

    //1-Calling method.
    public void SearchMoviesApi(String query,int pageNumber){
        movieApiClient.searchMoviesApi(query,pageNumber);
        mQuery = query;
        mPageNumber=pageNumber;
        movieApiClient.searchMoviesApi(query, pageNumber);
    }

    public void SearchMoviesPop(int pageNumber){
        movieApiClient.searchMoviesPop(pageNumber);
        mPageNumber=pageNumber;
        movieApiClient.searchMoviesPop(pageNumber);
    }

    public void SearchNextPage(){
        SearchMoviesPop(mPageNumber+1);
    }

//    public void SearchNextPage(){
//        SearchMoviesPop(mQuery,mPageNumber+1);
//    }
}
