package com.example.user01.planit;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angel on 5/31/2016.
 */
public class PhotoRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = 	"http://planit7.comli.com/addPhoto.php";
    private Map<String, String> params;

    public PhotoRequest(String username, Bitmap profilePicture, Response.Listener<String> listener, Response.ErrorListener err){
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, err);
        params = new HashMap();
        params.put("username", username);
        Log.i("username", username);
        Log.i("profilepic",LoginLogoutHelpers.encodeTobase64(profilePicture));
        params.put("profilepicture", LoginLogoutHelpers.encodeTobase64(profilePicture));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
