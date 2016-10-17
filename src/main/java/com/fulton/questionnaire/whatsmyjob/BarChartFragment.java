package com.fulton.questionnaire.whatsmyjob;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BarChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BarChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarChartFragment extends Fragment {
    private static final String ANSWERS_HASH_MAP = "answersHashMap";
    Context context;

    HashMap<String, Integer> answersHashMap;
    private OnFragmentInteractionListener mListener;

    public BarChartFragment() {
        // Required empty public constructor
    }

    public static BarChartFragment newInstance(HashMap<String, Integer> answersHashMap) {
        BarChartFragment fragment = new BarChartFragment();
        Bundle args = new Bundle();
        args.putSerializable(String.valueOf(ANSWERS_HASH_MAP), answersHashMap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if (getArguments() != null) {
            answersHashMap = (HashMap<String, Integer>) getArguments().getSerializable(ANSWERS_HASH_MAP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        context = getContext();
        setHasOptionsMenu(true);
        final BarChart chart = (BarChart) rootView.findViewById(R.id.chart1);

        List<String> keys = getXAxisValues();
        BarData data = new BarData(keys, getDataSet(keys));

        Legend l = chart.getLegend();


        chart.setBackgroundColor(Color.WHITE);

        chart.setData(data);
      //  chart.setDescription("What's My Job?");
        chart.animateXY(2000, 2000);
        chart.setEnabled(false);
        chart.setMinimumWidth(200);
        chart.setMinimumHeight(100);
        chart.setFocusable(true);
        chart.setDrawValueAboveBar(true);
        chart.setSoundEffectsEnabled(true);
        chart.setDescriptionTextSize(8);

        chart.invalidate();


        return rootView;


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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    private BarDataSet getDataSet(List<String> keys) {
        List<Integer> values = new ArrayList<>();
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        for (String key : keys) {
            values.add(this.answersHashMap.get(key));
        }

        for (int i = 0; i < values.size(); i++) {
            BarEntry v = new BarEntry(values.get(i), i);
            valueSet1.add(v);


        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Job Categories");
        barDataSet1.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet1.setValueTextSize(10);


        return barDataSet1;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (String s : answersHashMap.keySet()) {
            xAxis.add(s);

        }
        return xAxis;

    }

}




    



