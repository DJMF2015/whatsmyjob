package com.fulton.questionnaire.whatsmyjob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class QuestionnaireActivity extends AppCompatActivity{
    //initialisation
 //   LinearLayout rl;
    String colour;
    public int id;
    private int progressUpdate = 0;
    private Handler handler = new Handler();
    ProgressBar progBar;
    List<Question> quesList;
    int qid = 0;
    Question currentQ;
    TextView txtQuestion;
    RadioButton rda, rdb, rdc, rdd;
    Button Next, Back;
    Questionnaire questionnaire;
    DbHelper db;
    Context context;

    protected void onCreate(Bundle SavedInstanceState) {
        //  call the superclass so it can save the view hierarchy state
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        context = getApplicationContext();
        final RelativeLayout pl = (RelativeLayout) findViewById(R.id.p1);
        final TextView TV = (TextView) findViewById(R.id.progress);
        final SeekBar sk = (SeekBar) findViewById(R.id.seekBar);


       this.getSupportActionBar().show();


        db = new DbHelper(this);
        quesList = db.getAllQuestions();//fetch all questions from db
        questionnaire = db.createQuestionnaire(); ////start new questionnaire session
        txtQuestion = (TextView) findViewById(R.id.textView1);

        rda = (RadioButton) findViewById(R.id.radio0);
        rdb = (RadioButton) findViewById(R.id.radio1);
        rdc = (RadioButton) findViewById(R.id.radio2);
        rdd = (RadioButton) findViewById(R.id.radio3);
        Back = (Button) findViewById(R.id.button2);
        Next = (Button) findViewById(R.id.button1);
        setQuestionView(db, questionnaire);


        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int p = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (p < 5) {
                    p = 25;
                    sk.setProgress(p);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                p = progress;
                txtQuestion.setTextSize(p);
                rda.setTextSize(p);
                rdb.setTextSize(p);
                rdc.setTextSize(p);
                rdd.setTextSize(p);
            }
        });


//move to the next question when user selects 'Next' button
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
                progBar.setMax(35);
                //  Create new layout parameters for progress bar
                AppBarLayout.LayoutParams lp = new AppBarLayout.LayoutParams(
                        200, // Width pixels
                        AppBarLayout.LayoutParams.WRAP_CONTENT // Height of progress bar
                );
                //Apply the layout parameters for progress bar
                progBar.setLayoutParams(lp);
                // Get the progress bar layout parameters
                AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) progBar.getLayoutParams();
                // Apply the layout rule for progress bar
                progBar.setLayoutParams(params);
                // Finally,  add the progress bar to layout
                pl.addView(progBar);
                //initialise radiogroup
                RadioGroup grp = (RadioGroup) findViewById(R.id.radioGroup1);
                if (grp.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(context, "Choose an option!", Toast.LENGTH_SHORT).show();//if no option chosen by user
                    return;
                }
                RadioButton answer = (RadioButton) findViewById(grp.getCheckedRadioButtonId());

                // Log.d("yourans", currentQ.getAnswers() + " " + answer.getText());
                db.saveQuestionAndAnswer(currentQ, (String) answer.getText(), questionnaire);//fetch the question and corresponding answers from db

                if (qid < quesList.size() - 1) {
                    qid++;//next question
                    update();
                    setQuestionView(db, questionnaire);//return/update questionid
                } else {
                    Intent intent = new Intent(QuestionnaireActivity.this, JobsActivity.class);//if no more questions (35 answered), start JobsActivity
                    Bundle b = new Bundle();

                    b.putInt("key", questionnaire.getId());
                    intent.putExtras(b); //pass key from intent to intent for results
                    startActivity(intent);//start activity
                    finish();
                }

            }
        });

    }
    public void update() {
        // Update the progress status

        final TextView TV = (TextView) findViewById(R.id.progress);
        progressUpdate += 1;//increment by +1 progress bar status on-screen

        progBar.setProgress(progressUpdate);
        TV.setText("Completed :" + progressUpdate + "/35");

//if back button selected decrement questionID (position) to go back. Then decrement progressbar.
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qid > 0) {
                    qid--; // Update the progress status
                    progressUpdate -= 1;
                    progBar.setProgress(progressUpdate);

                    TV.setText("Completed :" + progressUpdate + "/35");
                    goBack();

                }
            }

        });
    }

    private void goBack() {
        if (qid > 0) {
            qid--; // Update the progress status

            progBar.getProgress();
            progressUpdate -= 1;//decrement progressbar if user goes back
            progBar.setProgress(progressUpdate);

            setQuestionView(db, questionnaire);
        } else {
            Toast.makeText(context, "Cannot go back!", Toast.LENGTH_SHORT).show();//if question no. is at zero and user tries to go back.

        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }


    //retrieve current questionId and store in variable. Add the radiobuttons and store position in arraylist
    private void setQuestionView(DbHelper dbHelper, Questionnaire questionnaire) {
        RadioGroup grp = (RadioGroup) findViewById(R.id.radioGroup1);
        grp.clearCheck();
        currentQ = quesList.get(qid);
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);//create arraylist of 4 integers for answers and 'shuffle'

        System.out.println(positions);
        List<String> answers = new ArrayList<>();
        answers.add(currentQ.getRadioA().text);
        answers.add(currentQ.getRadioB().text);
        answers.add(currentQ.getRadioC().text);
        answers.add(currentQ.getRadioD().text);

        List<RadioButton> radioButtons = new ArrayList<>(); //add radiobuttons to arraylist
        radioButtons.add(rda);
        radioButtons.add(rdb);
        radioButtons.add(rdc);
        radioButtons.add(rdd);

/**
 positions contains where an answer would go
 if positions = [3,0,2,1]
 option a is at index 3
 option b is at index 0 and so on
 which means radio buttons(3) gets option a
 radioa  -- option b
 radiob -- option d
 radioc -- option c
 radiod -- option a
 **/
        txtQuestion.setText(currentQ.getQUESTION());

        for (int i = 0; i < positions.size(); i++) {
            Integer position = positions.get(i);
            radioButtons.get(position).setText(answers.get(i));
        }

        //save present question and answer position for calculating score
        Attempt previousAttempt = dbHelper.getSavedAnswerForQuestion(currentQ, questionnaire);
        if (previousAttempt == null) {
            //New attempt
        } else {
            String previousAnswer = previousAttempt.getAnswer();
            Integer position = -1;
            // this is the position where the answer is present. Ensures previous question matches with previous answer
            if (currentQ.getRadioA().equals(previousAnswer)) {
                position = positions.get(0);
            } else if (currentQ.getRadioB().equals(previousAnswer)) {
                position = positions.get(1);
            } else if (currentQ.getRadioC().equals(previousAnswer)) {
                position = positions.get(2);
            } else {
                position = positions.get(3);
            }

            switch (position) {
                case 0:
                    grp.check(R.id.radio0);
                    break;
                case 1:
                    grp.check(R.id.radio1);
                    break;
                case 2:
                    grp.check(R.id.radio2);
                    break;
                default:
                    grp.check(R.id.radio3);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.isActionViewExpanded();
        // handle item selection
        switch (item.getItemId()) {

            case R.id.smaller:
                txtQuestion.setTextSize(18);
                rda.setTextSize(18);//decreases answers text font
                rdb.setTextSize(18);
                rdc.setTextSize(18);
                rdd.setTextSize(18);

                return true;
            case R.id.plus:
                txtQuestion.setTextSize(20);
                rda.setTextSize(20);//increases answers text font
                rdb.setTextSize(20);
                rdc.setTextSize(20);
                rdd.setTextSize(20);
                return true;
            case R.id.return1:
                //handles option to return to main menu screen if user wishes to quit questionnaire
                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Return to main menu")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(QuestionnaireActivity.this, QuestActivity.class);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("No!", null).show();

                return true;
            default:

                return super.onOptionsItemSelected(item);

        }
    }
}




