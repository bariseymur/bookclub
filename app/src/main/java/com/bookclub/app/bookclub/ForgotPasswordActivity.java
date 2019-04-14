package com.bookclub.app.bookclub;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bookclub.app.bookclub.bookclubapi.BookClubAPI;

import java.util.ArrayList;

public class ForgotPasswordActivity extends AppCompatActivity {


    TextInputEditText username, email;
    ProgressBar progressBar;
    Button changePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        username = findViewById(R.id.usernameText);
        email = findViewById(R.id.emailText);
        progressBar = findViewById(R.id.progress);

        changePassword = findViewById(R.id.changePasswordButton);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username.setError(null);
                email.setError(null);


                if ((username.getText().toString() == null || username.getText().toString().equals(""))
                        && (email.getText().toString() == null || email.getText().toString().equals(""))){
                    username.requestFocus();
                    username.setError("Both email and username cannot be empty");

                    email.requestFocus();
                    email.setError("Both email and username cannot be empty");
                }
                else{
                    ForgotPasswordTask forgotPasswordTask = new ForgotPasswordTask();
                    forgotPasswordTask.execute();

                }

            }
        });

    }

    class ForgotPasswordTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress(true);


        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgress(false);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            BookClubAPI api = new BookClubAPI();
            ArrayList status = api.forgotPassword(username.getText().toString(), email.getText().toString());
            Log.d("forgot password attempt", status.toString());
            if (status.get(0).equals("success")){

                return true;
            }
            else return false;

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


}
