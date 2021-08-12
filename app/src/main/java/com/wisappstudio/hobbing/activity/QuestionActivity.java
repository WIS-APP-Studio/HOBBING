package com.wisappstudio.hobbing.activity;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.hobbing.R;

public class QuestionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Spinner
        Spinner questionSpinner = (Spinner)findViewById(R.id.spinner_question);
        ArrayAdapter questionAdapter = ArrayAdapter.createFromResource(this,
                R.array.question_list, android.R.layout.simple_spinner_dropdown_item);
        questionSpinner.setAdapter(questionAdapter);
    }
}
