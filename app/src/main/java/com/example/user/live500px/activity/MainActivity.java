package com.example.user.live500px.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import com.example.user.live500px.R;
import com.example.user.live500px.dao.PhotoDao;
import com.example.user.live500px.fragment.MainFragment;
import com.example.user.live500px.fragment.MoreInfoFragment;

public class MainActivity extends AppCompatActivity
        implements MainFragment.FragmentListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containContainer,
                            MainFragment.newInstance())
                    .commit();
        }
        initInstance();
    }

    private void initInstance() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout =(DrawerLayout) findViewById(R.id.DrawerLayout);
        actionBarDrawerToggle =new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,R.string.open_drawer,
                R.string.close_drawer

        );
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPhotoItemClicked(PhotoDao dao) {
        FrameLayout moreInfoContainer =(FrameLayout)
                findViewById(R.id.moreInfoContainer);

        if (moreInfoContainer == null){
            //mobile
            Intent intent = new Intent(MainActivity.this,
                    MoreInfoActivity.class);
            intent.putExtra("dao",dao);
            startActivity(intent);
        }else{
            //tablet
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.moreInfoContainer,
                            MoreInfoFragment.newInstance(dao))
                    .commit();

        }


    }
}
