package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.sam_chordas.android.stockhawk.R;

public class DetailActivity extends AppCompatActivity {

    private static final String DETAIL_FRAGMENT_TAG = "14041979";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        String symbol = getIntent().getExtras().getString("symbol");

        getSupportActionBar().setTitle(symbol);

        DetailFragment fragment = DetailFragment.newInstance(symbol);

        getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, fragment, DETAIL_FRAGMENT_TAG).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
