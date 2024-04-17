package com.example.memory.game.cards;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class GameCard {
    private int id;
    private ImageView imageFlipped;
    public ImageView imageBack;
    public ImageView imageShown;
    public boolean isFlipped;

    /**
     * Constructor for GameCard
     * @param id - id of the card
     * @param imageFlipped - image of the card
     * @param imageBack - image of the back of the card
     */
    public GameCard(int id, ImageView imageFlipped, ImageView imageBack) {
        this.id = id;
        this.imageFlipped = imageFlipped;
        this.imageBack = imageBack;
        this.isFlipped = false;
        this.imageShown = imageBack;
    }

    /**
     * Get the id of the card
     * @return id of the card
     */
    public int getId() {
        return id;
    }

    /**
     * Get the image of the card
     * @return image of the card
     */
    public ImageView getImage() {
        return imageShown;
    }

    /**
     * Get the image of the back of the card
     * @return image of the back of the card
     */
    public ImageView getImageFlipped() {
        return imageFlipped;
    }

    /**
     * Flip the card
     */
    public void flip() {
        if (isFlipped) {
            imageShown = imageBack;
        } else {
            imageShown = imageFlipped;
        }
        isFlipped = !isFlipped;
    }
}
