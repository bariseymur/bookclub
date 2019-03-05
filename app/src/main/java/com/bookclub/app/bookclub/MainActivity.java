package com.bookclub.app.bookclub;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MatchListFragment.OnFragmentInteractionListener, GeneralListFragment.OnFragmentInteractionListener {

    private final Fragment generalListFragment = new GeneralListFragment();
    private final Fragment matchListFragement = new MatchListFragment();
    private FragmentManager fm = getSupportFragmentManager();
    Fragment active = generalListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragment_container, matchListFragement, "matchlistfragment").hide(matchListFragement).commit();
        fm.beginTransaction().add(R.id.fragment_container, generalListFragment, "generallistfragment").commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(generalListFragment).commit();
                    active = generalListFragment;
                    return true;
                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(active).show(matchListFragement).commit();
                    active = matchListFragement;
                    return true;
            }
            return false;
        }
    };

}
