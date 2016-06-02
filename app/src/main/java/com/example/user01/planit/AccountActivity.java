package com.example.user01.planit;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AccountActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 49;
    private User user;
    private final int PICK_IMAGE_ID = 390;
    private final int PIC_CROP = 430;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Bundle data = getIntent().getExtras();
        user = data.getParcelable("user");

        ImageView imProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        if(LoginLogoutHelpers.isPhotoSaved(this)){
            BitmapDrawable pic = new BitmapDrawable(getResources(), LoginLogoutHelpers.getBitmapFromLocalStorage(this));
            imProfilePic.setImageDrawable(pic);
        }else {
            createGetPhotoRequest();
        }

        imProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });


    }

    private void createGetPhotoRequest() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String profilepicture = jsonResponse.getString("profilepicture");
                    LoginLogoutHelpers.saveBitmapToLocalStorage(AccountActivity.this ,profilepicture);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                if(resultCode == RESULT_OK) {
                    bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                    // TODO use bitmap
                    performCrop(bitmap, LoginLogoutHelpers.getImageUri(this, bitmap));
                    break;
                }
                break;
            case PIC_CROP:
                if(resultCode == RESULT_OK) {
                    //get the returned data
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    Bitmap bitmaps = extras.getParcelable("data");
                    try {
                        Bitmap smallerbitmap = LoginLogoutHelpers.scaleImage(this, LoginLogoutHelpers.getImageUri(this, bitmaps));
                        createPhotoRequest(smallerbitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    onPickImage();

                }
                break;

        }
    }

    private void performCrop(Bitmap bitmap, Uri uri){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message

            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();

            try {
                Bitmap smallerbitmap = LoginLogoutHelpers.scaleImage(this, LoginLogoutHelpers.getImageUri(this,bitmap));
                createPhotoRequest(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void onPickImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }else{
            onPickImage();
        }
    }

    void createPhotoRequest(final Bitmap b){

        final ImageView imProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                LoginLogoutHelpers.saveBitmapToLocalStorage(AccountActivity.this, LoginLogoutHelpers.encodeTobase64(b));
                Toast.makeText(AccountActivity.this, "Photo Added", Toast.LENGTH_SHORT).show();
                BitmapDrawable pic = new BitmapDrawable(getResources(), b);
                imProfilePic.setImageDrawable(pic);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountActivity.this, "Couldn't add Photo", Toast.LENGTH_SHORT).show();
            }
        };

        PhotoRequest photoRequest = new PhotoRequest(user.getUsername(), b, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(AccountActivity.this);
        queue.add(photoRequest);
    }

}
