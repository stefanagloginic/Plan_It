package com.example.user01.planit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private YelpComboWrapper yelp;
    private ArrayList<YelpEvent> events;
    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private Button button;
    private Button generateButton;
    private EditText inputField;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gets the various TextViews in order to change them
        textView = (TextView) findViewById(R.id.businessName);
        textView1 = (TextView) findViewById(R.id.address);
        textView2 = (TextView) findViewById(R.id.rating);
        textView3 = (TextView) findViewById(R.id.url);
        textView4 = (TextView) findViewById(R.id.hours);
        textView5 = (TextView) findViewById(R.id.price);

        inputField = (EditText) findViewById(R.id.inputfield);

        View b = findViewById(R.id.button);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setEnabled(false);

        // Create a new Yelp wrapper object
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events = yelp.createYelpEventArray();
                int randNum = randint();
                textView.setText(events.get(randNum).getBusinessName());
                textView1.setText(events.get(randNum).getBusinessAddress());
                textView2.setText(events.get(randNum).getBusinessRating());
                textView3.setText(events.get(randNum).getBusinessURL());
                textView4.setText(events.get(randNum).getBusinessHours());
                textView5.setText(events.get(randNum).getBusinessPriceRange());
            }
        });

        generateButton = (Button) findViewById(R.id.generate);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yelp = new YelpComboWrapper(MainActivity.this, inputField.getText().toString());
            }
        });
    }

    public int randint(){
        int rand =(int) (Math.random()*events.size());
        return rand;
    }
}


