package com.example.memory.shop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.memory.navigation.BottomNavFragment;
import com.example.memory.navigation.HeaderFragment;
import com.example.memory.R;
import com.example.memory.utilities.ReadWriteJSON;
import com.example.memory.databinding.ActivityInventaireBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventaireActivity extends AppCompatActivity implements OnCardBoughtListener, BottomNavFragment.OnFragmentInteractionListener {
    ActivityInventaireBinding binding;
    private List<TripleCardsFragment> fragments;
    private List<CardFragment> cards;
    private ReadWriteJSON readWriteJSON;

    /**
     * Create the activity
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readWriteJSON = new ReadWriteJSON(getApplicationContext(), "cards.json");
        binding = ActivityInventaireBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.header, HeaderFragment.newInstance(R.drawable.logo_inventory_drawable, getString(R.string.inventory))).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.footer, BottomNavFragment.newInstance(getString(R.string.returnString))).commit();
        cards = useJSON();
        fragments = new ArrayList<>();

        List<CardFragment> boughtCards = new ArrayList<>();
        for (CardFragment card : cards) {
            if (card.getIsBought()) {
                boughtCards.add(card);
            }
        }
        boughtCards = boughtCards.stream().filter(card -> card != null && card.getRarity() != null).collect(Collectors.toList());
        boughtCards.sort(new CardComparator());
        if (boughtCards.size() % 3 != 0) {
            int emptyCards = 3 - (boughtCards.size() % 3);
            for (int i = 0; i < emptyCards; i++) {
                boughtCards.add(null);
            }
        }
        for (CardFragment card : boughtCards) {
            if (card != null && card.getSelected()) {
                card.setSelected(true);
            }
        }
        for (int i = 0; i < boughtCards.size(); i += 3) {
            TripleCardsFragment tripleCards = TripleCardsFragment.newInstance(boughtCards.get(i), boughtCards.get(i + 1), boughtCards.get(i + 2));
            fragments.add(tripleCards);
        }

        FragmentManager fm = getSupportFragmentManager();
        for (Fragment fragment : fm.getFragments()) {
            fm.beginTransaction().remove(fragment).commit();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (TripleCardsFragment frag : fragments) {
            ft.add(R.id.cards, frag);
        }
        ft.commit();
    }

    /**
     * Use the JSON file to get the cards
     * @return the list of cards
     */
    private List<CardFragment> useJSON() {
        String jsonString = readWriteJSON.readJSON("cards.json");
        List<CardFragment> cards = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("cards");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject cardObject = jsonArray.getJSONObject(i);
                String name = cardObject.getString("name");
                String imageBack = cardObject.getString("imageBack");
                String price = cardObject.getString("prix");
                int descriptionId = getResources().getIdentifier(cardObject.getString("description"), "string", getPackageName());
                String description = getResources().getString(descriptionId);
                boolean isBought = cardObject.getBoolean("estAchetee");
                Rarity rarity = Rarity.fromString(cardObject.getString("rarete"));
                boolean selected1 = cardObject.getBoolean("selected");
                CardFragment card = CardFragment.newInstance(name, imageBack, price, description, isBought, rarity, true, selected1);
                cards.add(card);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cards;
    }

    /**
     * When a card is bought, update the inventory
     * @param card the card that was bought by the user
     */
    @Override
    public void onCardBought(CardFragment card) {
        for (int i = 0; i < fragments.size(); i++) {
            TripleCardsFragment tripleCards = fragments.get(i);
            tripleCards.setCard1(updateCard(tripleCards.getCard1()));
            tripleCards.setCard2(updateCard(tripleCards.getCard2()));
            tripleCards.setCard3(updateCard(tripleCards.getCard3()));
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            ft.attach(fragments.get(i));
        }
        ft.commit();
    }

    /**
     * When a card is selected, do nothing
     * @param card the card that was selected by the user
     */
    @Override
    public void onCardSelected(CardFragment card) {
    }

    /**
     * When the user clicks on the return button, go back to the main activity
     * @param oldCard the card that was selected by the user
     * @return the updated card
     */
    private CardFragment updateCard(CardFragment oldCard) {
        if (oldCard == null) {
            return null;
        }
        if (oldCard.getIsBought()) {
            readWriteJSON.editJSONCard(oldCard.getName(), true, oldCard.getSelected());
            return CardFragment.newInstance(oldCard.getName(), String.valueOf(oldCard.getImage()), oldCard.getPrice(), oldCard.getDescription(), true, oldCard.getRarity(), oldCard.getInventory(), oldCard.getSelected());
        }
        return oldCard;
    }

    /**
     * When the user clicks on the return button, go back to the main activity
     */
    @Override
    public void onPauseGame() {
    }
}