package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

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
        getSupportFragmentManager().beginTransaction().replace(R.id.headerStart, Header.newInstance(R.drawable.logo_drawable_main, "Start",
                "Choisissez votre mode de jeu")).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.buttonsStart, BottomButton.newInstance("Retour", "Start")).commit();
        spinner = findViewById(R.id.spinnerStart);
        List choice = new ArrayList();
        choice.add("Facile");
        choice.add("Moyen");
        choice.add("Difficile");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, choice);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}