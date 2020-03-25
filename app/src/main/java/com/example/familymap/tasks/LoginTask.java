package com.example.familymap.tasks;

import android.os.AsyncTask;

import com.example.familymap.util.HttpClient;

import request.LoginRequest;
import result.LoginResult;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResult> {

    public interface Listener {

    }

    private Listener listener;

    public LoginTask(Listener listener) {this.listener = listener;}


    @Override
    protected LoginResult doInBackground(LoginRequest... loginRequests) {
        HttpClient httpClient = new HttpClient();
        return httpClient.login(loginRequests[0]);
    }
}
