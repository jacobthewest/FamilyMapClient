package com.example.familymap.tasks;

import android.os.AsyncTask;

import com.example.familymap.util.HttpClient;

import java.util.ArrayList;

import model.Event;
import model.Person;
import model.ProgramMemory;

public class GetDataTask extends AsyncTask<Void, Void, Void> {


    public interface Listener {
        void onGetDataComplete();
    }

    private Listener listener;
    private String authToken;
    private String username;
    ProgramMemory programMemory = ProgramMemory.instance();


    public GetDataTask(Listener listener, String authToken, String username) {
        this.listener = listener;
        this.authToken = authToken;
        this.username = username;
    }

    @Override
    protected void onPostExecute(Void v) {
        listener.onGetDataComplete();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        HttpClient httpClient = new HttpClient();
        Person[] persons = httpClient.getFamily(authToken).getData();
        Event[] events = httpClient.getEvents(authToken).getData();
        Person selfPerson = getSelfPerson(persons);

        this.programMemory.loadLoginData(true, selfPerson, persons, events, username);

        String[] eventTypes = getEventTypes();
        this.programMemory.setEventTypes(eventTypes);

        return null;
    }

    public String[] getEventTypes() {
        Event[] events = this.programMemory.getEvents();

        ArrayList<String> eventTypes = new ArrayList<String>();

        // Add all unique eventTypes to the eventTypes ArrayList
        for(Event singleEvent: events) {
            if(eventTypes.size() == 0) {
                eventTypes.add(singleEvent.getEventType());
            } else {
                if(!eventTypes.contains(singleEvent.getEventType())) {
                    eventTypes.add(singleEvent.getEventType());
                }
            }
        }

        return eventTypes.toArray(new String[0]); // Weird parameter needed for cast
    }

    public Person getSelfPerson(Person[] persons) {

        for(Person singlePerson: persons) {
            if(singlePerson.getAssociatedUsername().equals(this.username)) {
                return singlePerson;
            }
        }
        return null;
    }
}
