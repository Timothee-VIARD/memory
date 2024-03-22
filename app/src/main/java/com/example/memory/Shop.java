package com.example.memory;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.memory.databinding.ActivityShopBinding;

import java.util.ArrayList;
import java.util.List;

public class Shop extends AppCompatActivity implements OnCardBoughtListener {
    ActivityShopBinding binding;
    private List<TripleCards> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().add(R.id.header, Header.newInstance(R.drawable.logo_drawable_main, "Shop", "Buy new cards to improve your deck!")).commit();

        Card card1 = Card.newInstance("Card 1", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 10, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, " +
                "but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages," +
                "and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", false, "legendary");
        Card card2 = Card.newInstance("Card 2", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 20, "This is a basic skin.", false, "common");
        Card card3 = Card.newInstance("Card 3", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 30, "This is a basic skin.", false, "unique");

        Card card4 = Card.newInstance("Card 4", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 40, "This is a basic skin.", false, "rare");
        Card card5 = Card.newInstance("Card 5", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 50, "This is a basic skin.", false, "epic");
        Card card6 = Card.newInstance("Card 6", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 60, "This is a basic skin.", false, "uncommon");

        Card card7 = Card.newInstance("Card 7", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 70, "This is a basic skin.", false, "uncommon");
        Card card8 = Card.newInstance("Card 8", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 80, "This is a basic skin.", false, "common");
        Card card9 = Card.newInstance("Card 9", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 90, "This is a basic skin.", false, "rare");

        Card card10 = Card.newInstance("Card 10", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 100, "This is a basic skin.", false, "epic");
        Card card11 = Card.newInstance("Card 11", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 110, "This is a basic skin.", false, "unique");
        Card card12 = Card.newInstance("Card 12", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 120, "This is a basic skin.", false, "legendary");

        Card card13 = Card.newInstance("Card 13", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 130, "This is a basic skin.", false, "legendary");
        Card card14 = Card.newInstance("Card 14", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 140, "This is a basic skin.", false, "rare");
        Card card15 = Card.newInstance("Card 15", Integer.parseInt(String.valueOf(R.drawable.basicskin_drawable)), 150, "This is a basic skin.", false, "unique");

        fragments = new ArrayList<>();
        fragments.add(TripleCards.newInstance(card1, card2, card3));
        fragments.add(TripleCards.newInstance(card4, card5, card6));
        fragments.add(TripleCards.newInstance(card7, card8, card9));
        fragments.add(TripleCards.newInstance(card10, card11, card12));
        fragments.add(TripleCards.newInstance(card13, card14, card15));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (TripleCards frag : fragments) {
            ft.add(R.id.cards, frag);
        }
        ft.commit();
    }

    @Override
    public void onCardBought(Card card) {
        // Mettez à jour l'état de la carte achetée
        card.setBought();

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
        // Si la carte a été achetée, créez une nouvelle carte avec l'état mis à jour
        if (oldCard.isBought()) {
            return Card.newInstance(oldCard.getName(), oldCard.getImage(), oldCard.getPrice(), oldCard.getDescription(), true, oldCard.getRarity());
        }
        // Sinon, retournez l'ancienne carte
        return oldCard;
    }
}