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
import com.example.memory.databinding.ActivityShopBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity implements OnCardBoughtListener, BottomNavFragment.OnFragmentInteractionListener {
    ActivityShopBinding binding;
    private List<TripleCardsFragment> fragments;
    private List<CardFragment> cards;
    private ReadWriteJSON readWriteJSON;

    /**
     * This method is called when the activity is first created.
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readWriteJSON = new ReadWriteJSON(getApplicationContext(), "cards.json");
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.header, HeaderFragment.newInstance(R.drawable.logo_shop_drawable, getString(R.string.shop))).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.footer, BottomNavFragment.newInstance(getString(R.string.returnString))).commit();
        cards = useJSON();
        fragments = new ArrayList<>();
        cards.removeIf(CardFragment::getDefaultCard);
        for (int i = 0; i < cards.size(); i++) {
            int randomIndex = (int) (Math.random() * cards.size());
            CardFragment temp = cards.get(i);
            cards.set(i, cards.get(randomIndex));
            cards.set(randomIndex, temp);
        }
        if (cards.size() % 3 != 0) {
            int emptyCards = 3 - (cards.size() % 3);
            for (int i = 0; i < emptyCards; i++) {
                cards.add(null);
            }
        }
        for (int i = 0; i < cards.size(); i += 3) {
            TripleCardsFragment tripleCards = TripleCardsFragment.newInstance(cards.get(i), cards.get(i + 1), cards.get(i + 2));
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
     * This method reads the JSON file and creates a list of cards.
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
                boolean selected = cardObject.getBoolean("default");
                CardFragment card = CardFragment.newInstance(name, imageBack, price, description, isBought, rarity, selected);
                cards.add(card);
            }
            return cards;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is called when a card is bought.
     * @param card the card that is bought
     */
    @Override
    public void onCardBought(CardFragment card) {
        card.setBought();
        readWriteJSON.editJSONCard(card.getName(), true);
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
     * This method is called when a card is selected.
     * @param card the card that is selected
     */
    @Override
    public void onCardSelected(CardFragment card) {
    }

    /**
     * This method is called when the return button is clicked.
     * @param oldCard the old card
     * @return the updated card
     */
    private CardFragment updateCard(CardFragment oldCard) {
        if (oldCard == null) {
            return null;
        }
        if (oldCard.getIsBought()) {
            readWriteJSON.editJSONCard(oldCard.getName(), true);
            return CardFragment.newInstance(oldCard.getName(), String.valueOf(oldCard.getImage()), oldCard.getPrice(), oldCard.getDescription(), true, oldCard.getRarity(), oldCard.getDefaultCard());
        }
        return oldCard;
    }

    /**
     * This method is called when the game is paused.
     */
    @Override
    public void onPauseGame() {
    }
}