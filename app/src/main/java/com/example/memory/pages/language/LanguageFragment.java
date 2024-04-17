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

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLanguageBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
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

    /**
     * This method is called when the view previously created by {@link #onCreateView}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}