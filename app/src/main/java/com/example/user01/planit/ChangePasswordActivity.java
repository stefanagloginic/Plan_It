package com.example.user01.planit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ChangePasswordActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoL.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        user = LoginLogoutHelpers.retrieveUser(this);

        Button bNewPassword = (Button) findViewById(R.id.bChangePassword);

        bNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgressBarAnimation();
                EditText etOldPassword = (EditText) findViewById(R.id.etOldPassword);
                if (checkCredentials()) {
                    createNewPasswordRequest();
                } else {
                    if (etOldPassword.getText().toString().equals(user.getPassword())) {
                        Toast.makeText(ChangePasswordActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "New Passwords Not Matching", Toast.LENGTH_SHORT).show();
                    }
                    stopProgressBarAnimation();
                }

            }
        });
    }

    private void createNewPasswordRequest() {
        final EditText etNewPassword = (EditText) findViewById(R.id.etNewPassword) ;

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                    user.setPassword(EncryptHelper.Encrypt(etNewPassword.getText().toString()));
                    LoginLogoutHelpers.saveUser(ChangePasswordActivity.this.user, ChangePasswordActivity.this);

                    Toast.makeText(ChangePasswordActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                    stopProgressBarAnimation();
                    ChangePasswordActivity.this.finish();


            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                stopProgressBarAnimation();
            }
        };

        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(user.getUsername(),EncryptHelper.Encrypt(etNewPassword.getText().toString()), responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);
        queue.add(newPasswordRequest);
    }

    private boolean checkCredentials(){
        boolean isCorrect = false;
        EditText etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        boolean caseOne = EncryptHelper.Encrypt(etOldPassword.getText().toString()).equals(user.getPassword());
        Log.i("caseOne", Boolean.toString(caseOne));
        EditText etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        EditText etRetypePassword = (EditText) findViewById(R.id.etRetypePassword);
        boolean caseTwo = etNewPassword.getText().toString().equals(etRetypePassword.getText().toString());
        Log.i("caseTwo", Boolean.toString(caseTwo));
        if(caseOne && caseTwo){
            isCorrect = true;
        }
        return isCorrect;
    }

    private void stopProgressBarAnimation(){
        EditText etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        EditText etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        EditText etRetypePassword = (EditText) findViewById(R.id.etRetypePassword);
        Button bNewPassword = (Button) findViewById(R.id.bChangePassword);

        etOldPassword.setEnabled(true);
        etNewPassword.setEnabled(true);
        etRetypePassword.setEnabled(true);
        bNewPassword.setClickable(true);

        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoadingImage);
        pb.setVisibility(View.GONE);
    }

    private void startProgressBarAnimation(){
        EditText etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        EditText etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        EditText etRetypePassword = (EditText) findViewById(R.id.etRetypePassword);
        Button bNewPassword = (Button) findViewById(R.id.bChangePassword);

        etOldPassword.setEnabled(false);
        etNewPassword.setEnabled(false);
        etRetypePassword.setEnabled(false);
        bNewPassword.setClickable(false);


        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoadingImage);
        pb.setVisibility(View.VISIBLE);
    }



}

