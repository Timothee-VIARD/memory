package com.example.memory;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memory.databinding.ActivityMenuGameBinding;

public class MenuGame extends AppCompatActivity {

    private ActivityMenuGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Copier le fichier JSON vers le stockage interne
        ReadWriteJSON readWriteJSON = new ReadWriteJSON(getApplicationContext());

        binding = ActivityMenuGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().add(R.id.buttonsMenuGame, BottomButton.newInstance(getString(R.string.inventory), getString(R.string.shop))).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.headerMenuGame, Header.newInstance(R.drawable.logo_drawable_main, "Memory", "Bienvenue dans le jeu du memory")).commit();
    }
}