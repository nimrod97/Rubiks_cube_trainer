package com.example.rubikscubetrainer;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
