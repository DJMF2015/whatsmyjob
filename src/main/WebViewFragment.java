package com.fulton.questionnaire.com;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {


    private static WebView web;

    private static final String JOBS_LIST = "topTenJobs";


    private ArrayList<String> topTenJobs;
    private WebView wv1;
    TextView text;
    WebViewFragment context;


    private static WebViewFragment instance = new WebViewFragment();


    public static WebViewFragment getInstance(WebViewFragment ctx) {


        return instance;
    }


    public WebViewFragment() {
        // Required empty public constructor


    }

    public static WebViewFragment newInstance(ArrayList<String> topTenJobs) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(JOBS_LIST, String.valueOf(topTenJobs));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if (getArguments() != null) {
            topTenJobs = getArguments().getStringArrayList(JOBS_LIST);
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            setHasOptionsMenu(true);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        wv1 = (WebView) rootView.findViewById(R.id.webView);
        text = (TextView) rootView.findViewById(R.id.textView1);
        // Inflate the layout for this fragment


       // return web.setWebViewClient(client);

        {



        }
        setHasOptionsMenu(true);
        setUpWebView();
        checkConnection(getContext());
        return rootView;
    }
    public boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
            //   Toast.makeText(context, " you are not connected", Toast.LENGTH_SHORT).show();
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi

                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan

                return true;
            }



        }
        Toast.makeText(context, "You are not connected! Have you checked your Connection?", Toast.LENGTH_LONG).show();
        return false;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        item.isActionViewExpanded();
        switch (item.getItemId()) {
            case R.id.indeed_back:
                wv1.loadUrl("http://www.indeed.co.uk");

                return true;
            case R.id.job_button:
                wv1.loadUrl("https://www.reed.co.uk/career-advice/career-guides");
                return true;


            //  return true;
            case R.id.national:
                wv1.loadUrl("https://nationalcareersservice.direct.gov.uk/Pages/Home.aspx");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

  ///  WebViewClient client;

    private void setUpWebView() throws NetworkOnMainThreadException {
        final WebSettings websettings = wv1.getSettings();
        String strtext = getArguments().getString("topTenJobs");

        String url = "http://"+JOBS_LIST+".indeed.co.uk";
        String response = "<html><body>Top Job set to <b>toptenjobs</b></body></html>";
        wv1.loadData(url, "text/html", "utf-8");
      wv1.loadUrl(url);
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.setVerticalScrollBarEnabled(true);
        websettings.setSupportZoom(true);
        websettings.setUseWideViewPort(true);
        websettings.setSaveFormData(true);
        websettings.setBuiltInZoomControls(true);
        websettings.setDisplayZoomControls(true);
        websettings.setLoadWithOverviewMode(true);
        //   wv1.loadUrl(url);


        class MyBrowser extends WebViewClient {

        }

        wv1.setWebViewClient(new MyBrowser());


        try {
           // web.loadUrl(url);
            wv1.loadUrl(url);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    BarChartFragment.OnFragmentInteractionListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (BarChartFragment.OnFragmentInteractionListener) context;
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


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    // @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

}

