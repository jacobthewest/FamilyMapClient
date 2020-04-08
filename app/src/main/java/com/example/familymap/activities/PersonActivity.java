package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.example.familymap.R;
import model.Person;
import model.ProgramMemory;

public class PersonActivity extends AppCompatActivity {

    public static final String EXTRA_PERSON_ID = "personID";
    private ProgramMemory programMemory = ProgramMemory.instance();
    private Person personInFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Family Map: Person Details");


        String personIdInFocus = getIntent().getExtras().getString(EXTRA_PERSON_ID);
        this.personInFocus = programMemory.getPersonById(personIdInFocus);
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
