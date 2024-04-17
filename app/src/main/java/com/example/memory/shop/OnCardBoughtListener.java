package com.example.memory.shop;

public interface OnCardBoughtListener {
    /**
     * Called when a card is bought
     * @param card the card that was bought
     */
    void onCardBought(CardFragment card);

    /**
     * Called when a card is selected
     * @param card the card that was selected
     */
    void onCardSelected(CardFragment card);
}