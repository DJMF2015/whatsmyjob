package com.fulton.questionnaire.whatsmyjob;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoriesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoriesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesListFragment extends Fragment {

    private static final String CATEGORIES_LIST = "categoriesList";


    private ArrayList<String> categoriesList;
    private ArrayAdapter<String> categoriesArrayAdapter;
    private ListView categoriesListView;


    private OnFragmentInteractionListener mListener;

    public CategoriesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param categoriesList Parameter 1.
     * @return A new instance of fragment CategoriesListFragment.
     */

    public static CategoriesListFragment newInstance(ArrayList<String> categoriesList) {
        CategoriesListFragment fragment = new CategoriesListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(CATEGORIES_LIST, categoriesList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoriesList = getArguments().getStringArrayList(CATEGORIES_LIST);
            setHasOptionsMenu(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.categories_list, container, false);
        if (this.categoriesList.equals(null)) {
            return rootView;
        }
        this.categoriesListView = (ListView) rootView.findViewById(R.id.jobs);
        categoriesArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoriesList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);

                text1.setText(categoriesList.get(position).toString());
                setHasOptionsMenu(true);
                return view;
            }
        };
        categoriesListView.setAdapter(categoriesArrayAdapter);

        save();
        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void save() {

        String Str = "Top Work Environments\n " + categoriesList;

        try {
            File locationFile = new File("/sdcard/categories.txt");
            locationFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(locationFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(Str);
            myOutWriter.append('\n');
            myOutWriter.flush();
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {


        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.categories, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        item.isActionViewExpanded();
        switch (item.getItemId()) {
            case R.id.category:
                Intent intent = new Intent(getActivity(), CategoryList.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}