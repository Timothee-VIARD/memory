package com.example.memory.shop;

import java.util.Comparator;

public class CardComparator implements Comparator<CardFragment> {
    @Override
    public int compare(CardFragment c1, CardFragment c2) {
        return c1.getRarity().compareTo(c2.getRarity());
    }
}