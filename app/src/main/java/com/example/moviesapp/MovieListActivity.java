package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.adapter.MovieRecyclerView;
import com.example.moviesapp.adapter.OnMovieListener;
import com.example.moviesapp.pojo.MovieModel;
import com.example.moviesapp.request.Servicey;
import com.example.moviesapp.response.MovieSearchResponse;
import com.example.moviesapp.utils.Credentials;
import com.example.moviesapp.utils.MovieApi;
import com.example.moviesapp.viewModels.MovieListViewModels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    MovieListViewModels movieListViewModels;
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;
    boolean isPopular=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        //toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Search Movies.
        SetUpSearchView();

         movieListViewModels =  new ViewModelProvider(this).get(MovieListViewModels.class);
//Calling the Observe method.
        ObserveAnyChange();
        ConfigureRecyclerView();

        ObservePopularMovies();
        //Getting Popular Movies.
        movieListViewModels.SearchMoviePop(1);

    }

    private void ObservePopularMovies() {
        movieListViewModels.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null){
                    for (MovieModel movieModel:movieModels) {
                        //Get The data in log
                        Log.v("Tag","onChanged: "+movieModel.getTitle());
                        movieRecyclerViewAdapter.setmMovie(movieModels);
                    }
                }
            }
        });
    }


    // Observing any data change.
    private void ObserveAnyChange(){
        movieListViewModels.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null){
                    for (MovieModel movieModel:movieModels) {
                        //Get The data in log
                        Log.v("Tag","onChanged: "+movieModel.getTitle());
                        movieRecyclerViewAdapter.setmMovie(movieModels);
                    }
                }
            }
        });
    }


//5_ initialization RecyclerView&adding data to it
    public void ConfigureRecyclerView(){
         movieRecyclerViewAdapter = new MovieRecyclerView(this);
         recyclerView.setAdapter(movieRecyclerViewAdapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
         recyclerView.setHasFixedSize(true);
         recyclerView.setItemAnimator(new DefaultItemAnimator());
         recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));

         //RecyclerView Pagination.
        //Loading next page of api response.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollHorizontally(1)){
                    //Here we need to display the next search results on the next page of api.
                    movieListViewModels.SearchNextPage();
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        //we don't need position of the movie in recyclerView.
        //We need the ID of the movie  in order to get all details
        Intent intent = new Intent(this, MoviesDetails.class);
        intent.putExtra("movie",movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);

    }

    @Override
    public void OnCategoryClick(String Catergory) {

    }

    /*This method used by search movies in toolbar SearchView*/
    private void SetUpSearchView() {
        final SearchView searchView =findViewById(R.id.search_View);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModels.SearchMovieApi(
                        //The search string getter from searchView
                        query,1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular=false;
            }
        });
    }

// this method to search movies
    private void GetRetrofitResponse() {
      MovieApi movieApi = Servicey.getMovieApi();
      Call<MovieSearchResponse> responseCall = movieApi
              .searchMovie(
                      Credentials.API_KEY,
                      "Jack Reacher",
                      1);
      responseCall.enqueue(new Callback<MovieSearchResponse>() {
          @Override
          public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
              if (response.code() == 200){
//                  Log.v("Tag","the response" +response.body().toString());
                  List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
                  for (MovieModel movie:movies) {
                      Log.v("Tag","Name: " + movie.getTitle());
                  }
              }else {
                  try {
                      Log.v("Tag","Error" + response.errorBody().string());
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          }

          @Override
          public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

          }
      });
    }

    //this method to search movies by id
    //Remember that movie _id = 550 is for Fight Club.
    // Movie_id = 343611 is for Jack Reacher.
    private void GetRetrofitAccordingToId(){
        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieModel> responseCall = movieApi.getMovie(
                343611,
                Credentials.API_KEY);
        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200){
                    MovieModel movieModel = response.body();
                    Log.v("Tag", "id: " + movieModel.getTitle());
                }else {
                    try {
                        Log.v("Tag", "Error " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }



}