package com.example.memory.shop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.memory.R;
import com.example.memory.utilities.ReadWriteJSON;
import com.example.memory.databinding.FragmentCardBinding;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment implements Serializable {
    private static final String NOM = "name";
    private static final String DESCRIPTION = "descrip";
    private static final String PRIX = "price";
    private static final String ACHETE = "bought";
    private static final String RARETE = "Rarity";
    private static final String IMAGE_BACK = "imageBack";
    private static final String INVENTORY = "inventory";
    private static final String SELECTED = "selected";
    private static final String DEFAULTCARD = "CardFragment";

    private String nom;
    private String description;
    private String prix;
    private boolean achete;
    private Rarity rarete;
    private String imageBack;
    private FragmentCardBinding binding;
    private OnCardBoughtListener listener;
    private boolean selected = false;
    private boolean inventory = false;
    private static CardFragment currentSelectedCard = null;
    private ReadWriteJSON readWriteJSON;
    private boolean defaultCard;

    /**
     * Interface to implement the listener for the card bought.
     */
    public CardFragment() {
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
     * @return A new instance of fragment CardFragment.
     */
    public static CardFragment newInstance(String name, String image, String price, String description, boolean isBought, Rarity rarity, boolean defaultCard) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString(NOM, name);
        args.putString(IMAGE_BACK, image);
        args.putString(PRIX, price);
        args.putString(DESCRIPTION, description);
        args.putBoolean(ACHETE, isBought);
        args.putString(RARETE, String.valueOf(rarity));
        args.putBoolean(DEFAULTCARD, defaultCard);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * @param name Name of the card.
     * @param image Image of the card.
     * @param price Price of the card.
     * @param description Description of the card.
     * @param isBought If the card is bought.
     * @param rarity Rarity of the card.
     * @param inventory If the card is in the inventory.
     * @param selected If the card is selected.
     * @return A new instance of fragment CardFragment.
     */
    public static CardFragment newInstance(String name, String image, String price, String description, boolean isBought, Rarity rarity, boolean inventory, boolean selected) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString(NOM, name);
        args.putString(IMAGE_BACK, image);
        args.putString(PRIX, price);
        args.putString(DESCRIPTION, description);
        args.putBoolean(ACHETE, isBought);
        args.putString(RARETE, String.valueOf(rarity));
        args.putBoolean(INVENTORY, inventory);
        args.putBoolean(SELECTED, selected);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nom = getArguments().getString(NOM);
            description = getArguments().getString(DESCRIPTION);
            prix = getArguments().getString(PRIX);
            achete = getArguments().getBoolean(ACHETE);
            rarete = Rarity.valueOf(getArguments().getString(RARETE));
            imageBack = getArguments().getString(IMAGE_BACK);
            inventory = getArguments().getBoolean(INVENTORY);
            defaultCard = getArguments().getBoolean(DEFAULTCARD);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        readWriteJSON = new ReadWriteJSON(getContext(), "cards.json");
        binding = FragmentCardBinding.inflate(inflater, container, false);
        binding.cardImage.setImageResource(getResources().getIdentifier(imageBack, "drawable", requireActivity().getPackageName()));
        switch (rarete) {
            case UNCOMMON:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.outline_uncommon_cards));
                break;
            case RARE:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.outline_rare_cards));
                break;
            case EPIC:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.outline_epic_cards));
                break;
            case LEGENDARY:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.outline_legendary_cards));
                break;
            case UNIQUE:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.outline_unique_cards));
                break;
            default:
                binding.cardImage.setBackground(getResources().getDrawable(R.drawable.outline_common_cards));
                break;
        }
        if (achete && !inventory) {
            binding.cardImageObtain.setBackground(getResources().getDrawable(R.drawable.fill_obtained_card));
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

    /**
     * Use this factory method to create a new instance of
     * @param context The context.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCardBoughtListener) {
            listener = (OnCardBoughtListener) context;
        }
    }

    /**
     * Use this function to show the dialog of the card.
     */
    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = (ViewGroup) getView().findViewById(android.R.id.content);
        View dialog = LayoutInflater.from(this.getContext()).inflate(R.layout.card_dialog, viewGroup, false);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.create();

        ImageView dialogImage = dialog.findViewById(R.id.cardImage);
        TextView dialogName = dialog.findViewById(R.id.cardName);
        TextView dialogDescription = dialog.findViewById(R.id.cardDescription);
        TextView dialogPrice = dialog.findViewById(R.id.cardPrice);
        TextView dialogRarity = dialog.findViewById(R.id.cardRarity);
        TextView dialogSelected = dialog.findViewById(R.id.cardSelected);
        Button dialogButton = dialog.findViewById(R.id.returnButton);
        ImageButton dialogClose = dialog.findViewById(R.id.exitButton);

        dialogImage.setImageResource(getResources().getIdentifier(imageBack, "drawable", requireActivity().getPackageName()));
        dialogName.setText(nom);
        dialogRarity.setText(String.valueOf(rarete));
        switch (rarete) {
            case UNCOMMON:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_uncommon_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.uncommon));
                break;
            case RARE:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_rare_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.rare));
                break;
            case EPIC:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_epic_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.epic));
                break;
            case LEGENDARY:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_legendary_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.legendary));
                break;
            case UNIQUE:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_unique_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.unique));
                break;
            default:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_common_cards));
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
            String price = null;
            if (!Objects.equals(prix, "Free")) price = prix + " €";
            else price = getString(R.string.free);
            dialogPrice.setText(price);
            dialogButton.setText(getString(R.string.buy));
        }

        changeColor(dialogClose);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Objects.equals(prix, "Free") && dialogButton.getText().equals(getString(R.string.buy))) {
                    alertDialog.dismiss();
                    if (onBuy()) {
                        alertDialog.show();
                    }
                } else {
                    setBought();
                    if (listener != null) {
                        listener.onCardBought(CardFragment.this);
                    }
                    alertDialog.dismiss();
                }
            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * Use this function to buy a card.
     * @return The result of the purchase.
     */
    private boolean onBuy() {
        boolean result = false;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = (ViewGroup) getView().findViewById(android.R.id.content);
        View dialog = LayoutInflater.from(this.getContext()).inflate(R.layout.buy_dialog, viewGroup, false);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.create();

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
        EditText dialogCardNumber = dialog.findViewById(R.id.cardNumber);
        EditText dialogCardExpiration = dialog.findViewById(R.id.cardExpiry);
        EditText dialogCardCVV = dialog.findViewById(R.id.cardCVV);
        EditText dialogCardName = dialog.findViewById(R.id.cardHolderName);

        changeColor(dialogClose);

        dialogImage.setImageResource(getResources().getIdentifier(imageBack, "drawable", requireActivity().getPackageName()));
        dialogName.setText(nom);
        dialogRarity.setText(String.valueOf(rarete));
        switch (rarete) {
            case UNCOMMON:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_uncommon_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.uncommon));
                break;
            case RARE:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_rare_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.rare));
                break;
            case EPIC:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_epic_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.epic));
                break;
            case LEGENDARY:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_legendary_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.legendary));
                break;
            case UNIQUE:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_unique_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.unique));
                break;
            default:
                dialogImage.setBackground(getResources().getDrawable(R.drawable.outline_common_cards));
                dialogRarity.setTextColor(getResources().getColor(R.color.common));
                break;
        }

        String price = prix + " €";
        dialogPrice.setText(price);

        dialogCardExpiration.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});

        if (rarete == Rarity.UNIQUE || rarete == Rarity.LEGENDARY) {
            dialogLayout.setVisibility(View.VISIBLE);
            dialogCondition.setVisibility(View.VISIBLE);
        }

        if (dialogCheckBox.isChecked()) {
            result = true;
        }

        dialogCardExpiration.addTextChangedListener(new TextWatcher() {
            private boolean mFormatting;
            private int mAfter;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!mFormatting) {
                    mAfter = after;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mFormatting) {
                    mFormatting = true;
                    if (mAfter != 0 && s.length() == 2) {
                        int month = Integer.parseInt(s.toString());
                        if (month < 1 || month > 12) {
                            s.clear();
                        } else {
                            s.append("/");
                        }
                    } else if (mAfter == 0 && s.length() == 2) {
                        s.delete(1, 2);
                    }
                    mFormatting = false;
                }
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCardNumber.getText().toString().equals("4242424242424242") && dialogCardExpiration.getText().toString().equals("12/42") && dialogCardCVV.getText().toString().equals("424") && dialogCardName.getText().toString().equals("ESEO")) {
                    setBought();
                    if (listener != null) {
                        listener.onCardBought(CardFragment.this);
                    }
                    alertDialog.dismiss();
                } else {
                    dialogError.setVisibility(View.VISIBLE);
                    alertDialog.show();
                }
            }
        });

        dialogCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCheckBox.isChecked()) {
                    setBought();
                    if (listener != null) {
                        listener.onCardBought(CardFragment.this);
                    }
                    alertDialog.dismiss();
                }
            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        return result;
    }

    /**
     * Use this function to select a card.
     * @param view The view.
     */
    private void onSelect(View view) {
        setSelected(true);
        if (listener != null) {
            listener.onCardSelected(this);
        }
    }

    /**
     * Use this function to get the image of the card.
     * @return  The image of the card.
     */
    public String getImage() {
        return getArguments() != null ? getArguments().getString(IMAGE_BACK) : null;
    }

    /**
     * Use this function to get the name of the card.
     * @return The name of the card.
     */
    public String getName() {
        return getArguments() != null ? getArguments().getString(NOM) : null;
    }

    /**
     * Use this function to get the description of the card.
     * @return The description of the card.
     */
    public String getDescription() {
        return getArguments() != null ? getArguments().getString(DESCRIPTION) : null;
    }

    /**
     * Use this function to get the price of the card.
     * @return The price of the card.
     */
    public String getPrice() {
        return getArguments() != null ? getArguments().getString(PRIX) : null;
    }

    /**
     * Use this function to get if the card is bought.
     * @return If the card is bought.
     */
    public boolean getIsBought() {
        return getArguments() != null && getArguments().getBoolean(ACHETE);
    }

    /**
     * Use this function to get the rarity of the card.
     * @return The rarity of the card.
     */
    public Rarity getRarity() {
        return Rarity.valueOf(getArguments() != null ? getArguments().getString(RARETE) : null);
    }

    /**
     * Use this function to get if the card is in the inventory.
     * @return If the card is in the inventory.
     */
    public boolean getInventory() {
        return getArguments() != null && getArguments().getBoolean(INVENTORY);
    }

    /**
     * Use this function to get if the card is selected.
     * @return If the card is selected.
     */
    public boolean getSelected() {
        return getArguments() != null && getArguments().getBoolean(SELECTED);
    }

    /**
     * Use this function to get if the card is the default card.
     * @return If the card is the default card.
     */
    public boolean getDefaultCard() {
        return getArguments() != null && getArguments().getBoolean(DEFAULTCARD);
    }

    /**
     * Use this function to set the color of the exit button.
     * @param dialogClose The dialog close.
     */
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

    /**
     * Use this function to set the card as selected.
     * @param selected If the card is selected.
     */
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

    /**
     * Use this function to set the card as default.
     * @param defaultcard If the card is default.
     */
    public void setDefaultcard(boolean defaultcard) {
        this.defaultCard = defaultcard;
        Bundle args = getArguments();
        if (args != null) {
            args.putBoolean(DEFAULTCARD, defaultcard);
            setArguments(args);
        }
        if (readWriteJSON != null) {
            readWriteJSON.editJSONCard(getName(), getDefaultCard(), defaultcard);
        }

    }

    /**
     * Use this function to clear the selected card.
     */
    public void clearSelected() {
        if (binding != null) {
            binding.selectButton.setChecked(false);
            editButtontStatus(false);
        }
    }

    /**
     * Use this function to edit the button status.
     * @param selected If the button is selected.
     */
    public void editButtontStatus(boolean selected) {
        this.selected = selected;
        Bundle args = getArguments();
        if (args != null) {
            args.putBoolean(SELECTED, selected);
            setArguments(args);
        }
        if (readWriteJSON != null) {
            readWriteJSON.editJSONCard(getName(), getIsBought(), selected);
        }
    }

    /**
     * Use this function to set the card as bought.
     */
    public void setBought() {
        if (!achete) {
            Bundle args = getArguments();
            if (args != null) {
                args.putBoolean(ACHETE, true);
                setArguments(args);
            }
            achete = true;
            binding.cardImageObtain.setBackground(getResources().getDrawable(R.drawable.fill_obtained_card));
            binding.obtainCard.setText(getString(R.string.obtain));
            setSelected(true);
        }
    }
}