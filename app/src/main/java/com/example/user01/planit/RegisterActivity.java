package com.example.user01.planit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/RobotoL.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        final ArrayList<EditText> editTextArrayList = new ArrayList<EditText>();

        editTextArrayList.add((EditText) findViewById(R.id.etFirstName));
        editTextArrayList.add((EditText)  findViewById(R.id.etLastName));
        editTextArrayList.add((EditText) findViewById(R.id.etUserName));
        editTextArrayList.add((EditText) findViewById(R.id.etPassword));
        editTextArrayList.add((EditText) findViewById(R.id.etAge));



        final Button bRegister = (Button) findViewById(R.id.bRegister);

        assert bRegister != null;
        assert bRegister != null;
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!areRequiredFieldsFilled(editTextArrayList)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Please Enter Missing Fields")
                            .setNegativeButton("OK", null)
                            .create()
                            .show();
                    return;
                }
                final String firstName = editTextArrayList.get(0).getText().toString();
                final String lastName = editTextArrayList.get(1).getText().toString();
                final String username = editTextArrayList.get(2).getText().toString();
                final String password = editTextArrayList.get(3).getText().toString();
                final String age = editTextArrayList.get(4).getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                RegisterActivity.this.startActivity(intent);
                                RegisterActivity.this.finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                        }
                    }

                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Failed no internet access!")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(firstName, lastName, username, Integer.parseInt(age), password, responseListener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private boolean areRequiredFieldsFilled(ArrayList<EditText> editTextArrayList){
        final String firstName = editTextArrayList.get(0).getText().toString();
        final String lastName = editTextArrayList.get(1).getText().toString();
        final String username = editTextArrayList.get(2).getText().toString();
        final String password = editTextArrayList.get(3).getText().toString();
        final String age = editTextArrayList.get(4).getText().toString();

        boolean isFilled = true;

        if(firstName.length() == 0){
            isFilled = false;
        }
        if(lastName.length()==0){
            isFilled = false;
        }
        if(username.length()== 0){
            isFilled = false;
        }
        if(password.length() == 0){
            isFilled = false;
        }
        if(age.length() == 0){
            isFilled = false;
        }

        return isFilled;
    }
}
