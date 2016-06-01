package com.example.user01.planit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Bundle data = getIntent().getExtras();
        user = data.getParcelable("user");

        ImageView imProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        createGetPhotoRequest();

    }

    private void createGetPhotoRequest() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String profilepicture = jsonResponse.getString("profilepicture");
                    setProfilePicture(profilepicture);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        };

        GetPhotoRequest photoRequest = new GetPhotoRequest(user.getUsername(), responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(AccountActivity.this);
        queue.add(photoRequest);
    }

    private void setProfilePicture(String image){
        if(image.equals("")){
            Toast.makeText(getBaseContext(), "No image found", Toast.LENGTH_SHORT).show();
        }else{
            ImageView imProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
            Bitmap bitmap = LoginLogoutHelpers.decodeBase64(image);
            BitmapDrawable pic = new BitmapDrawable(getResources(), bitmap);
            imProfilePic.setImageDrawable(pic);
        }
    }
}
