package com.example.memory;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.memory.databinding.ActivityInventaireBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Inventaire extends AppCompatActivity implements OnCardBoughtListener {
    ActivityInventaireBinding binding;
    private List<TripleCards> fragments;
    private List<Card> cards;

    private ReadWriteJSON readWriteJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readWriteJSON = new ReadWriteJSON();

        readWriteJSON.copyJsonFileToInternalStorage(this);

        binding = ActivityInventaireBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().add(R.id.header, Header.newInstance(R.drawable.logo_drawable_main, "Inventaire")).commit();

        useJSON();

        fragments = new ArrayList<>();

        if (cards.size() % 3 != 0) {
            // Ajoutez des cartes vides pour que le nombre total de cartes soit un multiple de 3
            int emptyCards = 3 - (cards.size() % 3);
            for (int i = 0; i < emptyCards; i++) {
                cards.add(null);
            }
        }

        fragments = new ArrayList<>();

        ArrayList<Card> boughtCards = new ArrayList<>();

        for (Card card : cards) {
            Log.d("card", String.valueOf(card.getIsBought()));
            if (card.getIsBought()) {
                boughtCards.add(card);
            }
        }

        if (boughtCards.size() % 3 != 0) {
            int emptyCards = 3 - (boughtCards.size() % 3);
            for (int i = 0; i < emptyCards; i++) {
                boughtCards.add(null);
            }
        }

        for (int i = 0; i < boughtCards.size(); i += 3) {
            TripleCards tripleCards = TripleCards.newInstance(boughtCards.get(i), boughtCards.get(i + 1), boughtCards.get(i + 2));
            fragments.add(tripleCards);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (TripleCards frag : fragments) {
            ft.add(R.id.cards, frag);
        }
        ft.commit();
    }

    private void useJSON() {
        String jsonString = readWriteJSON.readJSON(this);
        cards = new ArrayList<>();
        try {
            // Create a JSONObject from the JSON string
            JSONObject jsonObject = new JSONObject(jsonString);

            // Get the "cards" array from the JSONObject
            JSONArray jsonArray = jsonObject.getJSONArray("cards");

            // Iterate over the array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject cardObject = jsonArray.getJSONObject(i);

                // Get the name of the card
                String name = cardObject.getString("name");

                // Get the image from the resources
                String image = cardObject.getString("image");

                // Get the price of the card
                String price = cardObject.getString("prix");

                // Get the description of the card
                int descriptionId = getResources().getIdentifier(cardObject.getString("description"), "string", getPackageName());
                String description = getResources().getString(descriptionId);

                // Get the state of the card
                boolean isBought = cardObject.getBoolean("estAchetee");

                // Get the rarity of the card
                String rarity = cardObject.getString("rarete");

                // Use the raw to create a new card
                Card card = Card.newInstance(name, image, price, description, isBought, rarity);

                // Add the card to your list of cards or to your user interface
                cards.add(card);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCardBought(Card card) {
        // Mettez à jour les données des fragments existants
        for (int i = 0; i < fragments.size(); i++) {
            TripleCards tripleCards = fragments.get(i);
            tripleCards.setCard1(updateCard(tripleCards.getCard1()));
            tripleCards.setCard2(updateCard(tripleCards.getCard2()));
            tripleCards.setCard3(updateCard(tripleCards.getCard3()));
        }

        // Utilisez une transaction de fragment pour réattacher tous les fragments
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            ft.attach(fragments.get(i));
        }
        ft.commit();
    }

    private Card updateCard(Card oldCard) {
        // Check if the oldCard is null
        if (oldCard == null) {
            return null;
        }

        // If the card has been bought, create a new card with the updated state
        if (oldCard.getIsBought()) {
            readWriteJSON.editJSON(this, oldCard.getName(), oldCard.getImage(), oldCard.getPrice(), oldCard.getDescription(), true, oldCard.getRarity());
            return Card.newInstance(oldCard.getName(), String.valueOf(oldCard.getImage()), oldCard.getPrice(), oldCard.getDescription(), true, oldCard.getRarity());
        }
        // Otherwise, return the old card
        return oldCard;
    }
}