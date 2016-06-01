package com.example.user01.planit;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Angel on 5/7/2016.
 */
public class User implements  Parcelable{
    private int userID;
    private String username;
    private String firstName;
    private String password;
    private int age;
    private String lastName;
    private String profilePicture;


    public User(String username, String firstName,String lastName, String password, int age){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.age = age;
    }

    public User(JSONObject o) throws JSONException{
        this.firstName = o.getString("firstname");
        this.age = o.getInt("age");
        this.userID = o.getInt("userid");
        this.password = o.getString("password");
        this.username = o.getString("username");
        this.lastName = o.getString("lastname");
        //this.profilePicture = o.getString("profilepicture");
    }

    public User(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        this.username = data[0];
        this.firstName = data[1];
        this.password = data[2];
        this.userID = Integer.parseInt(data[3]);
        this.age = Integer.parseInt(data[4]);
        this.lastName = data[5];
        //this.profilePicture = data[6];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.username,
                this.firstName,
                this.password,
                String.valueOf(this.userID),
                String.valueOf(this.age),
                this.lastName,
                //this.profilePicture
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername(){
        return username;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getPassword(){
        return password;
    }

    public int getAge(){
        return age;
    }

    public int getUserID(){
        return userID;
    }

    public String getLastName(){
        return lastName;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setProfilePicture(String profilePicture) {this.profilePicture = profilePicture; }
}
