package com.example.memory;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.memory.databinding.ActivityMenuGameBinding;
import com.example.memory.game.LeaderboardActivity;
import com.example.memory.game.gameSetting.MenuStartActivity;
import com.example.memory.navigation.BottomNavFragment;
import com.example.memory.navigation.HeaderFragment;
import com.example.memory.utilities.ReadWriteJSON;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements BottomNavFragment.OnFragmentInteractionListener {

    private ActivityMenuGameBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Copier le fichier JSON vers le stockage interne
        ReadWriteJSON readWriteJSONCards = new ReadWriteJSON(getApplicationContext(), "cards.json");
        ReadWriteJSON readWriteJSONLeaderboard = new ReadWriteJSON(getApplicationContext(), "leaderboard.json");

        // TODO : Supprimer les lignes suivantes
//        readWriteJSONCards.setJSON("cards.json");
//        readWriteJSONLeaderboard.setJSON("leaderboard.json");

        binding = ActivityMenuGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarSettings.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_language, R.id.nav_about)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

        binding.startButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuStartActivity.class);
            startActivity(intent);
            finish();
        });

        binding.leaderButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
            finish();
        });

        binding.languageButton.setOnClickListener(v -> {
            if (conf.getLocales().get(0).getLanguage().equals("fr"))
                conf.setLocale(new Locale("en"));
            else if (conf.getLocales().get(0).getLanguage().equals("en"))
                conf.setLocale(new Locale("fr"));
            res.updateConfiguration(conf, res.getDisplayMetrics());
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onPauseGame() {
        // Implement what should happen when the game is paused
    }
}