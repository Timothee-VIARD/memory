package com.example.memory;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.memory.databinding.FragmentBottomButtonBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomButton#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomButton extends Fragment {

    private String message;
    private String message2;
    private int nbButton; //faire le nombre de bouton, en invisble ou pas ?
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
     * @param message the message in the button.
     * @return A new instance of fragment TemplateButton.
     */
    public static BottomButton newInstance(String message, String message2) {
        BottomButton fragment = new BottomButton();
        Bundle args = new Bundle();
        args.putString("MESSAGE", message);
        args.putString("MESSAGE2", message2);
        args.putInt("NB_BUTTON", 2);
        fragment.setArguments(args);
        return fragment;
    }


    public static BottomButton newInstance(String message) {
        BottomButton fragment = new BottomButton();
        Bundle args = new Bundle();
        args.putString("MESSAGE", message);
        args.putInt("NB_BUTTON", 1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is being created.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString("MESSAGE", null);
            message2 = getArguments().getString("MESSAGE2", null);
            nbButton = getArguments().getInt("NB_BUTTON", 0);

        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomButtonBinding.inflate(inflater, container, false);
        //BottomButton but = 
        if (nbButton == 1) {
            binding.button2.setVisibility(View.INVISIBLE);
            binding.button3.setVisibility(View.INVISIBLE);
            binding.button1.setVisibility(View.VISIBLE);
            binding.button1.setText(message);
            binding.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MenuGame.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        } else {
            binding.button2.setVisibility(View.VISIBLE);
            binding.button3.setVisibility(View.VISIBLE);
            binding.button1.setVisibility(View.INVISIBLE);
            binding.button2.setText(message);
            binding.button3.setText(message2);
            binding.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (message.equals(getString(R.string.returnString))) {
                        intent = new Intent(getActivity(), MenuGame.class);
                    } else if (message.equals(getString(R.string.inventory))) {
                        intent = new Intent(getActivity(), Inventaire.class);
                    } else if (message.equals(getString(R.string.menu))) {
                        intent = new Intent(getActivity(), MenuGame.class);
                    } else if (message.equals(getString(R.string.pause))) {
                        //TODO : generer la dialog box
                    } else if (message.equals(getString(R.string.stop))) {
                        intent = new Intent(getActivity(), MenuGame.class);
                    }
                    if (intent != null) {
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            });
            binding.button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (message2.equals(getString(R.string.shop))) {
                        intent = new Intent(getActivity(), Shop.class);
                    } else if (message2.equals(getString(R.string.start))) {
                        //TODO: remove comment
                        // intent = new Intent(getActivity(), GameActivity.class);
                    } else if (message2.equals(getString(R.string.continuer))) {
                        //TODO: fermer le dialog box
                    } else if (message2.equals(getString(R.string.restart))) {
                        //TODO : regenerer la page
                    } else if (message2.equals(getString(R.string.stop))) {
                        intent = new Intent(getActivity(), MenuGame.class);
                    }
                    if (intent != null) {
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            });
        }

        return binding.getRoot();
    }

}
