package com.example.user01.planit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    YelpAPIWrapper yelp;
    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gets the various TextViews in order to change them
        textView = (TextView) findViewById(R.id.businessName);
        textView1 = (TextView) findViewById(R.id.address);
        textView2 = (TextView) findViewById(R.id.rating);
        textView3 = (TextView) findViewById(R.id.url);
        // Create a new Yelp wrapper object
        yelp = new YelpAPIWrapper(MainActivity.this);
        yelp.execute();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(yelp.getBusinessName());
                textView1.setText(yelp.getBusinessAddress());
                textView2.setText(yelp.getBusinessRating());
                textView3.setText(yelp.getBusinessURL());
            }
        });
    }

}


