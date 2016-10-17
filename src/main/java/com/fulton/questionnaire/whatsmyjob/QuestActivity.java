package com.fulton.questionnaire.whatsmyjob;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;


/**
 * Created by David on 03/06/16.
 */
public class QuestActivity extends AppCompatActivity {
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbHelper = new DbHelper(this);
        dbHelper.getReadableDatabase();
        final List<Questionnaire> questionnaireList = dbHelper.getAllAnswers();


        Button begin = (Button) findViewById(R.id.begin_button);

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(QuestActivity.this, Instructions.class);
                startActivity(intent);
            }
        });

        Button about = (Button) findViewById(R.id.about_btn);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(QuestActivity.this, AboutScreen.class);
                startActivity(intent);
            }

        });

        Button view = (Button) findViewById(R.id.button4);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestActivity.this, SavedJobs.class);

                startActivity(intent);
            }

        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.isActionViewExpanded();
        // handle item selection
        switch (item.getItemId()) {
            case R.id.info:
                information();

                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }


    public void information() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("WhatsMyJob: About")
                .setIcon(R.drawable.info)
                .setMessage(R.string.about1)

                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }


                }).setNegativeButton("", null).show();



    }
}







