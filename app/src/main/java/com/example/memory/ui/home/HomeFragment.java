package com.example.memory.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.memory.BottomButton;
import com.example.memory.Header;
import com.example.memory.Leaderboard;
import com.example.memory.MenuStart;
import com.example.memory.R;
import com.example.memory.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParentFragmentManager().beginTransaction().replace(R.id.buttonsMenuGame, BottomButton.newInstance(getString(R.string.inventory), getString(R.string.shop))).commit();
        getParentFragmentManager().beginTransaction().replace(R.id.headerMenuGame, Header.newInstance(R.drawable.logo_drawable_main, getString(R.string.app_name), getString(R.string.Welcome_Message))).commit();

        Button startButton = getView().findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MenuStart.class);
            startActivity(intent);
            getActivity().finish();
        });

        Button leaderButton = getView().findViewById(R.id.leaderButton);
        leaderButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Leaderboard.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}