package com.example.memory;

import android.content.Intent;
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

    /**
     * This method is called when the activity is created.
     * It sets up the navigation drawer and the fragments.
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReadWriteJSON readWriteJSONCards = new ReadWriteJSON(getApplicationContext(), "cards.json");
        ReadWriteJSON readWriteJSONLeaderboard = new ReadWriteJSON(getApplicationContext(), "leaderboard.json");

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


    /**
     * This method is called when the activity is resumed.
     * It does nothing.
     * It is overridden to implement the interface.
     * @see BottomNavFragment.OnFragmentInteractionListener
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * This method is called when the game is paused.
     * It does nothing.
     */
    @Override
    public void onPauseGame() {
    }
}