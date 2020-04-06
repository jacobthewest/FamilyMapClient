package com.example.familymap.tasks;

import android.os.AsyncTask;

import com.example.familymap.util.HttpClient;

import request.RegisterRequest;
import result.RegisterResult;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResult> {

    public interface Listener {
        void onRegisterComplete(RegisterResult registerResult);
    }

    private Listener listener;

    public RegisterTask(Listener listener) {this.listener = listener;}

    @Override
    protected RegisterResult doInBackground(RegisterRequest... registerRequests) {
        HttpClient httpClient = new HttpClient();
        return httpClient.register(registerRequests[0]);
    }

    @Override
    protected void onPostExecute(RegisterResult registerResult) {
        listener.onRegisterComplete(registerResult);
    }
}
