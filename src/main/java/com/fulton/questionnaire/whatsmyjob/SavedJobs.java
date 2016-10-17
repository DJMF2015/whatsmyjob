package com.fulton.questionnaire.whatsmyjob;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.widget.ViewFlipper;

/**
 * Created by David Fulton on 05/08/2016.
 */
public class SavedJobs extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
    ViewFlipper viewFlipper;
    Button Next, Previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//reference to xml file

        setContentView(R.layout.savedjobs);
        jobs();

        viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper5);


        Previous = (Button) findViewById(R.id.previous);

        Previous.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                categories();
                viewFlipper.showNext();
            }
        });
    }


    public void jobs() {


        try {

            List<String> jobs = new ArrayList<String>();
            File locationFile = new File("/sdcard/topTenJobs.txt");
            File file = new File(locationFile, "");

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;


                while ((line = br.readLine()) != null) {
                	List<String> _jobs =  Arrays.asList(line.split(","));
                    jobs.addAll(_jobs);

                    // Assuming that 'output' is the id of your TextView

                    //arrayadapter to display array of strings as simple ListView


                }
                br.close();
                ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jobs);
                ListView listView1 = (ListView) findViewById(R.id.listView1);
                listView1.setAdapter(adapter);


              categories();


            } catch (IOException e) {

                e.printStackTrace();
            }


        } catch (Exception e) {
           // Toast.makeText(getBaseContext(), e.getMessage(),
                   // Toast.LENGTH_SHORT).show();


        }
        categories();
    }


    public void categories() {
        try {

            List<String> cats = new ArrayList<String>();
            File locationFile = new File("/sdcard/categories.txt");
            File file = new File(locationFile, "");

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;


                while ((line = br.readLine()) != null) {
                    List<String> _cats =  Arrays.asList(line.split(","));
                    cats.addAll(_cats);

                    // Assuming that 'output' is the id of your TextView

                    //arrayadapter to display array of strings as simple ListView


                }
                br.close();
                ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cats);
                ListView listView2 = (ListView) findViewById(R.id.listView2);
                listView2.setAdapter(adapter);

                //  }

                } catch (IOException e) {

                e.printStackTrace();
            }


        } catch (Exception e) {
        //    Toast.makeText(getBaseContext(), e.getMessage(),
                    //Toast.LENGTH_SHORT).show();


        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        // Locate MenuItem with ShareActionProvider

        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Fetch and store ShareActionProvider

        // Return true to display menu
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.isActionViewExpanded();
        // handle item selection
        switch (item.getItemId()) {


            case R.id.menu_item_share:

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");

                share.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("file:////sdcard/topTenJobs.txt"));
                startActivity(Intent.createChooser(share, "Post Your Top Ten Jobs"));


                return true;
            default:
// If we got here, the user's action was not recognized.
                return super.onOptionsItemSelected(item);

        }
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);

        }
    }


}


/**
 * Intent sharingIntent = new Intent(Intent.ACTION_SEND);
 * Bundle b = new Bundle();
 * <p/>
 * sharingIntent.setType("text/plain");
 * sharingIntent.putExtras(b);
 * <p/>
 * <p/>
 * sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Your top jobs:");
 * sharingIntent.putExtra(Intent.ACTION_PASTE, "");
 * startActivity(Intent.createChooser(sharingIntent, "Share using"));
 **/