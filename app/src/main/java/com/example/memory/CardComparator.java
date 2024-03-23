package com.example.memory;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card c1, Card c2) {
        return c1.getRarity().compareTo(c2.getRarity());
    }
}