package com.example.memory;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memory.databinding.ActivityShopBinding;

public class Shop extends AppCompatActivity {
    ActivityShopBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}