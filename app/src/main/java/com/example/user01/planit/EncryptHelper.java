package com.example.user01.planit;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Angel on 5/7/2016.
 */
public class EncryptHelper {

    public static String Encrypt(String w){
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(w.getBytes(),0,w.length());
            result = new BigInteger(1, md.digest()).toString();

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }

}
