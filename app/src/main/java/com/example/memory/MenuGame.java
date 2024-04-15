package com.example.memory;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memory.databinding.ActivityMenuGameBinding;

import java.util.Locale;

public class MenuGame extends AppCompatActivity {

    private ActivityMenuGameBinding binding;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Copier le fichier JSON vers le stockage interne
        ReadWriteJSON readWriteJSON = new ReadWriteJSON(getApplicationContext());
        readWriteJSON.setJSON();

        binding = ActivityMenuGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.buttonsMenuGame, BottomButton.newInstance(getString(R.string.inventory), getString(R.string.shop))).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.headerMenuGame, Header.newInstance(R.drawable.logo_drawable_main, getString(R.string.app_name), getString(R.string.Welcome_Message))).commit();

        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        if (conf.getLocales().get(0).getLanguage().equals("fr"))
            binding.languageButton.setChecked(true);

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

        binding.languageButton.setOnClickListener(v -> {
            if (conf.getLocales().get(0).getLanguage().equals("fr"))
                conf.setLocale(new Locale("en"));
            else if (conf.getLocales().get(0).getLanguage().equals("en"))
                conf.setLocale(new Locale("fr"));
            res.updateConfiguration(conf, res.getDisplayMetrics());
            Intent intent = new Intent(this, MenuGame.class);
            startActivity(intent);
            finish();
        });
    }
}