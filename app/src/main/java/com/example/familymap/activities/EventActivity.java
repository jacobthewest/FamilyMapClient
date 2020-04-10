package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.drm.DrmStore;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymap.R;
import com.example.familymap.fragments.LoginFragment;
import com.example.familymap.fragments.MapFragment;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.Map;

import model.Event;
import model.ProgramMemory;

public class EventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_ID = "eventID";
    private ProgramMemory programMemory = ProgramMemory.instance();
    private Event eventInFoucs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Family Map: Event Details");

        String eventIDInFocus = getIntent().getExtras().getString(EXTRA_EVENT_ID);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null) {
            fragment = new MapFragment(eventIDInFocus);
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
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

    public void setEventInFocus(String eventIDInFocus) {
        for(Event singleEvent: this.programMemory.getEvents()) {
            if(singleEvent.getEventID().equals(eventIDInFocus)) {
                this.eventInFoucs = singleEvent;
            }
        }
    }
}
