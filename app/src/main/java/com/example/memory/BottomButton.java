package com.example.memory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.memory.databinding.FragmentBottomButtonBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomButton#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomButton extends Fragment {

    private String message;
    private FragmentBottomButtonBinding binding;

    /**
     * Use this factory method to create a new instance of templateButton
     */
    public BottomButton() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TemplateButton.
     */
    public static BottomButton newInstance(String param1) {
        BottomButton fragment = new BottomButton();
        Bundle args = new Bundle();
        args.putString("MESSAGE", param1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is being created.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString("MESSAGE", "");

        }
    }

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
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBottomButtonBinding.inflate(inflater, container, false);
        binding.button.setText(message);
        return binding.getRoot();
    }
}