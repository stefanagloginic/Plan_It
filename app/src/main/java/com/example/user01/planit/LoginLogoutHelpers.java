package com.example.user01.planit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Angel on 5/26/2016.
 */
public class LoginLogoutHelpers {
    public static int MAX_IMAGE_DIMENSION = 500;


     public static void saveUser(User user, Activity activity){
        //Creating a shared preference
        SharedPreferences mPrefs = activity.getBaseContext().getSharedPreferences("UserLoginData",0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("currUser", json);
        prefsEditor.putBoolean("loggedIn", true);
        prefsEditor.commit();
    }

    public static User retrieveUser(Activity activity){
        SharedPreferences mPrefs = activity.getBaseContext().getSharedPreferences("UserLoginData",0);
        Gson gson = new Gson();
        String json = mPrefs.getString("currUser", "");
        return gson.fromJson(json, User.class);
    }

    public static boolean retrieveLoggedIn(Activity activity){
        SharedPreferences mPrefs = activity.getBaseContext().getSharedPreferences("UserLoginData",0);
        return mPrefs.getBoolean("loggedIn", false);
    }


    public static void logUserOut(Activity activity){
        SharedPreferences mPrefs = activity.getBaseContext().getSharedPreferences("UserLoginData",0);
        mPrefs.edit().clear().commit();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean("loggedIn", false);
        prefsEditor.commit();
    }

    /*public static byte[] bitmapToByteArray(Bitmap b){
        Bitmap photo = b;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }

    public static Bitmap byteArrayToBitmap(byte[] data){
        Bitmap bmp;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        Canvas canvas = new Canvas(bmp);
        return bmp;
    }

    public static String byteArrayToBase64String(byte[] data){
        String encodedImage = Base64.encodeToString(data,Base64.DEFAULT);
        return encodedImage;
    }*/
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static Bitmap scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
