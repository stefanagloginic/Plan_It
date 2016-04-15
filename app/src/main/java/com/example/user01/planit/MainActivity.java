package com.example.user01.planit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private YelpAPIWrapper yelpAPIWrapper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        final Spinner timeOfDaySpinner = (Spinner) findViewById(R.id.time_of_day_spinner);
        ArrayAdapter<CharSequence> timeOfDayAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.time_of_day_choices,
                        R.layout.spinner_center_dropdown);
        timeOfDayAdapter.setDropDownViewResource(R.layout.spinner_center_dropdown);
        timeOfDaySpinner.setAdapter(timeOfDayAdapter);

        final Spinner priceRangeSpinner = (Spinner) findViewById(R.id.price_range_spinner);
        ArrayAdapter<CharSequence> priceRangeAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.price_range_choices,
                        R.layout.spinner_center_dropdown);
        priceRangeAdapter.setDropDownViewResource(R.layout.spinner_center_dropdown);
        priceRangeSpinner.setAdapter(priceRangeAdapter);

        final Spinner activityPreferencesSpinner =
                (Spinner) findViewById(R.id.activity_preferences_spinner);
        ArrayAdapter<CharSequence> activityPreferencesAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.activity_preferences_choices,
                        R.layout.spinner_center_dropdown);
        activityPreferencesAdapter.setDropDownViewResource(R.layout.spinner_center_dropdown);
        activityPreferencesSpinner.setAdapter(activityPreferencesAdapter);

        Button generateButton = (Button) findViewById(R.id.generate);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText locationInput = (EditText) findViewById(R.id.location_input);
                String location = locationInput.getText().toString();
                String timeOfDay = timeOfDaySpinner.getSelectedItem().toString();
                String priceRange = priceRangeSpinner.getSelectedItem().toString();
                String activityPreference = activityPreferencesSpinner.getSelectedItem().toString();
                ArrayList<String> settings = new ArrayList<>();
                settings.add(location);
                settings.add(timeOfDay);
                settings.add(priceRange);
                settings.add(activityPreference);
                yelpAPIWrapper = new YelpAPIWrapper(getBaseContext(), settings);
                yelpAPIWrapper.execute();
            }
        });
    }

}


