package model;

public class ProgramMemory {

    private static ProgramMemory instance;

    public static ProgramMemory instance() {
        if(instance == null) {
            instance = new ProgramMemory();
        }
        return instance;
    }

    private boolean isLoggedIn;
    private Person selfPerson;
    private Person[] persons;
    private Event[] events;
    private String[] eventTypes;
    private String username;


    public ProgramMemory() {}

    public void loadLoginData(boolean isLoggedIn, Person selfPerson, Person[] persons,
                              Event[] events, String username) {
        this.isLoggedIn = isLoggedIn;
        this.selfPerson = selfPerson;
        this.persons = persons;
        this.events = events;
        this.username = username;
    }

    public Person getPersonById(String personID) {
        if(this.persons == null || this.persons.length == 0) { return null; }

        for(Person singlePerson: this.persons) {
            if(singlePerson.getPersonID().equals(personID)) return singlePerson;
        }
        return null;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Person getSelfPerson() {
        return selfPerson;
    }

    public void setSelfPerson(Person selfPerson) {
        this.selfPerson = selfPerson;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public void setEventTypes(String[] eventTypes) {
        this.eventTypes = eventTypes;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public String[] getEventTypes() {
        return this.eventTypes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
