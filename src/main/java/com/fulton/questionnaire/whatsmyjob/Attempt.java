package com.fulton.questionnaire.whatsmyjob;


import java.util.Calendar;

/**
 * Created by David on 12/06/16.
 */
//'getters' and setters' of present questionID for returning position (quizID here to differentiate)
//and return the associated answer to a particualar question. Calendar function now redundant and not used.
//Attempt class is used to store which answer is stored for which question and calculate the score
public class Attempt {
    private int answerId;
    private int id;
    private String questionText;

    private Calendar createdTime;//no longer used but maintained
    private int questionId;
    private String answer;

//return ID for question
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//return ID for answer
    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int quesId) {
        this.answerId = quesId;
    }

//return ID for answer
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

//return questionID (position) associated with an answer
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public Calendar getCreatedTime() {
        return createdTime;
    }//no longer used

    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }//no longer used. maintained for future expansion of desired
//return and display question
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}