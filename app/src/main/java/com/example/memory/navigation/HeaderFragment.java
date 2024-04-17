package com.example.memory.navigation;

import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.memory.R;
import com.example.memory.databinding.FragmentHeaderBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeaderFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IMAGE_BACK = String.valueOf(0);
    private static final String TITLE = "param2";
    private static final String DESCRITION = null;

    private int imageBack;
    private String title;
    private String description;

    public HeaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageID     Parameter 1.
     * @param title       Parameter 2.
     * @param description Parameter 3.
     * @return A new instance of fragment HeaderFragment.
     */
    public static HeaderFragment newInstance(int imageID, String title, String description) {
        HeaderFragment fragment = new HeaderFragment();
    // TODO: Rename and change types and number of parameters
        Bundle args = new Bundle();
        args.putInt(IMAGE_BACK, imageID);
        args.putString(TITLE, title);
        args.putString(DESCRITION, description);
        fragment.setArguments(args);
        return fragment;
    }

    public static HeaderFragment newInstance(int imageID, String title) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_BACK, imageID);
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageBack = getArguments().getInt(IMAGE_BACK);
            title = getArguments().getString(TITLE);
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
        binding.imageView.setImageDrawable(getResources().getDrawable(imageBack));
        if(!title.equals(getString(R.string.app_name)) && !title.equals(getString(R.string.start))) {
            changeColor(binding.imageView);
        }
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void changeColor(ImageView dialogClose) {
        // Test le mode nuit de l'application pour adapter la couleur de la croix de fermeture
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Mode nuit activé, mettre la couleur claire
            dialogClose.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.primaryLight), PorterDuff.Mode.SRC_IN);
        } else {
            // Mode nuit désactivé, mettre la couleur sombre
            dialogClose.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.primaryDark), PorterDuff.Mode.SRC_IN);
        }
        dialogClose.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));
    }
}