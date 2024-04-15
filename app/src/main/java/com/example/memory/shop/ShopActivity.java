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

public class ShopActivity extends AppCompatActivity implements OnCardBoughtListener {
    ActivityShopBinding binding;
    private List<TripleCardsFragment> fragments;
    private List<CardFragment> cards;
    private ReadWriteJSON readWriteJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readWriteJSON = new ReadWriteJSON(getApplicationContext());
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.header, Header.newInstance(R.drawable.logo_shop_drawable, getString(R.string.shop))).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.footer, BottomButton.newInstance(getString(R.string.returnString))).commit();
        cards = useJSON();
        fragments = new ArrayList<>();
        // Supprime la carte par défaut et mettre a jour le nombre de cartes
        cards.removeIf(CardFragment::getDefaultCard);
        // Permet de mettre les cartes aléatoirement
        for (int i = 0; i < cards.size(); i++) {
            int randomIndex = (int) (Math.random() * cards.size());
            CardFragment temp = cards.get(i);
            cards.set(i, cards.get(randomIndex));
            cards.set(randomIndex, temp);
        }
        // Vérifiez si le nombre total de cartes est un multiple de 3
        if (cards.size() % 3 != 0) {
            // Ajoutez des cartes vides pour que le nombre total de cartes soit un multiple de 3
            int emptyCards = 3 - (cards.size() % 3);
            for (int i = 0; i < emptyCards; i++) {
                cards.add(null);
            }
        }
        for (int i = 0; i < cards.size(); i += 3) {
            TripleCardsFragment tripleCards = TripleCardsFragment.newInstance(cards.get(i), cards.get(i + 1), cards.get(i + 2));
            fragments.add(tripleCards);
        }

        //Supprimez les fragments existants dans le conteneur de cartes
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

    private List<CardFragment> useJSON() {
        String jsonString = readWriteJSON.readJSON();
        List<CardFragment> cards = new ArrayList<>();
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
                // Get the image from the resourcesa
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
                // Get default card
                boolean selected = cardObject.getBoolean("default");
                // Use the raw to create a new card
                CardFragment card = CardFragment.newInstance(name, image, price, description, isBought, rarity, selected);
                cards.add(card);
            }
            return cards;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCardBought(CardFragment card) {
        // Mettez à jour l'état de la carte achetée
        card.setBought();

        // Mettez à jour le fichier JSON pour refléter le nouvel état de la carte
        readWriteJSON.editJSON(card.getName(), true);

        // Mettez à jour les données des fragments existants
        for (int i = 0; i < fragments.size(); i++) {
            TripleCardsFragment tripleCards = fragments.get(i);
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
    public void onCardSelected(CardFragment card) {
        // Ne rien faire lorsqu'une carte est sélectionnée
    }

    private CardFragment updateCard(CardFragment oldCard) {
        // Check if the oldCard is null
        if (oldCard == null) {
            return null;
        }

        // If the card has been bought, create a new card with the updated state
        if (oldCard.getIsBought()) {
            readWriteJSON.editJSON(oldCard.getName(), true);
            return CardFragment.newInstance(oldCard.getName(), String.valueOf(oldCard.getImage()), oldCard.getPrice(), oldCard.getDescription(), true, oldCard.getRarity(), oldCard.getDefaultCard());
        }
        // Otherwise, return the old card
        return oldCard;
    }
}