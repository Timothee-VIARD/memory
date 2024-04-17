package com.example.memory.game.gameSetting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memory.R;
import com.example.memory.databinding.ActivityMenuStartBinding;
import com.example.memory.navigation.BottomNavFragment;
import com.example.memory.navigation.HeaderFragment;

import java.util.ArrayList;
import java.util.List;

public class MenuStartActivity extends AppCompatActivity implements BottomNavFragment.OnFragmentInteractionListener {

    private ActivityMenuStartBinding binding;
    Spinner spinner;

    /**
     * This method is called when the activity is first created.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.headerStart, HeaderFragment.newInstance(R.drawable.logo_drawable_main, getString(R.string.start), getString(R.string.game_select))).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.buttonsStart, BottomNavFragment.newInstance(getString(R.string.returnString), getString(R.string.start))).commit();
        spinner = findViewById(R.id.spinnerStart);
        List<String> choice = new ArrayList<>();
        choice.add(getString(R.string.gamemode1));
        choice.add(getString(R.string.gamemode2));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SeekBar seekBar = findViewById(R.id.seekBarStart);
                setPreferences(seekBar.getProgress(), spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SeekBar seekBar = findViewById(R.id.seekBarStart);
        setPreferences(seekBar.getProgress(), spinner.getSelectedItem().toString());
        switch (seekBar.getProgress()) {
            case 0:
                binding.seekBarValue.setText(getString(R.string.facile));
                break;
            case 1:
                binding.seekBarValue.setText(getString(R.string.normal));
                break;
            case 2:
                binding.seekBarValue.setText(getString(R.string.difficile));
                break;
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setPreferences(progress, spinner.getSelectedItem().toString());
                switch (progress) {
                    case 0:
                        binding.seekBarValue.setText(getString(R.string.facile));
                        break;
                    case 1:
                        binding.seekBarValue.setText(getString(R.string.normal));
                        break;
                    case 2:
                        binding.seekBarValue.setText(getString(R.string.difficile));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

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

    /**
     * Method to set the preferences of the game.
     */
    private void setPreferences(int progress, String mode) {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("seekBarValue", progress);
        editor.putString("mode", mode);
        editor.apply();
    }

    /**
     * This method is called when the activity is paused.
     */
    @Override
    public void onPauseGame() {
    }
}