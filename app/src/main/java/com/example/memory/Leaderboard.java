package com.example.memory;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memory.databinding.ActivityLeaderboardBinding;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    private ActivityLeaderboardBinding binding;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().add(R.id.buttonsLeader, BottomButton.newInstance(getString(R.string.returnString))).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.headerLeader, Header.newInstance(R.drawable.logo_leaderboard_drawable, getString(R.string.leaderboard), getString(R.string.Welcome_Message))).commit();
        spinner = findViewById(R.id.spinnerLeader);
        List<String> choice = new ArrayList<>();
        choice.add("Normal");
        choice.add("Contre la Montre");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}