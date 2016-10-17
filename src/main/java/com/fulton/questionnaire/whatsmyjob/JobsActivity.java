package com.fulton.questionnaire.whatsmyjob;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class JobsActivity extends AppCompatActivity implements JobsListFragment.OnFragmentInteractionListener, BarChartFragment.OnFragmentInteractionListener, WebViewFragment.OnFragmentInteractionListener, CategoriesListFragment.OnFragmentInteractionListener {
    final Context context = getBaseContext();
    private boolean exit = false;

    //to avoid exception indexOutofBounds occuring!
    public static <T> List<T> safeSubList(ArrayList<T> list, int fromIndex, int toIndex) {
        int size = list.size();
        if (fromIndex >= size || toIndex <= 0 || fromIndex >= toIndex) { //check if empty and if so return empty list
            return Collections.emptyList();
        }

        fromIndex = Math.max(0, fromIndex);
        toIndex = Math.min(size, toIndex);

        return list.subList(fromIndex, toIndex);//return list range
    }

    @Override
    public void onBackPressed() {
        //alertdialog button for ensuring user wishes to exit back to main screen
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Main Menu")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(JobsActivity.this, QuestActivity.class);//launch intent to return to main screen
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No!", null).show();
    }


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbHelper dbHelper = new DbHelper(this);
        setContentView(R.layout.activity_jobs);

        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.get("key") != null) {
                //get key from intent
                final int quiz_key = extras.getInt("key");

                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), quiz_key, dbHelper);

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(mViewPager);

                //get attempted key from database using the key
                return;
            }
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private DbHelper dbHelper;
        private ArrayList<String> topTenJobs, categoriesList;
        private HashMap<String, Integer> barChartAnswersMap;

        public SectionsPagerAdapter(FragmentManager fm, int q_key, DbHelper dbHelper) {
            super(fm);
            this.dbHelper = dbHelper;
            init(q_key);

        }

        private void init(int quest_key) {
            final Questionnaire questionnaire = dbHelper.getQuest_Answer(quest_key);
            //get attempts from the questionnaire
            final List<Attempt> attemptList = dbHelper.getAllAttemptsInQuestionnaire(questionnaire.getId());
            final List<String> allAnswerTypes = new ArrayList<>();
            //get all the types from the answers in a list
            //ex list = ["INSIDE 1","INSIDE 2","OUTSIDE 1","PRODUCT 1"];
            for (Attempt attempt : attemptList) {
                List<String> answerType = dbHelper.getQuestion(attempt.getQuestionId()).match(attempt.getAnswer()).types;
                //get all answer types
                allAnswerTypes.addAll(answerType);
            }

            //=============================================================
            //BUILD CATEGORIES AKA ANSWERS TYPES MAP
            //this hash map will contain the scores for each answer type
            //ex {INSIDE : 3, OUTSIDE : 1, PRODUCT : 2}

            final HashMap<String, Integer> answerTypesMap = new LinkedHashMap<>();
            for (String allType : allAnswerTypes) {
                String[] typeAndScore = allType.split(" ");
                Integer currentScore = answerTypesMap.get(typeAndScore[0]);
                if (currentScore == null) {
                    currentScore = 0;
                }
                answerTypesMap.put(typeAndScore[0], currentScore + Integer.valueOf(typeAndScore[1]));
            }
            //=============================================================


            //=============================================================

            try {
                //=============================================================
                //Fetch json JOBS object from json file
                JSONObject jobsJsonRaw = new JSONObject(dbHelper.loadJSONFromAsset("jobs"));
                JSONObject jobsJson = jobsJsonRaw.getJSONObject("jobs");
                //=============================================================

                //Fetch opposite categories
                JSONObject oppositesJson = jobsJsonRaw.getJSONObject("opposites");
                HashMap<String, String> opposites = new LinkedHashMap<>();
                Iterator<?> oppositeKeys = oppositesJson.keys();
                while (oppositeKeys.hasNext()) {
                    String key = (String) oppositeKeys.next();
                    opposites.put(key, oppositesJson.getString(key));
                }
                //=============================================================

                //This gives all the categories which are present in the jobs file
                //Note this has ALL the categories, whereas allAnswerTypes contains only the subset of the categories
                //The subset is the categories that the user belongs to based on the answers submitted
                Iterator<?> keys = jobsJson.keys();
                //=============================================================
                //barChartAnswersMap is a map for all the categories, even the ones that the user did not select
                // use all categories for the barchart
                barChartAnswersMap = new LinkedHashMap<>(answerTypesMap);
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    //if category does not exist set it to null
                    if (barChartAnswersMap.get(key) == null) {
                        barChartAnswersMap.put(key, 0);
                    }
                }

                //Only show 5 categories whichever the samller
                for (String s : opposites.keySet()) {
                    String category = s;
                    String opp = opposites.get(s);
                    if (barChartAnswersMap.get(category) >= barChartAnswersMap.get(opp)) {
                        barChartAnswersMap.remove(opp);
                    } else {
                        barChartAnswersMap.remove(category);
                    }
                }

                //=============================================================
                //This is a list to use for our Categories List fragment
                //INSIDE -3
                categoriesList = new ArrayList<>();
                //get all the categories from the barChartAnswersMap keySet
                ArrayList<String> categoriesKeySet = new ArrayList(barChartAnswersMap.keySet());
                //Sort them alphabetically
                Collections.sort(categoriesKeySet);
                //For each category, get the score from barChartAnswerMap value
                for (String s : categoriesKeySet) {
                    categoriesList.add(s + "  : " + barChartAnswersMap.get(s));
                }


                //=============================================================
                //  jobs map
                final HashMap<String, Integer> jobsMap = new LinkedHashMap<>();
                //Get all the jobTypes which the user belongs to
                List<String> jobTypes = new ArrayList(answerTypesMap.keySet());
                //For each jobType (inside:outside...)
                for (String jobType : jobTypes) {
                    //read jobs based on the answers, only those categories which were selected
                    try {
                        //get all the jobs that are present in the json file for the current category
                        JSONArray jobsInType = jobsJson.getJSONArray(jobType);
                        for (int i = 0; i < jobsInType.length(); i++) {//jobsInType - baker,barber, hairdresser,make-up-person...

                            //For each job in the current category
                            String job = jobsInType.get(i).toString();
                            //get jobsMap value of the job
                            Integer jobWeight = jobsMap.get(job);
                            System.out.println(job);
                            if (jobWeight == null) {
                                //initially they are all null, and if so, initialize their value as 0
                                jobWeight = 0;

                            }
                            //answerTypesMap has a weight for each category
                            //INSIDE 3, OUTSIDE 2 etc
                            //Add the same weight for inside jobs
                            //hence an inside job's weight is incremented by 3 and outside's by 2
                            //the reason is to give more emphasis to inside jobs than outside jobs as
                            // the users answers direct us to do
                            jobWeight += answerTypesMap.get(jobType);
                            //put the value in to the jobmap
                            //so that next time this job appears in a different category, the weight can be incremented
                            jobsMap.put(job, jobWeight);

                            //Testing Stub only: print out values.
                            for (Object o : answerTypesMap.entrySet()) {
                                Map.Entry me2 = (Map.Entry) o;
                                System.out.println("Key: " + me2.getKey() + " & Value: " + me2.getValue());
                                System.out.println("job" + job);
                                System.out.println("score" + jobWeight);

                            }
                            // Converting HashMap keys into ArrayList
                            List<String> keyList = new ArrayList<String>(answerTypesMap.keySet());
                            //testing code printout
                            System.out.println("\n==> Size of Key list: " + keyList.size());

                            for (String temp : keyList) {
                                System.out.println(temp);
                            }

                            // Converting HashMap Values into ArrayList
                            List<Integer> valueList = new ArrayList<Integer>(answerTypesMap.values());
                            //testing code
                            System.out.println("\n==> Size of Value list: " + valueList.size());
                            for (Integer temp : valueList) {
                                System.out.println(temp);
                            }
                            List<Map.Entry> entryList = new ArrayList<Map.Entry>(answerTypesMap.entrySet());
                            //testing code
                            System.out.println("\n==> Size of Entry list: " + entryList.size());
                            for (Map.Entry temp : entryList) {
                                System.out.println(temp);
                            }

                        }
                    } catch (Exception e) {
                        continue;
                    }

                }

                //Get jobs by weights
                final ArrayList<String> weightedJobs = new ArrayList<String>(jobsMap.keySet());
                //testing print line
                System.out.println("\n==> Size of Key list: " + jobsMap.size() + weightedJobs.size() +
                        "\n ==>" + jobsMap.values());

                for (String temp : weightedJobs) {
                    System.out.println(temp);
                }
                // Converting HashMap Values into ArrayList
                List<Map.Entry> entryList = new ArrayList<Map.Entry>(jobsMap.entrySet());
                //testing print line
                System.out.println("\n==> Size of Entry list: " + entryList.size());
                for (Map.Entry temp : entryList) {
                    System.out.println(temp);
                }


                //sort them in the order of decreasing weights
                Collections.sort(weightedJobs, new Comparator<String>() {
                    @Override
                    public int compare(String job1, String job2) {

                        return jobsMap.get(job2).compareTo(jobsMap.get(job1));

                    }
                });

                this.topTenJobs = new ArrayList<String>(JobsActivity.safeSubList(weightedJobs, 0, 10));

                //testing print line
                System.out.println("\n==> Size of Entry list: " + topTenJobs.size());
                for (String temp : topTenJobs) {
                    System.out.println(temp);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }



        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    isStoragePermissionGranted();
                    System.out.println(topTenJobs);
                    return JobsListFragment.newInstance((ArrayList) this.topTenJobs);

                case 1:
                    return BarChartFragment.newInstance(this.barChartAnswersMap);
                case 2:
                    return CategoriesListFragment.newInstance((ArrayList) this.categoriesList);
                case 3:
                    return WebViewFragment.newInstance(new ArrayList<String>(JobsActivity.safeSubList(this.topTenJobs, 0, 3)));
            }
            return null;
        }


        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Top 10 Jobs";
                case 1:
                    return "Bar Chart";
                case 2:
                    return "Top Choices";
                case 3:
                    return "Jobs Shop";
            }
            return null;
        }

        /**

         * check at first runtime for SDK>23 the permissions settings for storage
         * & request authorisation for writing/reading txt file to save data
         **/
        public boolean isStoragePermissionGranted() {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    //    Log.v(TAG, "Permission is granted");
                    return true;
                } else {

                    //   Log.v(TAG, "Permission is revoked");
                    ActivityCompat.requestPermissions(JobsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return false;
                }
            } else { //permission is automatically granted on sdk<23 upon installation
                //  Log.v(TAG, "Permission is granted");
                return true;
            }
        }
    }

    //request user for permissions for storage for SDK>23
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }

    }
}


