package com.example.memory;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.memory.databinding.ActivityInventaireBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventaire extends AppCompatActivity implements OnCardBoughtListener {
    ActivityInventaireBinding binding;
    private List<TripleCards> fragments;
    private List<Card> cards;
    private ReadWriteJSON readWriteJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readWriteJSON = new ReadWriteJSON(getApplicationContext());
        binding = ActivityInventaireBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.header, Header.newInstance(R.drawable.logo_inventory_drawable, getString(R.string.inventory))).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.footer, BottomButton.newInstance(getString(R.string.returnString))).commit();
        cards = useJSON();
        fragments = new ArrayList<>();

        List<Card> boughtCards = new ArrayList<>();
        for (Card card : cards) {
            if (card.getIsBought()) {
                boughtCards.add(card);
            }
        }
        // Filtrer les cartes achetées pour supprimer les cartes nulles et les cartes sans rareté
        boughtCards = boughtCards.stream().filter(card -> card != null && card.getRarity() != null).collect(Collectors.toList());
        //  Trier les cartes achetées par rareté
        boughtCards.sort(new CardComparator());
        // Vérifiez si le nombre total de cartes est un multiple de 3
        if (boughtCards.size() % 3 != 0) {
            // Ajoutez des cartes vides pour que le nombre total de cartes soit un multiple de 3
            int emptyCards = 3 - (boughtCards.size() % 3);
            for (int i = 0; i < emptyCards; i++) {
                boughtCards.add(null);
            }
        }
        // Sélectionnez la carte déjà sélectionnée
        for (Card card : boughtCards) {
            if (card != null && card.getSelected()) {
                card.setSelected(true);
            }
        }
        for (int i = 0; i < boughtCards.size(); i += 3) {
            TripleCards tripleCards = TripleCards.newInstance(boughtCards.get(i), boughtCards.get(i + 1), boughtCards.get(i + 2));
            fragments.add(tripleCards);
        }

        //Supprimez les fragments existants dans le conteneur de cartes
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment fragment : fm.getFragments()) {
            fm.beginTransaction().remove(fragment).commit();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (TripleCards frag : fragments) {
            ft.add(R.id.cards, frag);
        }
        ft.commit();
    }

    private List<Card> useJSON() {
        String jsonString = readWriteJSON.readJSON();
        List<Card> cards = new ArrayList<>();
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
                // Get the id description of the card
                int descriptionId = getResources().getIdentifier(cardObject.getString("description"), "string", getPackageName());
                // Get the description of the card by the ID
                String description = getResources().getString(descriptionId);
                // Get the state of the card
                boolean isBought = cardObject.getBoolean("estAchetee");
                // Get the Rarity of the card
                Rarity rarity = Rarity.fromString(cardObject.getString("rarete"));
                // Get the selected state of the card
                boolean selected1 = cardObject.getBoolean("selected");
                // Use the raw to create a new card
                Card card = Card.newInstance(name, image, price, description, isBought, rarity, true, selected1);
                // Add the card to your list of cards or to your user interface
                cards.add(card);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cards;
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

    @Override
    public void onCardSelected(Card card) {
        // Do something when a card is selected

    }

    private Card updateCard(Card oldCard) {
        // Check if the oldCard is null
        if (oldCard == null) {
            return null;
        }
        // If the card has been bought, create a new card with the updated state
        if (oldCard.getIsBought()) {
            readWriteJSON.editJSON(oldCard.getName(), true, oldCard.getSelected());
            return Card.newInstance(oldCard.getName(), String.valueOf(oldCard.getImage()), oldCard.getPrice(), oldCard.getDescription(), true, oldCard.getRarity(), oldCard.getInventory(), oldCard.getSelected());
        }
        // Otherwise, return the old card
        return oldCard;
    }
}