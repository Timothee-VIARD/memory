package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.memory.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private List<BottomButton> buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        buttons = new ArrayList<>();
        buttons.add(BottomButton.newInstance("Button 1","Button 2"));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (BottomButton button : buttons) {
            transaction.add(binding.fragmentContainer.getId(), button);
        }
        transaction.commit();
    }
}