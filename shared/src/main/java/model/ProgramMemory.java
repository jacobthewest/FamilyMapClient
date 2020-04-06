package model;

public class ProgramMemory {

    private boolean isLoggedIn;
    private User[] users;
    private Person[] persons;
    private Event[] events;


    public ProgramMemory() {}

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public User[] getUsers() {
        return this.users;
    }

    public Event[] getEvents() {
        return this.events;
    }

    public Person[] getPersons() {
        return this.persons;
    }


    public void setUsers(User[] users) {
        this.users = users;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
