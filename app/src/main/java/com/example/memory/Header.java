package com.example.memory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.memory.databinding.FragmentHeaderBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Header#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Header extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IMAGE = String.valueOf(0);
    private static final String TITLE = "param2";
    private static final String DESCRITION = null;

    // TODO: Rename and change types of parameters
    private int image;
    private String title;
    private String description;

    public Header() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageID     Parameter 1.
     * @param title       Parameter 2.
     * @param description Parameter 3.
     * @return A new instance of fragment Header.
     */
    // TODO: Rename and change types and number of parameters
    public static Header newInstance(int imageID, String title, String description) {
        Header fragment = new Header();
        Bundle args = new Bundle();
        args.putInt(IMAGE, imageID);
        args.putString(TITLE, title);
        args.putString(DESCRITION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getInt(IMAGE);
            title = getArguments().getString(TITLE);
            if(DESCRITION != "null")
                description = getArguments().getString(DESCRITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHeaderBinding binding = FragmentHeaderBinding.inflate(inflater, container, false);
        binding.titleView.setText(title);
        if(description != "null") {
            binding.descriptionView.setText(description);
        }
        binding.imageView.setImageDrawable(getResources().getDrawable(image));
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}