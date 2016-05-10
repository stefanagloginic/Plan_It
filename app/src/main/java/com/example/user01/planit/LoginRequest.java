package com.example.user01.planit;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class LoginRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = 	"http://planit7.comli.com/Login.php";
    private Map<String, String> params;

    public LoginRequest(String username, String password, Response.Listener<String> listener, Response.ErrorListener err){
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, err);
        params = new HashMap();
        params.put("username", username);
        params.put("password", EncryptHelper.Encrypt(password));

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

