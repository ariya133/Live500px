package com.example.user.live500px.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.live500px.R;
import com.example.user.live500px.dao.PhotoDao;
import com.example.user.live500px.fragment.MoreInfoFragment;

public class MoreInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        initInstance();
        PhotoDao dao = getIntent().getParcelableExtra("dao");
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containContainer,
                            MoreInfoFragment.newInstance(dao))
                    .commit();
        }
    }

    private void initInstance() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
