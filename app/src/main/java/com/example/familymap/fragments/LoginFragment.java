package com.example.familymap.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.familymap.R;

public class LoginFragment extends Fragment {

    private TextView serverHostTextView;
    private TextView serverPortTextView;
    private TextView userNameTextView;
    private TextView passwordTextView;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;
    private TextView genderTextView;

    private EditText serverHostEditText;
    private TextView serverPortEditText;
    private TextView userNameEditText;
    private TextView passwordEditText;
    private TextView firstNameEditText;
    private TextView lastNameEditText;
    private TextView emailEditText;

    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    private Button signInButton;
    private Button registerButton;



    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        serverHostTextView = (TextView)v.findViewById(R.id.serverHostTextView);
        serverPortTextView = (TextView)v.findViewById(R.id.serverPortTextView);
        userNameTextView = (TextView)v.findViewById(R.id.userNameTextView);
        passwordTextView = (TextView)v.findViewById(R.id.passwordTextView);
        firstNameTextView = (TextView)v.findViewById(R.id.firstNameTextView);
        lastNameTextView = (TextView)v.findViewById(R.id.lastNameTextView);
        emailTextView = (TextView)v.findViewById(R.id.emailTextView);
        genderTextView = (TextView)v.findViewById(R.id.genderTextView);

        serverHostEditText = (EditText)v.findViewById(R.id.serverHostEditText);
        serverPortEditText = (EditText)v.findViewById(R.id.serverPortEditText);
        userNameEditText = (EditText)v.findViewById(R.id.userNameEditText);
        passwordEditText = (EditText)v.findViewById(R.id.passwordEditText);
        firstNameEditText = (EditText)v.findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText)v.findViewById(R.id.lastNameEditText);
        emailEditText = (EditText)v.findViewById(R.id.emailEditText);

        genderRadioGroup = (RadioGroup) v.findViewById(R.id.genderRadioGroup);
        maleRadioButton = (RadioButton)v.findViewById(R.id.maleRadioButton);
        femaleRadioButton = (RadioButton)v.findViewById(R.id.femaleRadioButton);

        signInButton = (Button)v.findViewById(R.id.signInButton);
        registerButton = (Button)v.findViewById(R.id.registerButton);

        return v;
    }
}
