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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private CardFragment card1 = null;
    private CardFragment card2 = null;
    private CardFragment card3 = null;

    public TripleCardsFragment() {
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
    public static TripleCardsFragment newInstance(CardFragment card1, CardFragment card2, CardFragment card3) {
        TripleCardsFragment fragment = new TripleCardsFragment();
    // TODO: Rename and change types and number of parameters
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
                card1 = (CardFragment) getArguments().getSerializable("cardA");
            if (getArguments().getSerializable("cardB") != null)
                card2 = (CardFragment) getArguments().getSerializable("cardB");
            if (getArguments().getSerializable("cardC") != null)
                card3 = (CardFragment) getArguments().getSerializable("cardC");
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

    public CardFragment getCard1() {
        return card1;
    }

    public CardFragment getCard2() {
        return card2;
    }

    public CardFragment getCard3() {
        return card3;
    }

    public void setCard1(CardFragment card) {
        card1 = card;
    }
    public void setCard2(CardFragment card) {
        card2 = card;
    }

    public void setCard3(CardFragment card) {
        card3 = card;
    }
}