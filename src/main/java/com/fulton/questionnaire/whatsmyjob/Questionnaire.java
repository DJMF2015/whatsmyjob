package com.fulton.questionnaire.whatsmyjob;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by David on 12/06/16.
 */

/**
 * Created by David on 12/06/16.
 */

/**
 * this class was originally designed to not only return ID of question but
 * return and set calendar time for tracking number of attempt bu user. ie)1,2,3..etc
 * Reason has not been removed is due to fact it is tightly coupled with dbHelper and QuestionnaireActivity when first designed.
 * Removing these variables would impact upon rest programme and cause errors. But time and attempt no longer
 * displayed to user in a View but required for return of ID
 */
public class Questionnaire {
    public int id;
    private Calendar createdTime;

    public Questionnaire(){
        this.createdTime = new GregorianCalendar();//no longer used but retained until refactored
    }public void setCreatedTime(Calendar createdTime) {
    this.createdTime = createdTime;
   }//no longer used but retained until refactored

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Calendar getCreatedTime() {
    return createdTime;
    }//no longer used but retained until refactored
}

