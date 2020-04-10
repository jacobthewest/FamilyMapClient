package com.example.familymap.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.familymap.R;
import com.example.familymap.activities.MainActivity;
import com.example.familymap.activities.PersonActivity;
import com.example.familymap.activities.SettingsActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;


import model.Event;
import model.Person;
import model.ProgramMemory;


public class MapFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMarkerClickListener, View.OnClickListener {
    private GoogleMap map;
    private View view;
    private ProgramMemory programMemory = ProgramMemory.instance();
    private Person personInFocus;
    private Event eventInFocus;

    public MapFragment() {
        // Required empty public constructor
        this.eventInFocus = null;
    }

    public MapFragment(String eventIdInFocus) {
        this.eventInFocus = getEventByID(eventIdInFocus);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        this.view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        initializeViews();
        setListeners();

        if(this.eventInFocus != null) {
            setEventViews();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    public void setListeners() {
        // Set a listener for marker click

        LinearLayout linearLayout = this.view.findViewById(R.id.personDetailsOnMap);
        linearLayout.setOnClickListener(this);
    }

    public void setAllMarkers() {

        Event userBirth = getUsersBirth();

        Event[] events = programMemory.getEvents();
        for(Event singleEvent: events) {
            double lat = singleEvent.getLatitude();
            double lon = singleEvent.getLongitude();
            LatLng eventLocation = new LatLng(lat, lon);

            addMarker(eventLocation, singleEvent);

            if(this.eventInFocus == null) {
                if(singleEvent.equals(userBirth)) {
                    focusOnEvent(eventLocation);
                }
            } else {
                if (singleEvent.getEventID().equals(this.eventInFocus.getEventID())) {
                    focusOnEvent(eventLocation);
                }
            }
        }
    }

    public void addMarker(LatLng eventLocation, Event singleEvent) {

        float markerColor = getMarkerColor(singleEvent);


        Marker tempMarker = map.addMarker(
                new MarkerOptions()
                        .position(eventLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
        );


        //tempMarker.setIcon(BitmapDescriptorFactory.defaultMarker(markerColor));
        tempMarker.setTag(singleEvent.getEventID());

    }

    public float getMarkerColor(Event singleEvent) {
        return programMemory.getMarkerColor(singleEvent);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        //Integer eventIdAsInt = (Integer) marker.getTag();
        String eventId = marker.getTag().toString();

        setMarkerInFocus(eventId);

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    public void setEventViews() {
        setMarkerInFocus(this.eventInFocus.getEventID());
    }

    public void setMarkerInFocus(String eventId) {
        Event clickedEvent = getEventByID(eventId);
        Person tempPerson = getPersonByPersonID(clickedEvent.getPersonID());

        this.eventInFocus = clickedEvent;
        this.personInFocus = tempPerson;

        Drawable genderIcon;
        if(tempPerson.getGender().equals("m")) {
            genderIcon = new IconDrawable(getActivity(),
                    FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(5);
        } else {
            genderIcon = new IconDrawable(getActivity(),
                    FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(5);
        }

        ImageView iv = this.view.findViewById(R.id.genderIcon);
        iv.setImageDrawable(genderIcon);

        TextView textView1 = this.view.findViewById(R.id.eventPersonName);
        textView1.setText(tempPerson.getFirstName() + " " + tempPerson.getLastName());

        TextView textView2 = this.view.findViewById(R.id.event_details);
        textView2.setText(
                clickedEvent.getEventType().toUpperCase() + ": "
                        + clickedEvent.getCity() + ", " + clickedEvent.getCountry()
                        + " (" + clickedEvent.getYear() + ")"
        );
    }

    public Person getPersonByPersonID(String personID) {
        for(Person singlePerson: programMemory.getPersons()) {
            if(singlePerson.getPersonID().equals(personID)) {
                return singlePerson;
            }
        }
        return null;
    }

    public Event getEventByID(String eventID) {
        for(Event singleEvent: programMemory.getEvents()) {
            if(singleEvent.getEventID().equals(eventID)) {
                return singleEvent;
            }
        }
        return null;
    }

    public Event getUsersBirth() {
        Event[] events = programMemory.getEvents();
        Person selfPerson = programMemory.getSelfPerson();

        for(Event singleEvent: events) {
            if(singleEvent.getPersonID().equals(selfPerson.getPersonID()) &&
                    singleEvent.getEventType().equals("Birth")) {
                    return singleEvent;
            }
        }
        return null;
    }

    public void focusOnEvent(LatLng eventLocation) {

        //map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
        CameraPosition cameraPosition;
        if(this.eventInFocus == null) {
            cameraPosition = new CameraPosition.Builder().target(eventLocation).zoom(3).build();
        } else {
            cameraPosition = new CameraPosition.Builder().target(eventLocation).zoom(14).build();
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        //map.moveCamera(cameraUpdate);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        map.setOnMarkerClickListener(this);
    }

    private void initializeViews() {
//        Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(5);
//        ImageView iv = this.view.findViewById(R.id.genderIcon);
//        iv.setImageDrawable(genderIcon);

        Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).colorRes(R.color.android_green).sizeDp(5);
        ImageView iv = this.view.findViewById(R.id.genderIcon);
        iv.setImageDrawable(genderIcon);
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
        setAllMarkers();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PersonActivity.class);

        String personID = personInFocus.getPersonID();

        intent.putExtra(PersonActivity.EXTRA_PERSON_ID, personID);
        startActivity(intent);
    }
}
