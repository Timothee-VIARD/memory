package com.example.memory.shop;

import java.util.Comparator;

public class CardComparator implements Comparator<CardFragment> {

    /**
     * Compares the rarity of two cards
     * @param c1 card 1
     * @param c2 card 2
     * @return 0 if the rarities are the same, -1 if c1 is more rare, 1 if c2 is more rare
     */
    @Override
    public int compare(CardFragment c1, CardFragment c2) {
        return c1.getRarity().compareTo(c2.getRarity());
    }
}