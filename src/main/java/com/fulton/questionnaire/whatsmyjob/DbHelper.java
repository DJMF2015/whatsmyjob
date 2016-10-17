package com.fulton.questionnaire.whatsmyjob;

import android.database.Cursor;


import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by David on 12/06/16.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static String reverseDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    //version number and upgrades
    private static final int DATABASE_VERSION = 31;

    // Database Name
  //  private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "questionaireApp";
    // tasks table name
    private static final String TABLE_QUESTIONS = "quest";
    private static final String TABLE_ANSWERS = "ans";
    private static final String TABLE_ATTEMPTS = "attempts";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_QUES = "question";
    private static final String KEY_ANSWER = "answer";
    //answers text e.g) "I really like to clean dishes..."
    private static final String KEY_OPTA = "opta"; //option a
    private static final String KEY_OPTB = "optb"; //option b
    private static final String KEY_OPTC = "optc"; //option c
    private static final String KEY_OPTD = "optd"; //option d
    private static final String OPTA_CATEGORY = "optaTypes"; //option a
    private static final String OPTB_CATEGORY = "optbTypes"; //option b
    private static final String OPTC_CATEGORY = "optcTypes"; //option c
    private static final String OPTD_CATEGORY = "optdTypes"; //option d

    private static final String KEY_ATTEMPT_ID = "att_no";//attemptid


    private static final String KEY_DATETIME = "datetime";//no longer used in app...
    private SQLiteDatabase dbase;
    private Context context;

    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dbase = db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONS + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_QUES + " TEXT, " +
                KEY_ANSWER + " TEXT, " +
                KEY_OPTA + " TEXT, " +
                KEY_OPTB + " TEXT, " +
                KEY_OPTC + " TEXT, " +
                KEY_OPTD + " TEXT, " +
                OPTA_CATEGORY + " TEXT, " +
                OPTB_CATEGORY + " TEXT, " +
                OPTC_CATEGORY + " TEXT, " +
                OPTD_CATEGORY + " TEXT );";

        String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_ANSWERS + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_DATETIME + " DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')) "
                + ");";//DATETIME no longer used but maintained in case of future expansion...

        String sql3 = "CREATE TABLE IF NOT EXISTS " + TABLE_ATTEMPTS + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ATTEMPT_ID + " INTEGER NOT NULL, " +
                KEY_QUES + " INTEGER NOT NULL ," +
                KEY_ANSWER + " TEXT NOT NULL ," +
                KEY_DATETIME + " DATETIME DEFAULT (DATETIME(current_timestamp, 'localtime')) "
                + ");";//DATETIME no longer used but maintained in case of future expansion...

        db.execSQL(sql);
      //  Log.d(TAG, sql);
        db.execSQL(sql2);
    //    Log.d(TAG, sql2);
        db.execSQL(sql3);
     //   Log.d(TAG, sql3);
        addQuestions();

    }
    //load the JSON field and stream in to be read as Strings
    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {

            InputStream is = this.context.getAssets().open(filename + ".json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    //fetches from Json and populates the table with questions
    private void addQuestions() {
        JSONObject questions_data;
        try {
            questions_data = new JSONObject(loadJSONFromAsset("questions"));
            JSONArray questions = questions_data.getJSONArray("questions");
            for (int i = 0; i < questions.length(); i++) {
                JSONObject question_json = questions.getJSONObject(i);
                JSONArray answers = question_json.getJSONArray("answers");

                Question question = new Question(question_json.getString("question"), answers);
                this.addQuestion(question);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        // Create tables again
        onCreate(db);
    }

    // Adding new question
    public void addQuestion(Question quest) {

        ContentValues values = new ContentValues();
        values.put(KEY_QUES, quest.getQUESTION());
        values.put(KEY_ANSWER, quest.getAnswers());
        values.put(KEY_OPTA, quest.getRadioA().text);
        values.put(KEY_OPTB, quest.getRadioB().text);
        values.put(KEY_OPTC, quest.getRadioC().text);
        values.put(KEY_OPTD, quest.getRadioD().text);
        values.put(OPTA_CATEGORY, android.text.TextUtils.join(",", quest.getRadioA().types));
        values.put(OPTB_CATEGORY, android.text.TextUtils.join(",", quest.getRadioB().types));
        values.put(OPTC_CATEGORY, android.text.TextUtils.join(",", quest.getRadioC().types));
        values.put(OPTD_CATEGORY, android.text.TextUtils.join(",", quest.getRadioD().types));
        // Inserting Row
    //    Log.d(TAG, "Adding question");
        dbase.insert(TABLE_QUESTIONS, null, values);
    }
    //for each question and answer attempt insert new rows for each
    public void saveQuestionAndAnswer(Question quest, String answer, Questionnaire questionnaire) {

        ContentValues values = new ContentValues();//insert new value for the questions, answers and attempt by ID
        values.put(KEY_ATTEMPT_ID, questionnaire.getId());
        values.put(KEY_QUES, quest.getID());
        values.put(KEY_ANSWER, answer);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(reverseDateTimeFormat, Locale.ENGLISH);
        values.put(KEY_DATETIME, simpleDateFormat.format(questionnaire.getCreatedTime().getTime()));
        // Inserting Row
      //  Log.d(TAG, "Saving answer");
        Attempt attempt = getSavedAnswerForQuestion(quest, questionnaire);
        if (attempt == null) {
            dbase.insert(TABLE_ATTEMPTS, null, values);
        } else {
            final String SELECTION = KEY_ATTEMPT_ID + "=?" + " and " + KEY_QUES + "=?"; //match question and id of attempt using 'wildcard'
            final String[] SELECTION_ARGS = {String.valueOf(questionnaire.getId()), String.valueOf(quest.getID())};//insert values of selection columns
            dbase.update(TABLE_ATTEMPTS, values, SELECTION, SELECTION_ARGS);//update values in attempt table
        }

    }

    //create arraylist for storing all questions to read from dbase table
    public List<Question> getAllQuestions() {
        List<Question> quesList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS;
        dbase = this.getReadableDatabase();

        Cursor cursor = dbase.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " ORDER BY RANDOM() LIMIT 35", null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) do {
            Question quest = cursorToQuestion(cursor);

            quesList.add(quest);
        } while (cursor.moveToNext());
        else if (cursor.moveToPrevious()) {
            do {
                Question quest = cursorToQuestion(cursor);

                quesList.add(quest);
            } while (cursor.moveToPrevious());
            // return quest list
            return quesList;
        }
        return quesList;
    }

    @NonNull
    ////create arraylist for storing all questions to read from dbase table
    private Question cursorToQuestion(Cursor cursor) {
        Question quest = new Question();
        quest.setID(cursor.getInt(0));
        quest.setQuestions(cursor.getString(1));
        quest.setAnswer(cursor.getString(2));
        List<Answer> answers = new ArrayList<Answer>(4);
        answers.add(new Answer(cursor.getString(3), cursor.getString(7)));
        answers.add(new Answer(cursor.getString(4), cursor.getString(8)));
        answers.add(new Answer(cursor.getString(5), cursor.getString(9)));
        answers.add(new Answer(cursor.getString(6), cursor.getString(10)));
        quest.setAnswers(answers);
        return quest;
    }
    //retrieve cursor object and return data to store in arraylist for the rows to search through
    public List<Questionnaire> getAllAnswers() {
        dbase = this.getReadableDatabase();
        List<Questionnaire> questionnaireList = new ArrayList<>();
        Cursor cursor = dbase.query(TABLE_ANSWERS, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Questionnaire questionnaire = cursorToQuesID(cursor);
                questionnaireList.add(questionnaire);
                cursor.moveToNext();
            }
        }
        return questionnaireList;
    }
    //fetch questionnaire based on id
    private Questionnaire cursorToQuesID(Cursor cursor) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(cursor.getInt(0));
        if (!cursor.isNull(1)) {


        }
        return questionnaire;
    }
    /**
     // fetch dbase query for all entries in questionnaire. Fetch questionnaire ID of first row and increment by +1 to get ID of next row to be stored.
     *If empty, throw exception & set ID to 0 & 'push' new questionnaire.
     *Else, fetch ID of row and increment by +1, then 'push' to the dbase.
     **/
    public Questionnaire createQuestionnaire() {
        int id;
        try {
            Cursor cr = dbase.query(TABLE_ANSWERS, null, null, null, null, null, null);
            cr.moveToLast();
            id = cr.getInt(0) + 1;//for next questionnaire attemp

        } catch (Exception e) {
            id = 1;
        }
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(id);
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(reverseDateTimeFormat, Locale.ENGLISH);
        contentValues.put(KEY_DATETIME, simpleDateFormat.format(questionnaire.getCreatedTime().getTime()));
        dbase.insert(TABLE_ANSWERS, null, contentValues);
       // Log.d(TAG, "Creating questionnaire " + questionnaire.getId());
        return questionnaire;
    }

    //get questionnaire answer row by id.  Return the 1 row as a String
    public Questionnaire getQuest_Answer(int id) {
        dbase = this.getReadableDatabase();
        Questionnaire questionnaire = null;
        Cursor cursor = dbase.query(TABLE_ANSWERS, null, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            questionnaire = cursorToQuesID(cursor);
        } else {

        }

        // return contact
        return questionnaire;
    }
    //return question row by ID. Query dase & check ID in array. Return the 1 row as a String
    public Question getQuestion(int id) {
        dbase = this.getReadableDatabase();
        Question question = null;
        Cursor cursor = dbase.query(TABLE_QUESTIONS, null, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            question = cursorToQuestion(cursor);
        } else {

        }

        // return contact
        return question;
    }

    //all attempts by id
    public List<Attempt> getAllAttemptsInQuestionnaire(int q_id) {
        List<Attempt> attemptList = new ArrayList<>();
        Cursor cursor = dbase.query(TABLE_ATTEMPTS, null, KEY_ATTEMPT_ID + "=?",
                new String[]{String.valueOf(q_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Attempt attempt = cursorToAttempt(cursor);
                attemptList.add(attempt);
                cursor.moveToNext();
            }
        }

        return attemptList;
    }
    //converting data from table into java object
    private Attempt cursorToAttempt(Cursor cursor) {
        Attempt attempt = new Attempt();
        attempt.setId(cursor.getInt(0));
        attempt.setAnswerId(cursor.getInt(1));
        Question question = getQuestion(cursor.getInt(2));
        attempt.setQuestionId(question.getID());
        attempt.setQuestionText(question.getQUESTION());
        attempt.setAnswer(cursor.getString(3));
        if (!cursor.isNull(4)) {
            SimpleDateFormat sdf = new SimpleDateFormat(reverseDateTimeFormat, Locale.ENGLISH);//no longer used but maintained as hardcoded
            Calendar cal = Calendar.getInstance();//no longer used but maintained as hardcoded
            try {
                cal.setTime(sdf.parse(cursor.getString(4)));
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                attempt.setCreatedTime(cal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return attempt;
    }

    public Attempt getSavedAnswerForQuestion(Question question, Questionnaire questionnaire) {
        ContentValues values = new ContentValues();
        values.put(KEY_ATTEMPT_ID, questionnaire.getId());
        values.put(KEY_QUES, question.getID());
        // Inserting Row
     //   Log.d(TAG, "Saving answer");
        final String SELECTION = KEY_ATTEMPT_ID + "=?" + " and " + KEY_QUES + "=?";//query rows based upon questionID and attempt ID
        final String[] SELECTION_ARGS = {String.valueOf(questionnaire.getId()), String.valueOf(question.getID())};//fetch selection for the question/answers & attempt
        Cursor cursor = dbase.query(TABLE_ATTEMPTS, null, SELECTION,
                SELECTION_ARGS, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursorToAttempt(cursor);
        } else {
            return null;
        }

    }
}