package com.example.familymap.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymap.R;
import com.example.familymap.tasks.LoginTask;

import request.LoginRequest;

public class LoginFragment extends Fragment implements LoginTask.Listener {

    private TextView serverHostTextView;
    private TextView serverPortTextView;
    private TextView userNameTextView;
    private TextView passwordTextView;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView emailTextView;
    private TextView genderTextView;

    private EditText serverHostEditText;
    private EditText serverPortEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;

    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    private Button signInButton;
    private Button registerButton;


    public interface Listener {

    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        setStateVariables(v);
        setButtonsDisabled();
        setButtonListeners();
        setTextChangeListeners();
        setFocusChangeListeners();

        return v;
    }

    public void checkRegisterButton() {
        boolean canRegister = canPressRegisterButton();
        if(canRegister) {
            setRegisterButtonEnabled(true);
        } else {
            setRegisterButtonEnabled(false);
        }
    }


    public void checkSignInButton() {
        boolean canSignIn = canPressSignInButton();
        if(canSignIn) {
            setSignInButtonEnabled(true);
        } else {
            setSignInButtonEnabled(false);
        }
    }

    public void setFocusChangeListeners() {
        serverHostEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    checkRegisterButton();
                }
            }
        });

        serverPortEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    checkRegisterButton();
                }
            }
        });

        userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    checkSignInButton();
                    checkRegisterButton();
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    checkSignInButton();
                    checkRegisterButton();
                }
            }
        });

        firstNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    checkRegisterButton();
                }
            }
        });

        lastNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    checkRegisterButton();
                }
            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    checkRegisterButton();
                }
            }
        });

        genderRadioGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    checkRegisterButton();
                }
            }
        });


    }

    public void setTextChangeListeners() {
        serverHostEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { checkRegisterButton(); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        serverPortEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { checkRegisterButton(); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkSignInButton();
                checkRegisterButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkSignInButton();
                checkRegisterButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { checkRegisterButton(); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { checkRegisterButton(); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { checkRegisterButton(); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(canPressRegisterButton()) { setRegisterButtonEnabled(true); }
            }
        });
    }

    public boolean canPressSignInButton() {
        if(
            userNameEditText.getText().toString().isEmpty() ||
            passwordEditText.getText().toString().isEmpty()
           ) { return false; }
        return true;
    }

    public boolean canPressRegisterButton() {
        String a = serverHostEditText.getText().toString();
        String b = serverPortEditText.getText().toString();
        String c = firstNameEditText.getText().toString();
        String d = lastNameEditText.getText().toString();
        String e = emailEditText.getText().toString();
        int hey = genderRadioGroup.getCheckedRadioButtonId();

        if(
            serverHostEditText.getText().toString().isEmpty() ||
            serverPortEditText.getText().toString().isEmpty() ||
            !canPressSignInButton() ||
            firstNameEditText.getText().toString().isEmpty() ||
            lastNameEditText.getText().toString().isEmpty() ||
            emailEditText.getText().toString().isEmpty() ||
            genderRadioGroup.getCheckedRadioButtonId() == -1
        ) {
            return false;
        }
        return true;
    }

    public void setButtonsDisabled() {
        setSignInButtonEnabled(false);
        setRegisterButtonEnabled(false);
    }

    public void setSignInButtonEnabled(Boolean bool) {
        signInButton.setEnabled(bool);

    }
    public void setRegisterButtonEnabled(Boolean bool) {
        registerButton.setEnabled(bool);
    }

    public void setButtonListeners() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { signInButtonClicked(); }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { registerButtonClicked(); }
        });
    }

    public void setStateVariables(View v) {
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
    }

    public void signInButtonClicked() {
        try {
            Toast.makeText(
                    LoginFragment.this.getContext(), "Sign in button clicked", Toast.LENGTH_SHORT
            ).show();

            LoginRequest loginRequest;
            loginRequest = new LoginRequest(userNameEditText.getText().toString(), this.passwordEditText.getText().toString());

            new LoginTask(this).execute(loginRequest);

        } catch(Exception e) {
            Log.e("LoginFragment", e.getMessage(), e);
        }
    }

    public void registerButtonClicked() {
        try {
            Toast.makeText(
                    LoginFragment.this.getContext(), "Register button clicked", Toast.LENGTH_SHORT
            ).show();

//            LoginRequest loginRequest;
//            loginRequest = new LoginRequest(userNameEditText.getText().toString(), this.passwordEditText.getText().toString());
//
//            new LoginTask(this).execute(loginRequest);

        } catch(Exception e) {
            Log.e("LoginFragment", e.getMessage(), e);
        }
    }
}
