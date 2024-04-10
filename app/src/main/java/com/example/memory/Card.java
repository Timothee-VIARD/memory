package com.example.memory;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.memory.databinding.FragmentCardBinding;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Card#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Card extends Fragment implements Serializable {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NOM = "name";
    private static final String DESCRIPTION = "descrip";
    private static final String PRIX = "price";
    private static final String ACHETE = "bought";
    private static final String RARETE = "Rarity";
    private static final String IMAGE = "image";
    private static final String INVENTORY = "inventory";
    private static final String SELECTED = "selected";
    private static final String DEFAULTCARD = "Card";

    private String nom;
    private String description;
    private String prix;
    private boolean achete;
    private Rarity rarete;
    private String image;
    private FragmentCardBinding binding;
    private OnCardBoughtListener listener;
    private boolean selected = false;
    private boolean inventory = false;
    private static Card currentSelectedCard = null;
    private ReadWriteJSON readWriteJSON;
    private boolean defaultCard;

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
    public static Card newInstance(String name, String image, String price, String description, boolean isBought, Rarity rarity, boolean defaultCard) {
        Card fragment = new Card();
        Bundle args = new Bundle();
        args.putString(NOM, name);
        args.putString(IMAGE, image);
        args.putString(PRIX, price);
        args.putString(DESCRIPTION, description);
        args.putBoolean(ACHETE, isBought);
        args.putString(RARETE, String.valueOf(rarity));
        args.putBoolean(DEFAULTCARD, defaultCard);
        fragment.setArguments(args);
        return fragment;
    }

    public static Card newInstance(String name, String image, String price, String description, boolean isBought, Rarity rarity, boolean inventory, boolean selected) {
        Card fragment = new Card();
        Bundle args = new Bundle();
        args.putString(NOM, name);
        args.putString(IMAGE, image);
        args.putString(PRIX, price);
        args.putString(DESCRIPTION, description);
        args.putBoolean(ACHETE, isBought);
        args.putString(RARETE, String.valueOf(rarity));
        args.putBoolean(INVENTORY, inventory);
        args.putBoolean(SELECTED, selected);
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
            rarete = Rarity.valueOf(getArguments().getString(RARETE));
            image = getArguments().getString(IMAGE);
            inventory = getArguments().getBoolean(INVENTORY);
            defaultCard = getArguments().getBoolean(DEFAULTCARD);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        readWriteJSON = new ReadWriteJSON(getContext());
        binding = FragmentCardBinding.inflate(inflater, container, false);
        binding.cardImage.setImageResource(getResources().getIdentifier(image, "drawable", requireActivity().getPackageName()));
        switch (rarete) {
            case UNCOMMON:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.uncommon_cards));
                break;
            case RARE:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.rare_cards));
                break;
            case EPIC:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.epic_cards));
                break;
            case LEGENDARY:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.legendary_cards));
                break;
            case UNIQUE:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.unique_cards));
                break;
            default:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.common_cards));
                break;
        }
        if (achete && !inventory) {
            binding.cardImageObtain.setBackground(getResources().getDrawable(R.drawable.obtained_card));
            binding.obtainCard.setText(getString(R.string.obtain));
        }

        if (inventory) {
            binding.selectButton.setVisibility(View.VISIBLE);
            if (getArguments().getBoolean(SELECTED)) {
                setSelected(true);
            }
        } else {
            binding.selectButton.setVisibility(View.GONE);
        }

        binding.cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        if (getArguments() != null && getArguments().getBoolean(INVENTORY)) {
            binding.selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelect(v);
                }
            });
        }
        binding.selectButton.setChecked(selected);
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
        dialog.setContentView(R.layout.card_dialog);

        ImageView dialogImage = dialog.findViewById(R.id.cardImage);
        TextView dialogName = dialog.findViewById(R.id.cardName);
        TextView dialogDescription = dialog.findViewById(R.id.cardDescription);
        TextView dialogPrice = dialog.findViewById(R.id.cardPrice);
        TextView dialogRarity = dialog.findViewById(R.id.cardRarity);
        TextView dialogSelected = dialog.findViewById(R.id.cardSelected);
        Button dialogButton = dialog.findViewById(R.id.returnButton);
        ImageButton dialogClose = dialog.findViewById(R.id.exitButton);

        dialogImage.setImageResource(getResources().getIdentifier(image, "drawable", requireActivity().getPackageName()));
        dialogName.setText(nom);
        dialogRarity.setText(String.valueOf(rarete));
        switch (rarete) {
            case UNCOMMON:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.uncommon_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.uncommon));
                break;
            case RARE:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.rare_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.rare));
                break;
            case EPIC:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.epic_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.epic));
                break;
            case LEGENDARY:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.legendary_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.legendary));
                break;
            case UNIQUE:
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
            if (!inventory) dialogPrice.setText(getString(R.string.obtain));
            else dialogPrice.setVisibility(View.GONE);
            if (selected) dialogSelected.setText(getString(R.string.selected));
            else dialogSelected.setVisibility(View.GONE);
            dialogButton.setText(getString(R.string.returnString));
        } else {
            String price = prix + " €";
            dialogPrice.setText(price);
            dialogButton.setText(getString(R.string.buy));
        }

        changeColor(dialogClose);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(prix, "Free") && dialogButton.getText().equals(getString(R.string.buy))) {
                    dialog.dismiss();
                    if (onBuy()) {
                        dialog.show();
                    }
                } else {
                    setBought();
                    if (listener != null) {
                        listener.onCardBought(Card.this);
                    }
                    dialog.dismiss();
                }
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

    private boolean onBuy() {
        boolean result = false;
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.buy_dialog);

        ImageView dialogImage = dialog.findViewById(R.id.cardImage);
        TextView dialogName = dialog.findViewById(R.id.cardName);
        TextView dialogRarity = dialog.findViewById(R.id.cardRarity);
        TextView dialogPrice = dialog.findViewById(R.id.cardPrice);
        ImageButton dialogClose = dialog.findViewById(R.id.exitButton);
        Button dialogButton = dialog.findViewById(R.id.buyButton);
        TextView dialogError = dialog.findViewById(R.id.errorMessage);
        LinearLayout dialogLayout = dialog.findViewById(R.id.specialCard);
        TextView dialogCondition = dialog.findViewById(R.id.condition);
        CheckBox dialogCheckBox = dialog.findViewById(R.id.specialCheckBox);

        changeColor(dialogClose);

        dialogImage.setImageResource(getResources().getIdentifier(image, "drawable", requireActivity().getPackageName()));
        dialogName.setText(nom);
        dialogRarity.setText(String.valueOf(rarete));
        switch (rarete) {
            case UNCOMMON:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.uncommon_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.uncommon));
                break;
            case RARE:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.rare_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.rare));
                break;
            case EPIC:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.epic_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.epic));
                break;
            case LEGENDARY:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.legendary_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.legendary));
                break;
            case UNIQUE:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.unique_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.unique));
                break;
            default:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.common_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.common));
                break;
        }

        String price = prix + "€";
        dialogPrice.setText(price);

        if(rarete == Rarity.UNIQUE) {
            dialogLayout.setVisibility(View.VISIBLE);
            dialogCondition.setVisibility(View.VISIBLE);
        }

        if(dialogCheckBox.isChecked()) {
            result = true;
        }

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.setVisibility(View.VISIBLE);
                dialog.show();
            }
        });

        dialogCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogCheckBox.isChecked()) {
                    setBought();
                    if (listener != null) {
                        listener.onCardBought(Card.this);
                    }
                    dialog.dismiss();
                }
            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return result;
    }

    private void onSelect(View view) {
        setSelected(true);
        if (listener != null) {
            listener.onCardSelected(this);
        }
    }

    public String getImage() {
        return getArguments() != null ? getArguments().getString(IMAGE) : null;
    }

    public String getName() {
        return getArguments() != null ? getArguments().getString(NOM) : null;
    }

    public String getDescription() {
        return getArguments() != null ? getArguments().getString(DESCRIPTION) : null;
    }

    public String getPrice() {
        return getArguments() != null ? getArguments().getString(PRIX) : null;
    }

    public boolean getIsBought() {
        return getArguments() != null && getArguments().getBoolean(ACHETE);
    }

    public Rarity getRarity() {
        return Rarity.valueOf(getArguments() != null ? getArguments().getString(RARETE) : null);
    }

    public boolean getInventory() {
        return getArguments() != null && getArguments().getBoolean(INVENTORY);
    }

    public boolean getSelected() {
        return getArguments() != null && getArguments().getBoolean(SELECTED);
    }

    public boolean getDefaultCard() {
        return getArguments() != null && getArguments().getBoolean(DEFAULTCARD);
    }

    private void changeColor(ImageButton dialogClose) {
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

    public void setSelected(boolean selected) {
        // Si une nouvelle carte est sélectionnée
        if (selected && (currentSelectedCard == null || !currentSelectedCard.equals(this))) {
            // Désélectionner la carte précédemment sélectionnée
            if (currentSelectedCard != null) {
                currentSelectedCard.clearSelected();
            }
            // Mettre à jour la carte actuellement sélectionnée
            currentSelectedCard = this;
        }
        editButtontStatus(true);
    }

    public void clearSelected() {
        if (binding != null) {
            binding.selectButton.setChecked(false);
            editButtontStatus(false);
        }
    }

    public void editButtontStatus(boolean selected) {
        this.selected = selected;
        Bundle args = getArguments();
        if (args != null) {
            args.putBoolean(SELECTED, selected);
            setArguments(args);
        }
        if (readWriteJSON != null) {
            readWriteJSON.editJSON(getName(), getIsBought(), selected);
        }
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
            binding.obtainCard.setText(getString(R.string.obtain));
            setSelected(true);
        }
    }
}