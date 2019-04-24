package com.bookclub.app.bookclub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bookclub.app.bookclub.bookclubapi.BookClubAPI;

import java.util.Date;

import dmax.dialog.SpotsDialog;

public class WelcomeActivity extends AppCompatActivity {

    Button loginButton, signInButton, guestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginButton = findViewById(R.id.loginButton);
        signInButton = findViewById(R.id.signInButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(WelcomeActivity.this, "Login Test", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        guestButton = findViewById(R.id.noLoginButton);
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Toast.makeText(WelcomeActivity.this, "Signin Test", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });





    }
}
