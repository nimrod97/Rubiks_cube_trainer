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
import com.example.rubikscubetrainer.db.LastGamesOfOthers;
import com.example.rubikscubetrainer.db.MostPlay;
import com.example.rubikscubetrainer.db.MyLastGames;
import com.example.rubikscubetrainer.db.SolvedByMyself;
import com.example.rubikscubetrainer.db.SolvingMethodsList;

// In this fragment the users can choose between all the stats activities
// (it's a part of a tool bar)

public class StatsFragment extends Fragment {
    private Button myGames;
    private Button otherGames;
    private Button mostPlay;
    private Button solvedByMyself;
    private Button toughestCubes;
    private View view;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stats, container, false);
        myGames = view.findViewById(R.id.myLastGame);
        otherGames = view.findViewById(R.id.gamesOfOthers);
        mostPlay = view.findViewById(R.id.mostPlaying);
        solvedByMyself = view.findViewById(R.id.solved_myself);
        toughestCubes = view.findViewById(R.id.toughest_cubes);
        myGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyLastGames.class);
                startActivity(intent);
            }
        });
        otherGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LastGamesOfOthers.class);
                startActivity(intent);
            }
        });
        mostPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MostPlay.class);
                startActivity(intent);
            }
        });
        solvedByMyself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SolvedByMyself.class);
                startActivity(intent);
            }
        });
        toughestCubes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SolvingMethodsList.class);
                startActivity(intent);
            }
        });
        return view;
    }
}