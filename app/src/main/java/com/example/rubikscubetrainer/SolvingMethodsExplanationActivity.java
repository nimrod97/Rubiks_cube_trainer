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
        TextView t = (TextView) findViewById(R.id.cfop_algorithm_explanation);
        t.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
