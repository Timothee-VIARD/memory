package com.example.memory.game.gameSetting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.example.memory.R;
import com.example.memory.databinding.ActivityMenuStartBinding;
import com.example.memory.navigation.BottomNavFragment;
import com.example.memory.navigation.HeaderFragment;

import java.util.ArrayList;
import java.util.List;

public class MenuStartActivity extends AppCompatActivity implements BottomNavFragment.OnFragmentInteractionListener {

    private ActivityMenuStartBinding binding;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.headerStart, Header.newInstance(R.drawable.logo_drawable_main, getString(R.string.start), getString(R.string.game_select))).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.buttonsStart, BottomButton.newInstance(getString(R.string.returnString), getString(R.string.start))).commit();
        spinner = findViewById(R.id.spinnerStart);
        List<String> choice = new ArrayList<>();
        choice.add(getString(R.string.gamemode1));
        choice.add(getString(R.string.gamemode2));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Récupération de la difficulté choisie dans la seek bar
        SeekBar seekBar = findViewById(R.id.seekBarStart);
        setPreferences(seekBar.getProgress(), spinner.getSelectedItem().toString());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setPreferences(progress, spinner.getSelectedItem().toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //rien
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //rien
            }
        });

        //Récupération du mode de jeu choisi
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setPreferences(seekBar.getProgress(), spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setPreferences(int progress, String mode) {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("seekBarValue", progress);
        editor.putString("mode", mode);
        editor.apply();
    }

    @Override
    public void onPauseGame() {

    }
}