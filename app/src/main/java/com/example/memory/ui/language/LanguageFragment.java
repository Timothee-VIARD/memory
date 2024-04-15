package com.example.memory.ui.language;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.memory.MenuGame;
import com.example.memory.R;
import com.example.memory.databinding.FragmentLanguageBinding;

import java.util.Locale;

public class LanguageFragment extends Fragment {

    private @NonNull FragmentLanguageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LanguageViewModel galleryViewModel =
                new ViewModelProvider(this).get(LanguageViewModel.class);

        binding = FragmentLanguageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CheckBox checkbox = getView().findViewById(R.id.checkBoxLanguage);
        checkbox.setOnClickListener(v -> {
            Resources res = getResources();
            Configuration conf = res.getConfiguration();

            if (conf.getLocales().get(0).getLanguage().equals("fr")) {
                conf.setLocale(new Locale("en"));
            } else {
                conf.setLocale(new Locale("fr"));
            }
            res.updateConfiguration(conf, res.getDisplayMetrics());
            Intent intent = new Intent(getActivity(), MenuGame.class);
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