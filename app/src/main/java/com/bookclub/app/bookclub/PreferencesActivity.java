package com.bookclub.app.bookclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class PreferencesActivity extends AppCompatActivity {

    LinearLayout accountSettings, wishList;

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

    }
}
