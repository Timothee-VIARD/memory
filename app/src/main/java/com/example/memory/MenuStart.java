package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.example.memory.databinding.ActivityMenuStartBinding;

import java.util.ArrayList;
import java.util.List;

public class MenuStart extends AppCompatActivity {

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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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


    }
}