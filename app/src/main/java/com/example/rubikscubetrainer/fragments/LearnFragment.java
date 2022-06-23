package com.example.rubikscubetrainer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rubikscubetrainer.R;
import com.example.rubikscubetrainer.activities.NotationsActivity;
import com.example.rubikscubetrainer.activities.SolvingMethodsExplanationActivity;

// In this fragment the users can choose if they want to learn about the solving methods or about
// the solving notations of the cube
// (it's a part of a tool bar)

public class LearnFragment extends Fragment {
    private Button solvingMethods;
    private Button notations;
    private View view;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_learn, container, false);
        solvingMethods = view.findViewById(R.id.solvingMethods);
        notations = view.findViewById(R.id.notations);
        solvingMethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SolvingMethodsExplanationActivity.class);
                startActivity(intent);
            }
        });
        notations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NotationsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
