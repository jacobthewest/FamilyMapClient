package com.example.familymap.fragments;

import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.familymap.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.lang.reflect.Array;

import model.Event;
import model.Person;
import model.ProgramMemory;


public class MapFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    private View view;
    private ProgramMemory programMemory = ProgramMemory.instance();

    public MapFragment() {
        // Required empty public constructor
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

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    public void setAllMarkers() {

        Event userBirth = getUsersBirth();

        Event[] events = programMemory.getEvents();
        for(Event singleEvent: events) {
            double lat = singleEvent.getLatitude();
            double lon = singleEvent.getLongitude();
            LatLng eventLocation = new LatLng(lat, lon);

            addMarker(eventLocation, singleEvent);

            if(singleEvent.equals(userBirth)) {
                focusOnUserBirth(eventLocation);
            }
        }

        // Set a listener for marker click.
        map.setOnMarkerClickListener(this);
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
        String eventType = singleEvent.getEventType();
        String[] eventTypes = programMemory.getEventTypes();

        int numOfColors = 10;


        boolean colorNumFound = false;
        int i = 0;
        int colorIndex = 0;

        while(!colorNumFound) {
            if(eventType.equals(eventTypes[i])) {
                colorIndex = i;
                colorNumFound = true;
            } else if (i >= numOfColors && eventType.equals(eventTypes[i])) {
                colorIndex = i % numOfColors;
                colorNumFound = true;
            }
            i++;
        }

        switch (colorIndex) {
            case 0:
                return BitmapDescriptorFactory.HUE_RED;
            case 1:
                return BitmapDescriptorFactory.HUE_BLUE;
            case 2:
                return BitmapDescriptorFactory.HUE_VIOLET;
            case 3:
                return BitmapDescriptorFactory.HUE_GREEN;
            case 4:
                return BitmapDescriptorFactory.HUE_MAGENTA;
            case 5:
                return BitmapDescriptorFactory.HUE_ORANGE;
            case 6:
                return BitmapDescriptorFactory.HUE_AZURE;
            case 7:
                return BitmapDescriptorFactory.HUE_ROSE;
            case 8:
                return BitmapDescriptorFactory.HUE_CYAN;
            default:
                return BitmapDescriptorFactory.HUE_YELLOW;
        }
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        //Integer eventIdAsInt = (Integer) marker.getTag();
        String eventId = marker.getTag().toString();

        Event clickedEvent = getEventByID(eventId);

        Toast.makeText(this.getActivity(), clickedEvent.getEventType(), Toast.LENGTH_SHORT).show();

//        // Check if a click count was set, then display the click count.
//        if (clickCount != null) {
//            clickCount = clickCount + 1;
//            marker.setTag(clickCount);
//            Toast.makeText(this.getActivity(),
//                    marker.getTitle() +
//                            " has been clicked " + clickCount + " times.",
//                    Toast.LENGTH_SHORT).show();
//        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
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

    public void focusOnUserBirth(LatLng eventLocation) {

        //map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(eventLocation).zoom(3).build();
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

//    public void markerClicked();

}
