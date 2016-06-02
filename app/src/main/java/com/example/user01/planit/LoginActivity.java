package com.example.user01.planit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoL.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final Button bLogin = (Button) findViewById(R.id.bLogin);

        final TextView tvRegister = (TextView) findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!areRequiredFieldsFilled()) {
                    Toast.makeText(LoginActivity.this, "Enter Missing Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                startProgressBarAnimation();

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                User user = new User(jsonResponse);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("user", user);

                                LoginLogoutHelpers.saveUser(user, LoginActivity.this);

                                stopProgressBarAnimation();

                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                stopProgressBarAnimation();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            stopProgressBarAnimation();
                        }
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        stopProgressBarAnimation();
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener, errorListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private boolean areRequiredFieldsFilled(){
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        boolean isFilled = true;

        if(username.length()==0){
            isFilled = false;
        }
        if(password.length()==0){
            isFilled = false;
        }

        return isFilled;
    }

    private void stopProgressBarAnimation(){
        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        Button bNewPassword = (Button) findViewById(R.id.bLogin);

        TextView tvRegister = (TextView) findViewById(R.id.tvRegister);

        tvRegister.setEnabled(true);
        etUsername.setEnabled(true);
        etPassword.setEnabled(true);
        bNewPassword.setClickable(true);

        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoadingImage);
        pb.setVisibility(View.GONE);
    }

    private void startProgressBarAnimation(){
        EditText etUsername = (EditText) findViewById(R.id.etUsername);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);

        Button bNewPassword = (Button) findViewById(R.id.bLogin);

        TextView tvRegister = (TextView) findViewById(R.id.tvRegister);

        tvRegister.setEnabled(false);
        etUsername.setEnabled(false);
        etPassword.setEnabled(false);
        bNewPassword.setClickable(false);


        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoadingImage);
        pb.setVisibility(View.VISIBLE);
    }


}
