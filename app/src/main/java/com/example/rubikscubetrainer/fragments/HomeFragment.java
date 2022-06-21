package com.example.rubikscubetrainer.fragments;

import static com.example.rubikscubetrainer.activities.PlayingOptionsActivity.username;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rubikscubetrainer.activities.CubeGLActivity;
import com.example.rubikscubetrainer.R;
import com.example.rubikscubetrainer.scanning.ScanningActivity;

public class HomeFragment extends Fragment {
    private Button scan;
    private Button play;
    private TextView text;
    private View view;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        scan = view.findViewById(R.id.scan_button);
        play = view.findViewById(R.id.play_in_the_app_button);
        text = view.findViewById(R.id.username);
        text.setText("Welcome, " + username + "!");

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanningActivity.class);
                startActivity(intent);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CubeGLActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
