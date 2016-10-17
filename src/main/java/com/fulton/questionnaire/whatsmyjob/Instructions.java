package com.fulton.questionnaire.whatsmyjob;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by David Fulton on 01/08/2016.
 */
public class Instructions extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_layout);
        Button instructions = (Button) findViewById(R.id.button3);

        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Instructions.this, QuestionnaireActivity.class);
                startActivity(intent);

            }


        });
    }

}








