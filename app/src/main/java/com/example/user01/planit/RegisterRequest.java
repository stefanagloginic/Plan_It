package com.example.user01.planit;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angel on 5/4/2016.
 */
public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = 	"http://planit7.comli.com/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String firstName,String lastName, String username, int age, String password, Response.Listener<String> listener, Response.ErrorListener err){
        super(Method.POST, REGISTER_REQUEST_URL, listener, err);
        params = new HashMap();
        params.put("firstname", firstName);
        params.put("lastname", lastName);
        params.put("username", username);
        params.put("password", EncryptHelper.Encrypt(password));
        params.put("age", Integer.toString(age));

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}