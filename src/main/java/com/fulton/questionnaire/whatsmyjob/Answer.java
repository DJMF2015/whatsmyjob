package com.fulton.questionnaire.whatsmyjob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by David on 05/07/16.
 */
public class Answer implements Serializable {
    public String text ;
    public List<String> types;

    public static Answer createAnswer(String text, String types){
        return new Answer(text,types);
    }
    @Override
    public String toString(){
        return text;
    }

    //compare two string objects for the answers to return associated matching pairs (types). inside/outside
    @Override
    public boolean equals(Object answerText){
        return text.equals(answerText.toString());
    }
    Answer(){
        text = "";
        types = new ArrayList<>();//store types in arraylist
    }
    Answer(String text, String types){
        this.text = text;
        this.types = Arrays.asList(types.split(","));// splits respective categories i.e.) inside/outside,team/individual...
    }    //constructor is called. Fetches the answers text from "types"e.g.) inside, outside...
    Answer(JSONObject answer){
        try {
            text = answer.getString("text");
            types = new ArrayList<String>();
            JSONArray jArray =  answer.getJSONArray("types");//fetch types from JSON array
            if (jArray != null) {
                for (int i=0;i<jArray.length();i++){
                    types.add(jArray.get(i).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
