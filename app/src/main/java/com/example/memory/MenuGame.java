package com.example.memory;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.memory.databinding.ActivityMenuGameBinding;

public class MenuGame extends AppCompatActivity {

    private ActivityMenuGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Copier le fichier JSON vers le stockage interne
        ReadWriteJSON readWriteJSON = new ReadWriteJSON(getApplicationContext());
        readWriteJSON.setJSON();

        binding = ActivityMenuGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.buttonsMenuGame, BottomButton.newInstance(getString(R.string.inventory), getString(R.string.shop))).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.headerMenuGame, Header.newInstance(R.drawable.logo_drawable_main, "Memory", "Bienvenue dans le jeu du memory")).commit();

        binding.startButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuStart.class);
            startActivity(intent);
            finish();
        });

        binding.leaderButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Leaderboard.class);
            startActivity(intent);
            finish();
        });
    }
}