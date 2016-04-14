package com.example.user01.planit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText inputField;
    private Button generateButton;
    private Button button;
    private ArrayList<Event> events;
    private YelpComboWrapper yelp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        inputField = (EditText) findViewById(R.id.inputfield);
        button = (Button) findViewById(R.id.button);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setEnabled(false);

        generateButton = (Button) findViewById(R.id.generate);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yelp = new YelpComboWrapper(MainActivity.this, inputField.getText().toString());
            }
        });
    }

    public void createRecyclerView(View view) {
        events = yelp.createYelpEventArray();
        Intent intent = new Intent(this,RecyclerActivity.class);
        intent.putParcelableArrayListExtra("events", events);
        startActivity(intent);
    }
}


