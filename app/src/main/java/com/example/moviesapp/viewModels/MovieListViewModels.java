package com.example.moviesapp.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.pojo.MovieModel;
import com.example.moviesapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModels  extends ViewModel {
    private MovieRepository movieRepository;
    //constructor
    public MovieListViewModels() {
        movieRepository = MovieRepository.getInstance();

    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getPop(){
        return movieRepository.getPop();
    }

    //2-Calling method in View-Model.
    public void SearchMovieApi(String query,int pageNumber){
        movieRepository.SearchMoviesApi(query, pageNumber);
    }

    public void SearchMoviePop(int pageNumber){
        movieRepository.SearchMoviesPop(pageNumber);
    }
    public void SearchNextPage(){
        movieRepository.SearchNextPage();
    }
}
