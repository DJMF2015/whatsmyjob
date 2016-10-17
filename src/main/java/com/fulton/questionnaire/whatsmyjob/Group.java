package com.fulton.questionnaire.whatsmyjob;

/**
 * Created by David Fulton on 12/10/2016.
 */

    import java.util.ArrayList;
    import java.util.List;

    public class Group {

        public String string;
        public final List<String> children = new ArrayList<String>();

        public Group(String string) {
            this.string = string;
        }

    }

