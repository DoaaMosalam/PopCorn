package com.example.moviesapp.request;

import com.example.moviesapp.utils.Credentials;
import com.example.moviesapp.utils.MovieApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Servicey {
    /*initialization Retrofit */
   private static Retrofit retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Credentials.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
//   private static Retrofit retrofit = retrofitBuilder.build();
   private static MovieApi movieApi = retrofitBuilder.create(MovieApi.class);
   public static MovieApi getMovieApi(){
       return movieApi;
   }

}
