package com.example.memory.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.memory.R;
import com.example.memory.databinding.FragmentTripleCardsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripleCardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripleCardsFragment extends Fragment {
    private CardFragment card1 = null;
    private CardFragment card2 = null;
    private CardFragment card3 = null;

    /**
     * Empty constructor.
     */
    public TripleCardsFragment() {
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
    public static TripleCardsFragment newInstance(CardFragment card1, CardFragment card2, CardFragment card3) {
        TripleCardsFragment fragment = new TripleCardsFragment();
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

    /**
     * Called when the fragment is being created.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getSerializable("cardA") != null)
                card1 = (CardFragment) getArguments().getSerializable("cardA");
            if (getArguments().getSerializable("cardB") != null)
                card2 = (CardFragment) getArguments().getSerializable("cardB");
            if (getArguments().getSerializable("cardC") != null)
                card3 = (CardFragment) getArguments().getSerializable("cardC");
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    /**
     * Getters and setters for the cards.
     * @return The card 1.
     */
    public CardFragment getCard1() {
        return card1;
    }

    /**
     * Getters and setters for the cards.
     * @return The card 2.
     */
    public CardFragment getCard2() {
        return card2;
    }

    /**
     * Getters and setters for the cards.
     * @return The card 3.
     */
    public CardFragment getCard3() {
        return card3;
    }

    /**
     * Getters and setters for the cards.
     * @param card The card 1.
     */
    public void setCard1(CardFragment card) {
        card1 = card;
    }

    /**
     * Getters and setters for the cards.
     * @param card The card 2.
     */
    public void setCard2(CardFragment card) {
        card2 = card;
    }

    /**
     * Getters and setters for the cards.
     * @param card The card 3.
     */
    public void setCard3(CardFragment card) {
        card3 = card;
    }
}