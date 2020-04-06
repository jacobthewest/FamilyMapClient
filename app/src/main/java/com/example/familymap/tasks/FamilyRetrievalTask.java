package com.example.familymap.tasks;

import android.os.AsyncTask;

import com.example.familymap.util.HttpClient;
import result.PersonResult;

public class FamilyRetrievalTask extends AsyncTask<Void, Void, PersonResult> {

    public interface Listener {
        void onFamilyRetrievalComplete(PersonResult personResult);
    }

    private Listener listener;
    private String authToken;
    private String userName;

    public FamilyRetrievalTask(Listener listener, String authToken, String userName) {
        this.listener = listener;
        this.authToken = authToken;
        this.userName = userName;
    }

    @Override
    protected void onPostExecute(PersonResult personResult) {
        listener.onFamilyRetrievalComplete(personResult);
    }

    @Override
    protected PersonResult doInBackground(Void... voids) {
        HttpClient httpClient = new HttpClient();
        return httpClient.getFamily(authToken);
    }
}
