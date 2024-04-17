package com.example.memory.pages.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.memory.R;
import com.example.memory.databinding.FragmentHomeBinding;
import com.example.memory.game.LeaderboardActivity;
import com.example.memory.game.gameSetting.MenuStartActivity;
import com.example.memory.navigation.BottomNavFragment;
import com.example.memory.navigation.HeaderFragment;

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
        getParentFragmentManager().beginTransaction().replace(R.id.buttonsMenuGame, BottomNavFragment.newInstance(getString(R.string.inventory), getString(R.string.shop))).commit();
        getParentFragmentManager().beginTransaction().replace(R.id.headerMenuGame, HeaderFragment.newInstance(R.drawable.logo_drawable_main, getString(R.string.app_name), getString(R.string.Welcome_Message))).commit();

        Button startButton = getView().findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MenuStartActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        Button leaderButton = getView().findViewById(R.id.leaderButton);
        leaderButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LeaderboardActivity.class);
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