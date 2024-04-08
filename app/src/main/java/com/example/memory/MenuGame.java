package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.memory.databinding.ActivityMenuGameBinding;

public class MenuGame extends AppCompatActivity {

    private ActivityMenuGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_game);
    }
}