package com.bookclub.app.bookclub;

import android.app.AlertDialog;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bookclub.app.bookclub.bookclubapi.BookClubAPI;
import com.bookclub.app.bookclub.bookclubapi.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName, name, birthDay, country;
    User user;
    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.userNameText);
        name = findViewById(R.id.nameText);
        birthDay = findViewById(R.id.birthdayText);
        country = findViewById(R.id.country);
        alertDialog = new SpotsDialog(ProfileActivity.this);
        alertDialog.show();
        new ProfileInfoTask().execute();
    }


    public class ProfileInfoTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            alertDialog.dismiss();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            System.out.println("After call");

            BookClubAPI api = new BookClubAPI();
            ArrayList<Object> arr = api.seeOtherUserProfile("tarhanatnan");
            String status = (String)arr.get(0);
            String message = (String)arr.get(1);
            User user = (User)arr.get(2);
            userName.setText(user.getUsername());
            name.setText(user.getName());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            birthDay.setText(dateFormat.format(user.getDateOfBirth()));

            String city = SignupActivity.hereLocation(ProfileActivity.this, user.getLat(), user.getLon());
            if (city == null || city.equals("")){
                country.setText(city);
            }
            else{
                country.setText(user.getCountry());
            }
            System.out.println("Before return");

            return true;
        }
    }


}
