package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.familymap.R;
import com.example.familymap.fragments.LoginFragment;
import com.example.familymap.fragments.MapFragment;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import model.ProgramMemory;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgramMemory programMemory = ProgramMemory.instance();

    public SettingsActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Family Map: Settings");

        findViewById(R.id.logout).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        programMemory.setLoggedIn(false);
        finish();
    }
}
