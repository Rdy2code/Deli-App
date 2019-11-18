package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Get references to views used by Picasso library
        ImageView ingredientsIv = findViewById(R.id.image_iv);
        final ProgressBar progressBar = findViewById(R.id.imageProgress);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        //Enable navigation back to home page via the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);

        //Log message for testing purposes only
        Log.v("Image url", sandwich.getImage());

        Picasso.get()
                .load(sandwich.getImage())
                .error(R.mipmap.ic_launcher_round)
                .into(ingredientsIv, new Callback() {

                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                R.string.network_error,
                                Toast.LENGTH_LONG).show();
                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        //Get references to the several text views in the activity_detail layout
        //TODO: Replace with ButterKnife?
        TextView originTV = findViewById(R.id.origin_tv);
        TextView akaTV = findViewById(R.id.also_known_tv);
        TextView descriptionTV = findViewById(R.id.description_tv);
        TextView ingredientsTV = findViewById(R.id.ingredients_tv);

        //Get the text or list for each attribute (View) for the Sandwich object that started this activity
        //Then set or append the text to the appropriate TextView in the UI.
        //Notify the user if no data is available for given keys

        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            originTV.setText(R.string.detail_error_message);
        } else {
            originTV.setText(sandwich.getPlaceOfOrigin());
        }

        if (sandwich.getAlsoKnownAs().isEmpty()) {
            akaTV.setText(R.string.detail_error_message);
        } else {
            akaTV.setText(TextUtils.join(", ", sandwich.getAlsoKnownAs()));
        }

        descriptionTV.setText(sandwich.getDescription());

        ingredientsTV.setText(TextUtils.join(", ", sandwich.getIngredients()));
    }
}
