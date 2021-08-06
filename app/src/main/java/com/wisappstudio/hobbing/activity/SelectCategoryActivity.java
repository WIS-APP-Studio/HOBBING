package com.wisappstudio.hobbing.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hobbing.R;

public class SelectCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
    }

    public void click(View view) {
        Button button = (Button) view;
        FrameLayout parentFrameout = (FrameLayout) button.getParent();

        CheckBox checkBox = (CheckBox) parentFrameout.getChildAt(1);

        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
            checkBox.setVisibility(View.INVISIBLE);
        } else {
            checkBox.setChecked(true);
            checkBox.setVisibility(View.VISIBLE);
        }

        Toast.makeText(this, parentFrameout.getContentDescription() , Toast.LENGTH_SHORT).show();
    }

    public void select(View view) {
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkbox4);
        CheckBox checkBox5 = (CheckBox) findViewById(R.id.checkbox5);
        CheckBox checkBox6 = (CheckBox) findViewById(R.id.checkbox6);
        StringBuffer sb = new StringBuffer();

        sb.append(checkBox1.getContentDescription()+"="+checkBox1.isChecked()+"\n");
        sb.append(checkBox2.getContentDescription()+"="+checkBox2.isChecked()+"\n");
        sb.append(checkBox3.getContentDescription()+"="+checkBox3.isChecked()+"\n");
        sb.append(checkBox4.getContentDescription()+"="+checkBox4.isChecked()+"\n");
        sb.append(checkBox5.getContentDescription()+"="+checkBox5.isChecked()+"\n");
        sb.append(checkBox6.getContentDescription()+"="+checkBox6.isChecked());

        Toast.makeText(this, sb.toString().trim(), Toast.LENGTH_SHORT).show();
    }
}