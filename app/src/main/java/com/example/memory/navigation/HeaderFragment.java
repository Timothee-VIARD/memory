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
    private static final String IMAGE_BACK = String.valueOf(0);
    private static final String TITLE = "param2";
    private static final String DESCRITION = null;

    private int imageBack;
    private String title;
    private String description;

    /**
     * Required empty public constructor
     */
    public HeaderFragment() {
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
        Bundle args = new Bundle();
        args.putInt(IMAGE_BACK, imageID);
        args.putString(TITLE, title);
        args.putString(DESCRITION, description);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     * @param imageID    Parameter 1.
     * @param title     Parameter 2.
     * @return A new instance of fragment HeaderFragment.
     */
    public static HeaderFragment newInstance(int imageID, String title) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_BACK, imageID);
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of a fragment. This is called after onAttach(Activity)
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageBack = getArguments().getInt(IMAGE_BACK);
            title = getArguments().getString(TITLE);
            description = getArguments().getString(DESCRITION);
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
     * @return Return the View for the fragment's UI, or null.
     */
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
        return binding.getRoot();
    }

    /**
     * Change the color of the close button based on the theme
     * @param dialogClose the image
     */
    private void changeColor(ImageView dialogClose) {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            dialogClose.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.primaryLight), PorterDuff.Mode.SRC_IN);
        } else {
            dialogClose.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.primaryDark), PorterDuff.Mode.SRC_IN);
        }
        dialogClose.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));
    }
}