package com.example.user01.planit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_ID = 275;
    private static final int CAMERA_PERMISSION_CODE = 3;
    private static final int PIC_CROP = 290;
    private Bitmap bitmap;

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
        editTextArrayList.add((EditText) findViewById(R.id.etLastName));
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
                    Toast.makeText(RegisterActivity.this, "Please Enter Missing Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                startProgressBarAnimation();

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
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                stopProgressBarAnimation();
                                createDialogBoxPhoto();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                stopProgressBarAnimation();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopProgressBarAnimation();
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                        }
                    }

                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        stopProgressBarAnimation();
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

    private boolean areRequiredFieldsFilled(ArrayList<EditText> editTextArrayList) {
        final String firstName = editTextArrayList.get(0).getText().toString();
        final String lastName = editTextArrayList.get(1).getText().toString();
        final String username = editTextArrayList.get(2).getText().toString();
        final String password = editTextArrayList.get(3).getText().toString();
        final String age = editTextArrayList.get(4).getText().toString();

        boolean isFilled = true;

        if (firstName.length() == 0) {
            isFilled = false;
        }
        if (lastName.length() == 0) {
            isFilled = false;
        }
        if (username.length() == 0) {
            isFilled = false;
        }
        if (password.length() == 0) {
            isFilled = false;
        }
        if (age.length() == 0) {
            isFilled = false;
        }

        return isFilled;
    }

    public void onPickImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                if(resultCode == RESULT_OK) {
                    bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                    performCrop(bitmap, LoginLogoutHelpers.getImageUri(this, bitmap));
                    break;
                }else if(resultCode == RESULT_CANCELED){
                    startLoginActivity();
                    break;
                }
                else{
                    startLoginActivity();
                    break;
                }
            case PIC_CROP:
                if(resultCode == RESULT_OK) {
                    startProgressBarAnimation();
                    //get the returned data
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    Bitmap bitmaps = extras.getParcelable("data");
                    try {
                        Bitmap smallerbitmap = LoginLogoutHelpers.scaleImage(this, LoginLogoutHelpers.getImageUri(this, bitmaps));
                        createPhotoRequest(smallerbitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        startLoginActivity();
                    }
                    break;
                }else if(resultCode == RESULT_CANCELED){
                    startLoginActivity();
                    break;
                }else{
                    try {
                        Bitmap smallerbitmap = LoginLogoutHelpers.scaleImage(this, LoginLogoutHelpers.getImageUri(this, bitmap));
                        createPhotoRequest(smallerbitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

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

                } else {

                    startLoginActivity();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
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
                startLoginActivity();
                Toast.makeText(RegisterActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    public void createDialogBoxPhoto(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Add Profile Photo?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestCameraPermission();
            }
        });
        builder.setNegativeButton("Maybe Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startLoginActivity();

            }
        });

        builder.show();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }else{
            onPickImage();
        }
    }

    void createPhotoRequest(Bitmap b){
        EditText etUsername = (EditText) findViewById(R.id.etUserName);

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                         startLoginActivity();
                         Toast.makeText(RegisterActivity.this, "Photo Added", Toast.LENGTH_SHORT).show();   
                    }
                    else{
                          startLoginActivity();
                          Toast.makeText(RegisterActivity.this, "Failed to add photo", Toast.LENGTH_SHORT).show(); 
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                startLoginActivity();

                Toast.makeText(RegisterActivity.this, "Photo Added", Toast.LENGTH_SHORT).show();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                startLoginActivity();

                Toast.makeText(RegisterActivity.this, "Couldn't add Photo", Toast.LENGTH_SHORT).show();
            }
        };

        PhotoRequest photoRequest = new PhotoRequest(etUsername.getText().toString(), b, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(photoRequest);
    }

    private void stopProgressBarAnimation(){
        EditText etUsername = (EditText) findViewById(R.id.etUserName);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
        EditText etLastName = (EditText) findViewById(R.id.etLastName);
        EditText etAge = (EditText) findViewById(R.id.etAge);

        Button bRegister = (Button) findViewById(R.id.bRegister);

        etUsername.setEnabled(true);
        etPassword.setEnabled(true);
        etAge.setEnabled(true);
        etFirstName.setEnabled(true);
        etLastName.setEnabled(true);

        bRegister.setClickable(true);


        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoadingImage);
        pb.setVisibility(View.GONE);
    }

    private void startProgressBarAnimation(){
        EditText etUsername = (EditText) findViewById(R.id.etUserName);
        EditText etPassword = (EditText) findViewById(R.id.etPassword);
        EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
        EditText etLastName = (EditText) findViewById(R.id.etLastName);
        EditText etAge = (EditText) findViewById(R.id.etAge);

        Button bRegister = (Button) findViewById(R.id.bRegister);

        etUsername.setEnabled(false);
        etPassword.setEnabled(false);
        etAge.setEnabled(false);
        etFirstName.setEnabled(false);
        etLastName.setEnabled(false);

        bRegister.setClickable(false);


        ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoadingImage);
        pb.setVisibility(View.VISIBLE);
    }

    private void startLoginActivity(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        RegisterActivity.this.startActivity(intent);
        RegisterActivity.this.finish();
    }

}

