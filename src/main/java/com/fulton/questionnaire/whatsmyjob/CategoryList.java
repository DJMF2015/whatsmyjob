package com.fulton.questionnaire.whatsmyjob;

import java.util.ArrayList;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ExpandableListView;

public class CategoryList extends ExpandableListActivity{

    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ExpandableListView expandableList = getExpandableListView();

        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        setGroupParents();
        setChildData();

        ExpandableAdapter adapter = new ExpandableAdapter(parentItems, childItems);

        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        expandableList.setAdapter(adapter);
        expandableList.setOnChildClickListener(this);
    }

    public void setGroupParents() {
        parentItems.add("Inside");
        parentItems.add("Outside");
        parentItems.add("Physical");
        parentItems.add("Non-physical");
        parentItems.add("Creative");
        parentItems.add("Practical");
        parentItems.add("Team");
        parentItems.add("Independent");
        parentItems.add("People");
        parentItems.add("Equipment");
    }

    public void setChildData() {

        // inside
        ArrayList<String> child = new ArrayList<String>();
        child.add("would enjoy work that was inside");
        childItems.add(child);

        // outside
        child = new ArrayList<String>();
        child.add("would enjoy work that was outdoors");
        childItems.add(child);

        // physical
        child = new ArrayList<String>();
        child.add("Would suit work that was active and based outdoors");

        childItems.add(child);

        // Non-physical
        child = new ArrayList<String>();
        child.add("Would suit work that did not involve lots of physical activity such as Office work");
        childItems.add(child);


        // Creative
        child = new ArrayList<String>();
        child.add("Would enjoy work where they had some input and could use their own ideas");
        childItems.add(child);


        // non-Creative/Practical
        child = new ArrayList<String>();
        child.add("Would enjoy work where they knew what they had to do and could follow instructions");
        childItems.add(child);


        // Group/Team
        child = new ArrayList<String>();
        child.add("would enjoy working in a group");
        childItems.add(child);


        // independent
        child = new ArrayList<String>();
        child.add(" would enjoy working on their own or in small group");
        childItems.add(child);


        // people
        child = new ArrayList<String>();
        child.add("working alongside others");
        childItems.add(child);


        // Equipment
        child = new ArrayList<String>();
        child.add(" working with your hands and using tools");
        childItems.add(child);


}

}


