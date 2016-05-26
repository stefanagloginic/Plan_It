package com.example.user01.planit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {

    final private int REQUEST_READ_STORAGE = 2;
    final private int REQUEST_WRITE_STORAGE = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE );
        }else{
            if(LoginLogoutHelpers.retrieveLoggedIn(this)){
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                intent.putExtra("user", LoginLogoutHelpers.retrieveUser(this));

                SplashScreenActivity.this.startActivity(intent);
                SplashScreenActivity.this.finish();
            }else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_STORAGE:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
        }
    }



}
