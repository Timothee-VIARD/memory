package com.example.memory.pages.language;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.memory.R;
import com.example.memory.databinding.FragmentLanguageBinding;

import java.util.Locale;

public class LanguageFragment extends Fragment {

    private FragmentLanguageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLanguageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        RadioGroup radioGroup = requireView().findViewById(R.id.radioGroupLanguage);
        if(conf.getLocales().get(0).getLanguage().equals("fr"))
            radioGroup.check(R.id.radioButtonFrench);
        else
            radioGroup.check(R.id.radioButtonEnglish);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonFrench)
                conf.setLocale(Locale.forLanguageTag("fr"));
            else if (checkedId == R.id.radioButtonEnglish)
                conf.setLocale(Locale.forLanguageTag("en"));
            res.updateConfiguration(conf, res.getDisplayMetrics());
            requireActivity().recreate();
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}