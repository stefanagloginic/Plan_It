package com.example.user01.planit;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

        setFABListener();

        setTextViews();

        ImageView imProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        startProgressBarAnimation();

        if(LoginLogoutHelpers.isPhotoSaved(this)){
            BitmapDrawable pic = new BitmapDrawable(getResources(), LoginLogoutHelpers.getBitmapFromLocalStorage(this));
            Bitmap myBitmap = pic.getBitmap();
            if(myBitmap != null) {
                imProfilePic.setImageDrawable(pic);
            }
            stopProgressBarAnimation();

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
                    stopProgressBarAnimation();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(getBaseContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                stopProgressBarAnimation();
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
                stopProgressBarAnimation();
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

        startProgressBarAnimation();
        final ImageView imProfilePic = (ImageView) findViewById(R.id.ivProfilePic);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LoginLogoutHelpers.saveBitmapToLocalStorage(AccountActivity.this, LoginLogoutHelpers.encodeTobase64(b));
                Toast.makeText(AccountActivity.this, "Photo Added", Toast.LENGTH_SHORT).show();
                BitmapDrawable pic = new BitmapDrawable(getResources(), b);
                imProfilePic.setImageDrawable(pic);
                stopProgressBarAnimation();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AccountActivity.this, "Couldn't add Photo", Toast.LENGTH_SHORT).show();
                stopProgressBarAnimation();
            }
        };

        PhotoRequest photoRequest = new PhotoRequest(user.getUsername(), b, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(AccountActivity.this);
        queue.add(photoRequest);
    }

    private void stopProgressBarAnimation(){
        final ImageView imProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        imProfilePic.setClickable(true);
        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoadingImage);
        pb.setVisibility(View.GONE);
    }

    private void startProgressBarAnimation(){
        final ImageView imProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
        imProfilePic.setClickable(false);
        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoadingImage);
        pb.setVisibility(View.VISIBLE);
    }

    private void setFABListener(){
        final FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fabSettings);

        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RelativeLayout relativeLayoutContent = (RelativeLayout) findViewById(R.id.AccountInfo);
                relativeLayoutContent.setAlpha(0.1f);
                fabSettings.setVisibility(View.GONE);

                final RelativeLayout relativeLayoutSettings = (RelativeLayout) findViewById(R.id.Settings);
                relativeLayoutSettings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        relativeLayoutContent.setAlpha(1.0f);
                        relativeLayoutSettings.setVisibility(View.GONE);
                        fabSettings.setVisibility(View.VISIBLE);
                    }
                });

                setFABChangePassword();

                relativeLayoutSettings.setVisibility(View.VISIBLE);


            }
        });
    }

    private void setFABChangePassword(){
        FloatingActionButton fabGPS = (FloatingActionButton) findViewById(R.id.fabChangePassword);
        fabGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changePasswordIntent = new Intent(AccountActivity.this, ChangePasswordActivity.class);
                changePasswordIntent.putExtra("user", user);
                AccountActivity.this.startActivity(changePasswordIntent);
            }
        });
    }

    private void setTextViews(){
        TextView tvFullName = (TextView) findViewById(R.id.tvFullName);
        TextView tvUserUsername = (TextView) findViewById(R.id.tvuserUsername);
        TextView tvUserAge = (TextView) findViewById(R.id.tvUserAges);

        tvFullName.setText(user.getFirstName() + " " + user.getLastName());
        tvUserUsername.setText(user.getUsername());
        tvUserAge.setText(Integer.toString(user.getAge()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        final RelativeLayout relativeLayoutSettings = (RelativeLayout) findViewById(R.id.Settings);
        int visibility = relativeLayoutSettings.getVisibility();
        final RelativeLayout relativeLayoutContent = (RelativeLayout) findViewById(R.id.AccountInfo);
        final FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fabSettings);

        if(visibility == View.VISIBLE){
            relativeLayoutContent.setAlpha(1.0f);
            relativeLayoutSettings.setVisibility(View.GONE);
            fabSettings.setVisibility(View.VISIBLE);
        }
        else{
            this.finish();
        }
    }


}
