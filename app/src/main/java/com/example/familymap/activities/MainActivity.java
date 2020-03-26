package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.familymap.R;
import com.example.familymap.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null) {
            fragment = new LoginFragment();
            fm.beginTransaction()
            .add(R.id.fragmentContainer, fragment).commit();
        }
    }

    @Override
    public void onLoginComplete() {

    }
}
