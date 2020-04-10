package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.familymap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Family Map: Search");

        initializeViews();
        setListeners();
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

    public void initializeViews() {
        ImageView searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_search).colorRes(R.color.searchActivitySearchIcon).actionBarSize());

        ImageView deleteTextX = findViewById(R.id.deleteText);
        deleteTextX.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_times).colorRes(R.color.searchActivitySearchIcon).actionBarSize());
    }

    public void setListeners() {
        findViewById(R.id.deleteText).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText editText = findViewById(R.id.searchText);
        editText.setText("");
    }
}
