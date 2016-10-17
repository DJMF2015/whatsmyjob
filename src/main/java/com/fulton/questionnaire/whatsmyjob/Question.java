package com.fulton.questionnaire.whatsmyjob;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by David on 03/06/16.
 */

public class Question implements Serializable {
    private int ID;
    private String QUESTION;
    private String ANSWER;

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    private List<Answer> answers;
    public Question()
    {
        ID=0;
        QUESTION="";
        ANSWER="";
        answers = new ArrayList<>();
    }

    public Question(String question, JSONArray json_answers){
        QUESTION = question;
        ANSWER = "";
        answers = new ArrayList<Answer>();
        for(int i=0; i< json_answers.length();i++){
            try {
                answers.add(new Answer(json_answers.getJSONObject(i)))  ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    //getters for all questions and answer options
    public int getID()
    {
        return ID;
    }
    public String getQUESTION() {
        return QUESTION;
    }
    public Answer getRadioA() {
        return answers.get(0);
    }
    public Answer getRadioB() {
        return answers.get(1);
    }
    public Answer getRadioC() {
        return answers.get(2);
    }
    public Answer getRadioD() {
        return answers.get(3);
    }
    public String getAnswers() {
        return ANSWER;
    }
    //setters flor all questions and answer options
    public void setID(int id)
    {
        ID=id;
    }
    public void setQuestions(String Question) {
        QUESTION = Question;
    }
    public void setOptionA(Answer OptionA) {
        answers.set(0,OptionA);
    }
    public void setOptionB(Answer OptionB) {
        answers.set(1,OptionB);
    }
    public void setOptionC(Answer OptionC) {  answers.set(2,OptionC);}
    public void setOptionc(Answer OptionD) { answers.set(3,OptionD) ;}
    public void setAnswer(String answer) {
        ANSWER = answer;
    }
    //get matching answer object using 'equals' method and return correct result if true, else false
    public Answer match(String answer){
        try{
            for (Answer answer1 : answers) {
                if(answer1.equals(answer)){
                    return answer1;
                }
            }
            return new Answer();
        }catch(Exception e){
            return new Answer();
        }
    }
}
