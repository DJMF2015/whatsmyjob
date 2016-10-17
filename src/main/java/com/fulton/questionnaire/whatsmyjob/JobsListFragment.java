package com.fulton.questionnaire.whatsmyjob;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
 * {@link JobsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobsListFragment extends DialogFragment {


    private static final String JOBS_LIST = "jobslist";
    Context cxt;

    private ArrayList<String> topTenJobs;
    private ArrayAdapter<String> jobsArrayAdapter;
    private ListView jobsView;
    private OnFragmentInteractionListener mListener;

    public JobsListFragment() {
        // Required empty public constructor
    }

    /*
     *
     * @param jobsList Parameter 1.
     * @return A new instance of fragment JobsListFragment.
     */

    public static JobsListFragment newInstance(ArrayList<String> jobsList) {
        JobsListFragment fragment = new JobsListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(JOBS_LIST, jobsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            topTenJobs = getArguments().getStringArrayList(JOBS_LIST);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_jobs_list, container, false);

        if (this.topTenJobs.equals(null)) {
            return rootView;
        }
        this.jobsView = (ListView) rootView.findViewById(R.id.jobs);
        jobsArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, topTenJobs) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);


                TextView text1 = (TextView) view.findViewById(android.R.id.text1);


                text1.setText(topTenJobs.get(position).toString());


                return view;
            }
        };

        jobsView.setAdapter(jobsArrayAdapter );
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public void save() {

        String Str = "Your Top Jobs:\n"+ topTenJobs;

        try {
            File locationFile = new File("/sdcard/topTenJobs.txt");
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
*/
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}







