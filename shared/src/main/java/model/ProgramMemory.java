package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


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

    public List<Event> getLifeEventsByPersonId(String personIdInFocus) {
        List<Event> lifeEvents = new ArrayList<Event>();

        for(Event singleEvent: this.events) {
            if(singleEvent.getPersonID().equals(personIdInFocus)) {
                lifeEvents.add(singleEvent);
            }
        }

        return lifeEvents;
    }

    public String getFullNameByEventIdAndGender(String eventID, String gender) {

        ArrayList<Event> possibleEvents= new ArrayList<Event>();

        // Get Events by EventID
        for(Event singleEvent: this.events) {
            if(singleEvent.getEventID().equals(eventID)) {
                possibleEvents.add(singleEvent);
            }
        }

        Person correctPerson = persons[0];
        for(Person singlePerson: persons) {
            for(Event singleEvent: possibleEvents) {
                if(singlePerson.getPersonID().equals(singleEvent.getPersonID())) {
                    if(singlePerson.getGender().equals(gender)) {
                        correctPerson = singlePerson;
                    }
                }
            }
        }

        // Get Full name from Person
        return correctPerson.getFirstName() + " " + correctPerson.getLastName();
    }

    public float getMarkerColor(Event singleEvent) {
        String eventType = singleEvent.getEventType();

        int numOfColors = 10;

        boolean colorNumFound = false;
        int i = 0;
        int colorIndex = 0;

        while(!colorNumFound) {
            if(eventType.equals(this.eventTypes[i])) {
                colorIndex = i;
                colorNumFound = true;
            } else if (i >= numOfColors && eventType.equals(this.eventTypes[i])) {
                colorIndex = i % numOfColors;
                colorNumFound = true;
            }
            i++;
        }

        switch (colorIndex) {
            case 0:
                return (float) 0.0;
                        //BitmapDescriptorFactory.HUE_RED;
            case 1:
                return (float) 240.0;
                        //BitmapDescriptorFactory.HUE_BLUE;
            case 2:
                return (float) 270.0;
                        //BitmapDescriptorFactory.HUE_VIOLET;
            case 3:
                return (float) 120.0;
                        //BitmapDescriptorFactory.HUE_GREEN;
            case 4:
                return (float) 300.0;
                        //BitmapDescriptorFactory.HUE_MAGENTA;
            case 5:
                return (float) 30.0;
                        //BitmapDescriptorFactory.HUE_ORANGE;
            case 6:
                return (float) 210.0;
                        //BitmapDescriptorFactory.HUE_AZURE;
            case 7:
                return (float) 330.0;
                        //BitmapDescriptorFactory.HUE_ROSE;
            case 8:
                return (float) 180.0;
                        //BitmapDescriptorFactory.HUE_CYAN;
            default:
                return (float) 60.0;
                        //BitmapDescriptorFactory.HUE_YELLOW;
        }
    }

    public List<Person> getImmediateFamilyByPersonId(String personIdInFocus) {
        List<Person> returnMe = new ArrayList<Person>();

        Person personInFocus = getPersonById(personIdInFocus);

        for(Person singlePerson: this.persons) {
            addChildren(personIdInFocus, singlePerson, returnMe);
            addFather(personInFocus, singlePerson, returnMe);
            addMother(personInFocus, singlePerson, returnMe);
            addSpouse(personInFocus, singlePerson, returnMe);
        }

        returnMe = orderList(returnMe, personInFocus.getPersonID());

        return returnMe;
    }

    public List<Person> orderList(List<Person> orderMe, String selfID) {
        List<Person> orderedList = new ArrayList<Person>();

        // Add Father (if Exists)
        for(Person tempPerson: orderMe) {
            if(isFather(selfID, tempPerson.getPersonID())) {
                orderedList.add(tempPerson);
            }
        }

        // Add Mother (if Exists)
        for(Person tempPerson: orderMe) {
            if(isMother(selfID, tempPerson.getPersonID())) {
                orderedList.add(tempPerson);
            }
        }

        // Add Spouse (if Exists)
        for(Person tempPerson: orderMe) {
            if(isSpouse(selfID, tempPerson.getPersonID())) {
                orderedList.add(tempPerson);
            }
        }

        // Add Children (if Exists)
        for(Person tempPerson: orderMe) {
            if(isChild(selfID, tempPerson.getPersonID())) {
                orderedList.add(tempPerson);
            }
        }

        return orderedList;
    }

    public void addChildren(String personIdInFocus, Person singlePerson, List<Person> returnMe) {

        if(singlePerson.getMotherId() != null) {
            if(singlePerson.getMotherId().equals(personIdInFocus)) {
                returnMe.add(singlePerson);
            }
        }

        if(singlePerson.getFatherId() != null) {
            if(singlePerson.getFatherId().equals(personIdInFocus)) {
                returnMe.add(singlePerson);

            }
        }
    }

    public void addFather(Person personInFocus, Person singlePerson, List<Person> returnMe) {

        if(personInFocus.getFatherId() != null) {
            if(personInFocus.getFatherId().equals(singlePerson.getPersonID())) {
                returnMe.add(singlePerson);
            }
        }
    }

    public void addMother(Person personInFocus, Person singlePerson, List<Person> returnMe) {

        if(personInFocus.getMotherId() != null) {
            if(personInFocus.getMotherId().equals(singlePerson.getPersonID())) {
                returnMe.add(singlePerson);
            }
        }
    }

    public void addSpouse(Person personInFocus, Person singlePerson, List<Person> returnMe) {

        if(personInFocus.getSpouseId() != null) {
            if(personInFocus.getSpouseId().equals(singlePerson.getPersonID())) {
                returnMe.add(singlePerson);
            }
        }
    }

    public String getRelationship(String familyMemberPersonID, String personInFocusID) {
        for(Person singlePerson: this.persons) {
            if(singlePerson.getPersonID().equals(familyMemberPersonID)) {
                if(isMother(personInFocusID, singlePerson.getPersonID())) {
                    return "Mother";
                } else if (isFather(personInFocusID, singlePerson.getPersonID())) {
                    return "Father";
                } else if (isChild(personInFocusID, singlePerson.getPersonID())) {
                    return "Child";
                } else if (isSpouse(personInFocusID, singlePerson.getPersonID())) {
                    return "Spouse";
                }
            }
        }
        return "ERROR";
    }

    public boolean isMother(String selfID, String maybeMotherID) {
        Person self = getPersonById(selfID);
        if(self.getMotherId() != null) {
            if(maybeMotherID.equals(self.getMotherId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isFather(String selfID, String maybeFatherID) {
        Person self = getPersonById(selfID);
        if(self.getFatherId() != null) {
            if(maybeFatherID.equals(self.getFatherId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isChild(String selfID, String maybeChildID) {
        Person child = getPersonById(maybeChildID);
        if(child.getMotherId() != null) {
            if(selfID.equals(child.getMotherId())) {
                return true;
            }
        }

        if(child.getFatherId() != null) {
            if(selfID.equals(child.getFatherId())) {
                return true;
            }
        }

        return false;
    }

    public boolean isSpouse(String selfID, String maybeSpouseID) {
        Person self = getPersonById(selfID);
        if(self.getSpouseId() != null) {
            if(maybeSpouseID.equals(self.getSpouseId())) {
                return true;
            }
        }
        return false;
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
