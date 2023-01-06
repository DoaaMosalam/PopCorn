package com.example.moviesapp.request;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.moviesapp.AppExecutor;
import com.example.moviesapp.pojo.MovieModel;
import com.example.moviesapp.response.MovieSearchResponse;
import com.example.moviesapp.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    //LiveData for search
    private MutableLiveData<List<MovieModel>> mMovie;
    private static MovieApiClient instance;

    //making Global Runnable
    private RetrieveMoviesRunnable retrieveMoviesRunnable;
//================================================================================================
    //LiveData for popular
   private MutableLiveData<List<MovieModel>> mMoviePopular;
    //making popular Runnable
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;

    //getter instance.
    public static MovieApiClient getInstance() {
        if (instance == null){
            instance = new MovieApiClient();
        }
        return instance;
    }

    //constructor.
    private MovieApiClient(){
        mMovie = new MutableLiveData<>();
        mMoviePopular=new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovie;
    }
    public LiveData<List<MovieModel>> getMoviesPop(){
        return mMoviePopular;
    }


    //    This method that  we are going to call through the class
    public void searchMoviesApi(String query,int pageNumber) {
        if (retrieveMoviesRunnable != null){
            retrieveMoviesRunnable=null;
        }

        retrieveMoviesRunnable  = new RetrieveMoviesRunnable(query, pageNumber);
        final Future myHandler = AppExecutor.getInstance().netWorkIo().submit(retrieveMoviesRunnable);
        AppExecutor.getInstance().netWorkIo().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling the retrofit call
                myHandler.cancel(true);

            }
        }, 3000, TimeUnit.MILLISECONDS);
    }

    public void searchMoviesPop(int pageNumber) {
        if (retrieveMoviesRunnablePop != null){
            retrieveMoviesRunnablePop=null;
        }

        retrieveMoviesRunnablePop  = new RetrieveMoviesRunnablePop(pageNumber);
        final Future myHandler2 = AppExecutor.getInstance().netWorkIo().submit(retrieveMoviesRunnablePop);
        AppExecutor.getInstance().netWorkIo().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling the retrofit call
                myHandler2.cancel(true);

            }
        }, 1000, TimeUnit.MILLISECONDS);
    }


    //Retrieving data from RestApi by runnable class.
    private class RetrieveMoviesRunnable implements Runnable{
        private String query;
        private int pageNumber;
        boolean cancelRequest;

//        Constructor (query,pageNumber).
            public RetrieveMoviesRunnable(String query, int pageNumber) {
                this.query = query;
                this.pageNumber = pageNumber;
                cancelRequest=false;
            }

            @Override
            public void run() {
                //Getting the response object.
                try{
                    Response response = getMovies(query,pageNumber).execute();
                    if (cancelRequest) {
                        return;
                    }
                    if (response.code()==200){
                        List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                        if (pageNumber ==1){
                            //sending data to LiveData.
                            //postValue:used for background thread
                            mMovie.postValue(list);
                        }else {
                            List<MovieModel> currentMovies = mMovie.getValue();
                            currentMovies.addAll(list);
                            mMovie.postValue(currentMovies);
                        }

                    }else{
                        String error= response.errorBody().string();
                        Log.v("Tag","Error" + error);
                        mMovie.postValue(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mMovie.postValue(null);
                }
            }
            //search method query
            private Call<MovieSearchResponse> getMovies(String query,int pageNumber){
                return Servicey.getMovieApi().searchMovie(
                        Credentials.API_KEY,
                        query,
                        pageNumber
                );
            }
            private void CancelRequest(){
                Log.v("Tag","Canceling Search Request");
                cancelRequest = true;
            }
     }

    private class RetrieveMoviesRunnablePop implements Runnable{
        private int pageNumber;
        boolean cancelRequest;

        //        Constructor (query,pageNumber).
        public RetrieveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest=false;
        }

        @Override
        public void run() {
            //Getting the response object.
            try{
                Response response2 = getPop(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response2.code()==200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if (pageNumber ==1){
                        //sending data to LiveData.
                        //postValue:used for background thread
                        //SetValue:not for background thread
                        mMoviePopular.postValue(list);
                    }else {
                        List<MovieModel> currentMovies = mMoviePopular.getValue();
                        currentMovies.addAll(list);
                        mMoviePopular.postValue(currentMovies);
                    }

                }else{
                    String error= response2.errorBody().string();
                    Log.v("Tag","Error" + error);
                    mMoviePopular.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviePopular.postValue(null);
            }
        }
        /*search method query*/
        private Call<MovieSearchResponse> getPop(int pageNumber){
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }

        private void CancelRequest(){
            Log.v("Tag","Canceling Search Request");
            cancelRequest = true;
        }

    }

}
