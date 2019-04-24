package com.bookclub.app.bookclub;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bookclub.app.bookclub.bookclubapi.BookClubAPI;

import dmax.dialog.SpotsDialog;

public class PreferencesActivity extends AppCompatActivity {

    LinearLayout accountSettings, wishList, tradeList, history, logout;
    ImageButton profileButton;
    private android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        accountSettings = findViewById(R.id.accountSettingsLayoutItem);
        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, AccountSettingsActivity.class);

                startActivity(intent);
            }
        });

        wishList = findViewById(R.id.wishListLayout);
        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, WishListActivity.class);
                startActivity(intent);
            }
        });

        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, ProfileActivity.class);
                intent.putExtra("UserID", 5);
                startActivity(intent);
            }
        });

        tradeList = findViewById(R.id.tradeListLayout);
        tradeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, TradeListActivity.class);
                intent.putExtra("UserID", 5);
                startActivity(intent);
            }
        });


        history = findViewById(R.id.historyLinearLayout);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferencesActivity.this, HistoryActivity.class);
                intent.putExtra("UserID", 5);
                startActivity(intent);
            }
        });

        alertDialog = new SpotsDialog(this);

        logout = findViewById(R.id.logoutLinearLayout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.show();
                new LogoutOperation().execute();

            }
        });


    }

    public class LogoutOperation extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            BookClubAPI api = new BookClubAPI();


            api.signout();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            alertDialog.dismiss();
            Intent intent = new Intent(PreferencesActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
    }

}
