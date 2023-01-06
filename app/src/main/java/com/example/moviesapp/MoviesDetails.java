package com.example.moviesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.moviesapp.pojo.MovieModel;

public class MoviesDetails extends Activity {
    ImageView imageDetails;
    TextView titleDetails,overViewDetails;
    RatingBar ratingBarDetails;
    Button buyTicket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        imageDetails=findViewById(R.id.img_details);
        titleDetails=findViewById(R.id.textView_title_details);
        overViewDetails=findViewById(R.id.textView_overView_details);
        ratingBarDetails=findViewById(R.id.ratingBar_movie_details);
        buyTicket=findViewById(R.id.button_movie_details);

        GetDataFromIntent();
        BuyTicket();
    }
//This method when user click button Buy ticket.
    private void BuyTicket() {
        buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MoviesDetails.this, "Enjoy to watch movies.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetDataFromIntent() {
        if (getIntent().hasExtra("movie")){
            MovieModel movieModel = getIntent().getParcelableExtra("movie");
            titleDetails.setText(movieModel.getTitle());
            overViewDetails.setText(movieModel.getOverview());
            ratingBarDetails.setRating((movieModel.getVote_average())/2);
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500/" + movieModel.getPoster_path())
                    .error(R.drawable.ic_baseline_local_movies_24)
                    .into(imageDetails);
        }
    }
}
