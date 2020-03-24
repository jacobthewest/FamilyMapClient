package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.familymap.R;
import com.example.familymap.tasks.LoginTask;

public class MainActivity extends AppCompatActivity implements LoginTask.Listener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
    }

    @Override
    public void onError(Error e) {

    }
}
