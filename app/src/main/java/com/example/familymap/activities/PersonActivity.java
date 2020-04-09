package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.familymap.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import model.Event;
import model.Person;
import model.ProgramMemory;

public class PersonActivity extends AppCompatActivity {

    public static final String EXTRA_PERSON_ID = "personID";
    private ProgramMemory programMemory = ProgramMemory.instance();
    private Person personInFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Family Map: Person Details");


        String personIdInFocus = getIntent().getExtras().getString(EXTRA_PERSON_ID);
        this.personInFocus = programMemory.getPersonById(personIdInFocus);

        initViews();

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        List<Event> lifeEvents = getLifeEventsByPersonID(personIdInFocus);
        List<Person> family = getFamilyByPersonID(personIdInFocus);

        expandableListView.setAdapter(new ExpandableListAdapter(this.personInFocus, lifeEvents, family, this));
    }

    public List<Person> getFamilyByPersonID(String personIdInFocus) {
        List<Person> family = programMemory.getImmediateFamilyByPersonId(personIdInFocus);
        return family;
    }

    public List<Event> getLifeEventsByPersonID(String personIdInFocus) {
        List<Event> lifeEvents = programMemory.getLifeEventsByPersonId(personIdInFocus);
        return lifeEvents;
    }

    public void initViews() {

        TextView firstNameValue = findViewById(R.id.personDetailsFirstNameValue);
        TextView firstNameLabel = findViewById(R.id.personDetailsFirstNameLabel);
        TextView lastNameValue = findViewById(R.id.personDetailsLastNameValue);
        TextView lastNameLabel = findViewById(R.id.personDetailsLastNameLabel);
        TextView genderLabel = findViewById(R.id.genderLabel);
        TextView genderValue = findViewById(R.id.genderValue);


        firstNameValue.setText(this.personInFocus.getFirstName());
        firstNameLabel.setText("First Name");
        lastNameValue.setText(this.personInFocus.getLastName());
        lastNameLabel.setText("Last Name");
        genderLabel.setText("Gender");

        if(this.personInFocus.getGender().equals("m")) {
            genderValue.setText("Male");
        } else {
            genderValue.setText("Female");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int LIFE_EVENTS_GROUP_POSITION = 0;
        private static final int FAMILY_GROUP_POSITION = 1;

        private final List<Event> lifeEvents;
        private final List<Person> family;
        private final PersonActivity personActivity;
        private final Person personInFocus;

        ExpandableListAdapter(Person personInFocus, List<Event> lifeEvents, List<Person> family, PersonActivity personActivity) {
            this.lifeEvents = lifeEvents;
            this.family = family;
            this.personActivity = personActivity;
            this.personInFocus = personInFocus;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return lifeEvents.size();
                case FAMILY_GROUP_POSITION:
                    return family.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return getString(R.string.lifeEventsTitle);
                case FAMILY_GROUP_POSITION:
                    return getString(R.string.familyTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return lifeEvents.get(childPosition);
                case FAMILY_GROUP_POSITION:
                    return family.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    titleView.setText(R.string.lifeEventsTitle);
                    break;
                case FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.familyTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.life_event_item, parent, false);
                    initializeLifeEventsView(itemView, childPosition);
                    break;
                case FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.family_item, parent, false);
                    initializeFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeLifeEventsView(View lifeEventView, final int childPosition) {

            ImageView markerView = lifeEventView.findViewById(R.id.lifeEventMarker);

            Drawable markerIcon = getMarkerIcon(childPosition);
            markerView.setImageDrawable(markerIcon);

            TextView lifeEventDetails = lifeEventView.findViewById(R.id.lifeEventDetails);
            String eventType = lifeEvents.get(childPosition).getEventType().toUpperCase();
            String eventCity = lifeEvents.get(childPosition).getCity();
            String eventCountry = lifeEvents.get(childPosition).getCountry();
            String eventYear = Integer.toString(lifeEvents.get(childPosition).getYear());
            lifeEventDetails.setText(eventType + ": " + eventCity + ", " + eventCountry +
                    " (" + eventYear + ")");

            TextView lifeEventID = lifeEventView.findViewById(R.id.lifeEventID);
            String eventID = lifeEvents.get(childPosition).getEventID();
            lifeEventID.setTag(eventID);

            TextView lifeEventName = lifeEventView.findViewById(R.id.lifeEventName);
            String fullName = programMemory.getFullNameByEventIdAndGender(eventID, personInFocus.getGender());
            lifeEventName.setText(fullName);

            lifeEventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(PersonActivity.this, "Going to Event View Alright?", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void initializeFamilyView(View familyView, final int childPosition) {

            ImageView genderView = familyView.findViewById(R.id.familyMemberGender);

            Drawable genderIcon = getGenderIcon(childPosition);
            genderView.setImageDrawable(genderIcon);

            TextView familyMemberName = familyView.findViewById(R.id.familyMemberFullName);
            familyMemberName.setText(family.get(childPosition).getFirstName() + " " + family.get(childPosition).getLastName());

            TextView familyMemberPersonID = familyView.findViewById(R.id.familyMemberPersonID);
            familyMemberPersonID.setTag(family.get(childPosition).getPersonID());

            TextView familyMemberRelation = familyView.findViewById(R.id.familyMemberRelation);
            String relationship = programMemory.getRelationship(family.get(childPosition).getPersonID(), this.personInFocus.getPersonID());
            familyMemberRelation.setText(relationship);

            familyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this ,PersonActivity.class);

                    String personID = (String) v.findViewById(R.id.familyMemberPersonID).getTag();

                    intent.putExtra(PersonActivity.EXTRA_PERSON_ID, personID);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }

        public Drawable getMarkerIcon(final int childPosition) {

            Event eventSelected = this.lifeEvents.get(childPosition);
            int googleMapsMarkerColor = (int)programMemory.getMarkerColor(eventSelected);

            int markerDp = 50;

            switch(googleMapsMarkerColor) {
                case 0:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.red).sizeDp(markerDp);
                    //BitmapDescriptorFactory.HUE_RED;
                case 240:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.blue).sizeDp(markerDp);
                    //BitmapDescriptorFactory.HUE_BLUE;
                case 270:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.violet).sizeDp(markerDp);
                //BitmapDescriptorFactory.HUE_VIOLET;
                case 120:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.green).sizeDp(markerDp);
                //BitmapDescriptorFactory.HUE_GREEN;
                case 300:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.magenta).sizeDp(markerDp);
                //BitmapDescriptorFactory.HUE_MAGENTA;
                case 30:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.orange).sizeDp(markerDp);
                //BitmapDescriptorFactory.HUE_ORANGE;
                case 210:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.azure).sizeDp(markerDp);
                //BitmapDescriptorFactory.HUE_AZURE;
                case 330:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.rose).sizeDp(markerDp);
                //BitmapDescriptorFactory.HUE_ROSE;
                case 180:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.cyan).sizeDp(markerDp);
                //BitmapDescriptorFactory.HUE_CYAN;
                default:
                    return new IconDrawable(this.personActivity,
                            FontAwesomeIcons.fa_map_marker).colorRes(R.color.yellow).sizeDp(markerDp);
                //BitmapDescriptorFactory.HUE_YELLOW;
            }
        }

        public Drawable getGenderIcon(final int childPosition) {

            Drawable genderIcon;

            int genderDpSize = 50;

            if(family.get(childPosition).getGender().equals("m")) {
                genderIcon = new IconDrawable(this.personActivity,
                        FontAwesomeIcons.fa_male).colorRes(R.color.male_icon).sizeDp(genderDpSize);
            } else {
                genderIcon = new IconDrawable(this.personActivity,
                        FontAwesomeIcons.fa_female).colorRes(R.color.female_icon).sizeDp(genderDpSize);
            }

            return genderIcon;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}

