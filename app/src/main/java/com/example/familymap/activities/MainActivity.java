package com.example.familymap.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.PointerIcon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.familymap.R;
import com.example.familymap.fragments.LoginFragment;
import com.example.familymap.fragments.MapFragment;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import model.ProgramMemory;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    private FragmentManager fm;
    private Fragment fragment;
    private ProgramMemory programMemory = ProgramMemory.instance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Iconify.with(new FontAwesomeModule());

        setContentView(R.layout.activity_main);

        this.fm = getSupportFragmentManager();
        this.fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(this.fragment == null) {
            this.fragment = new LoginFragment(this);
            this.fm.beginTransaction()
            .add(R.id.fragmentContainer, this.fragment).commit();
        }
    }

    @Override
    public void terminateLoginFragment() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        this.fragment = new MapFragment();
        this.fm.beginTransaction()
                .add(R.id.fragmentContainer, this.fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.programMemory.isLoggedIn()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);

            MenuItem search = menu.findItem(R.id.search);
            MenuItem settings = menu.findItem(R.id.settings);

            search.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search).colorRes(R.color.white).actionBarSize());
            settings.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_gear).colorRes(R.color.white).actionBarSize());
        }

        return true;
    }
}
