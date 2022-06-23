package com.example.rubikscubetrainer.activities;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubikscubetrainer.R;

// This activity is responsible for explaining the users each solving method that used by the app
//The suitable layout for it is the 'activity_solving_methods_explanation'

public class SolvingMethodsExplanationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solving_methods_explanation);
        TextView t1 = (TextView) findViewById(R.id.beginner_algorithm_explanation);
        TextView t2 = (TextView) findViewById(R.id.cfop_algorithm_explanation);
        TextView t3 = (TextView) findViewById(R.id.koecimba_algorithm_explanation);
        t1.setMovementMethod(LinkMovementMethod.getInstance());
        t2.setMovementMethod(LinkMovementMethod.getInstance());
        t3.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
