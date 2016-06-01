package com.example.user01.planit;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angel on 6/1/2016.
 */
public class GetPhotoRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = 	"http://planit7.comli.com/getProfilePicture.php";
    private Map<String, String> params;

    public GetPhotoRequest(String username,Response.Listener<String> listener, Response.ErrorListener err){
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, err);
        params = new HashMap();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
