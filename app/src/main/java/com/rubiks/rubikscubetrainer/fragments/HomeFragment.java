package com.rubiks.rubikscubetrainer.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.rubiks.rubikscubetrainer.R;
import com.rubiks.rubikscubetrainer.activities.CubeGLActivity;
import com.rubiks.rubikscubetrainer.activities.PlayingOptionsActivity;
import com.rubiks.rubikscubetrainer.scanning.ScanningActivity;

// This fragment is responsible for direct users to the playingOptionsActivity activity
// (it's a part of a tool bar)

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
        text.setText("Welcome, " + PlayingOptionsActivity.username + "!");

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanningActivity.class);
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    startActivity(intent);
                else
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 50);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(getContext(), ScanningActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "You must allow your camera permissions in order to scan your cube!\n " +
                    "change it in your phone settings", Toast.LENGTH_LONG).show();
        }

    }
}