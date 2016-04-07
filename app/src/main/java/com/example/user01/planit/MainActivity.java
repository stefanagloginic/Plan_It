package com.example.user01.planit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    int randomInt;
    ArrayList<Business> businesses = new ArrayList<Business>();

    public void getStuff() {
        randomInt = randInt(0,20);
        YelpAPIFactory yelpAPIFactory = new YelpAPIFactory(
                getString(R.string.consumer_key),
                getString(R.string.consumer_secret),
                getString(R.string.token),
                getString(R.string.token_secret));
        YelpAPI yelpAPI = yelpAPIFactory.createAPI();

        Map<String, String> params = new HashMap<>();
        params.put("radius_filter", "16000");
        Call<SearchResponse> call = yelpAPI.search("Goleta", params);

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
                SearchResponse searchResponse = response.body();
                businesses = searchResponse.businesses();
                randomInt = randInt(0,businesses.size()-1);
                TextView businessTextView = (TextView) findViewById(R.id.BusinessName);
                businessTextView.setText(businesses.get(randomInt).name());

                TextView addressTextView = (TextView) findViewById(R.id.Address);
                addressTextView.setText(businesses.get(randomInt).location().address().get(0));

                TextView snippetTextView = (TextView) findViewById(R.id.SnippetText);
                snippetTextView.setText(businesses.get(randomInt).snippetText());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        call.enqueue(callback);
    }

    public void buttonPress(View view) {
        getStuff();
    }

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
