package com.example.memory.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.memory.HomeActivity;
import com.example.memory.R;
import com.example.memory.databinding.FragmentBottomNavBinding;
import com.example.memory.game.GameActivity;
import com.example.memory.shop.InventaireActivity;
import com.example.memory.shop.ShopActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomNavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomNavFragment extends Fragment {

    private String message;
    private String message2;
    private int nbButton; //faire le nombre de bouton, en invisble ou pas ?
    private FragmentBottomNavBinding binding;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of templateButton
     */
    public BottomNavFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param message the message in the button.
     * @return A new instance of fragment TemplateButton.
     */
    public static BottomNavFragment newInstance(String message, String message2) {
        BottomNavFragment fragment = new BottomNavFragment();
        Bundle args = new Bundle();
        args.putString("MESSAGE", message);
        args.putString("MESSAGE2", message2);
        args.putInt("NB_BUTTON", 2);
        fragment.setArguments(args);
        return fragment;
    }


    public static BottomNavFragment newInstance(String message) {
        BottomNavFragment fragment = new BottomNavFragment();
        Bundle args = new Bundle();
        args.putString("MESSAGE", message);
        args.putInt("NB_BUTTON", 1);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void onPauseGame();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        binding = FragmentBottomNavBinding.inflate(inflater, container, false);
        //BottomNavFragment but =
        if (nbButton == 1) {
            binding.button2.setVisibility(View.INVISIBLE);
            binding.button3.setVisibility(View.INVISIBLE);
            binding.button1.setVisibility(View.VISIBLE);
            binding.button1.setText(message);
            binding.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
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
                        intent = new Intent(getActivity(), HomeActivity.class);
                    } else if (message.equals(getString(R.string.inventory))) {
                        intent = new Intent(getActivity(), InventaireActivity.class);
                    } else if (message.equals(getString(R.string.menu))) {
                        intent = new Intent(getActivity(), HomeActivity.class);
                    } else if (message.equals(getString(R.string.pause))) {
                        mListener.onPauseGame();
                    } else if (message.equals(getString(R.string.stop))) {
                        intent = new Intent(getActivity(), HomeActivity.class);
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
                        intent = new Intent(getActivity(), ShopActivity.class);
                    } else if (message2.equals(getString(R.string.start))) {
                        intent = new Intent(getActivity(), GameActivity.class);
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        intent.putExtra("difficulty", sharedPref.getInt("seekBarValue", 0) + 1);
                        intent.putExtra("mode", sharedPref.getString("mode", "Normal"));
                    } else if (message2.equals(getString(R.string.stop))) {
                        intent = new Intent(getActivity(), HomeActivity.class);
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