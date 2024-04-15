package com.example.memory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.memory.databinding.FragmentTripleCardsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripleCards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripleCards extends Fragment {

    private Card card1 = null;
    private Card card2 = null;
    private Card card3 = null;

    public TripleCards() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param card1 First card.
     * @param card2 Second card.
     * @param card3 Third card.
     * @return A new instance of fragment Triple_Cards.
     */
    public static TripleCards newInstance(Card card1, Card card2, Card card3) {
        TripleCards fragment = new TripleCards();
        Bundle args = new Bundle();
        if (card1 != null)
            args.putSerializable("cardA", card1);
        if (card2 != null)
            args.putSerializable("cardB", card2);
        if (card3 != null)
            args.putSerializable("cardC", card3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getSerializable("cardA") != null)
                card1 = (Card) getArguments().getSerializable("cardA");
            if (getArguments().getSerializable("cardB") != null)
                card2 = (Card) getArguments().getSerializable("cardB");
            if (getArguments().getSerializable("cardC") != null)
                card3 = (Card) getArguments().getSerializable("cardC");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTripleCardsBinding binding = FragmentTripleCardsBinding.inflate(inflater, container, false);

        if (savedInstanceState == null) {
            if (card1 != null)
                getParentFragmentManager().beginTransaction().add(R.id.card1, card1, "cardA").commit();
            if (card2 != null)
                getParentFragmentManager().beginTransaction().add(R.id.card2, card2, "cardB").commit();
            if (card3 != null)
                getParentFragmentManager().beginTransaction().add(R.id.card3, card3, "cardC").commit();
        }

        return binding.getRoot();
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
        return card2;
    }

    public Card getCard3() {
        return card3;
    }

    public void setCard1(Card card) {
        card1 = card;
    }
    public void setCard2(Card card) {
        card2 = card;
    }

    public void setCard3(Card card) {
        card3 = card;
    }
}