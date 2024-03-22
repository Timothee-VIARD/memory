package com.example.memory;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.memory.databinding.FragmentCardBinding;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Card#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Card extends Fragment implements Serializable {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NOM = "name";
    private static final String DESCRIPTION = "descrip";
    private static final String PRIX = "price";
    private static final String ACHETE = "bought";
    private static final String RARETE = "rarity";
    private static final String IMAGE = "image";
    private static final String SELECTED = "selected";

    // TODO: Rename and change types of parameters
    private String nom;
    private String description;
    private String prix;
    private boolean achete;
    private String rarete;
    private String image;
    private FragmentCardBinding binding;
    private OnCardBoughtListener listener;
    private boolean selected = false;

    public Card() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name        Name of the card.
     * @param image       Image of the card.
     * @param price       Price of the card.
     * @param description Description of the card.
     * @param isBought    If the card is bought.
     * @param rarity      Rarity of the card.
     * @return A new instance of fragment Card.
     */
    // TODO: Rename and change types and number of parameters
    public static Card newInstance(String name, String image, String price, String description, boolean isBought, String rarity) {
        Card fragment = new Card();
        Bundle args = new Bundle();
        args.putString(NOM, name);
        args.putString(IMAGE, image);
        args.putString(PRIX, price);
        args.putString(DESCRIPTION, description);
        args.putBoolean(ACHETE, isBought);
        args.putString(RARETE, rarity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nom = getArguments().getString(NOM);
            description = getArguments().getString(DESCRIPTION);
            prix = getArguments().getString(PRIX);
            achete = getArguments().getBoolean(ACHETE);
            rarete = getArguments().getString(RARETE);
            image = getArguments().getString(IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCardBinding.inflate(inflater, container, false);
        binding.cardImage.setImageResource(getResources().getIdentifier(image, "drawable", getActivity().getPackageName()));
        switch (rarete) {
            case "uncommon":
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.uncommon_cards));
                break;
            case "rare":
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.rare_cards));
                break;
            case "epic":
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.epic_cards));
                break;
            case "legendary":
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.legendary_cards));
                break;
            case "unique":
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.unique_cards));
                break;
            default:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.common_cards));
                break;
        }
        if (achete) {
            binding.cardImageObtain.setBackground(getResources().getDrawable(R.drawable.obtained_card));
            binding.obtainCard.setText("Possédé");
        }

        binding.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // 3. Assurez-vous que l'activité hôte implémente OnCardBoughtListener
        if (context instanceof OnCardBoughtListener) {
            listener = (OnCardBoughtListener) context;
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);

        ImageView dialogImage = dialog.findViewById(R.id.cardImage);
        TextView dialogName = dialog.findViewById(R.id.cardName);
        TextView dialogDescription = dialog.findViewById(R.id.cardDescription);
        TextView dialogPrice = dialog.findViewById(R.id.cardPrice);
        TextView dialogRarity = dialog.findViewById(R.id.cardRarity);
        Button dialogButton = dialog.findViewById(R.id.returnButton);
        ImageButton dialogClose = dialog.findViewById(R.id.exitButton);

        dialogImage.setImageResource(getResources().getIdentifier(image, "drawable", getActivity().getPackageName()));
        dialogName.setText(nom);
        dialogRarity.setText(rarete);
        switch (rarete) {
            case "uncommon":
                dialogImage.setBackground(getResources().getDrawable(R.drawable.uncommon_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.uncommon));
                break;
            case "rare":
                dialogImage.setBackground(getResources().getDrawable(R.drawable.rare_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.rare));
                break;
            case "epic":
                dialogImage.setBackground(getResources().getDrawable(R.drawable.epic_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.epic));
                break;
            case "legendary":
                dialogImage.setBackground(getResources().getDrawable(R.drawable.legendary_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.legendary));
                break;
            case "unique":
                dialogImage.setBackground(getResources().getDrawable(R.drawable.unique_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.unique));
                break;
            default:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.common_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.common));
                break;
        }
        dialogDescription.setText(description);
        if (achete) {
            dialogPrice.setText("Possédé");
            dialogButton.setText("Retour");
        } else {
            String price = prix + " €";
            dialogPrice.setText(price);
            dialogButton.setText("Acheter");
        }

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBought();
                if (listener != null) {
                    listener.onCardBought(Card.this);
                }
                dialog.dismiss();
                dialog.dismiss();
            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public String getImage() {
        return getArguments().getString(IMAGE);
    }

    public String getName() {
        return getArguments().getString(NOM);
    }

    public String getDescription() {
        return getArguments().getString(DESCRIPTION);
    }

    public String getPrice() {
        return getArguments().getString(PRIX);
    }

    public boolean getIsBought() {
        return getArguments().getBoolean(ACHETE);
    }

    public String getRarity() {
        return getArguments().getString(RARETE);
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setBought() {
        if (!achete) {
            Bundle args = getArguments();
            if (args != null) {
                args.putBoolean(ACHETE, true);
                setArguments(args);
            }
            achete = true;
            binding.cardImageObtain.setBackground(getResources().getDrawable(R.drawable.obtained_card));
            binding.obtainCard.setText("Possédé");
        }
    }
}