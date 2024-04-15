package com.example.memory.cards;

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

    public GameCard(int id, ImageView imageFlipped, ImageView imageBack) {
        this.id = id;
        this.imageFlipped = imageFlipped;
        this.imageBack = imageBack;
        this.isFlipped = false;
        this.imageShown = imageBack;
    }

    public int getId() {
        return id;
    }

    public ImageView getImage() {
        return imageShown;
    }

    public ImageView getImageFlipped() {
        return imageFlipped;
    }

    public void flip() {
        if (isFlipped) {
            imageShown = imageBack;
        } else {
            imageShown = imageFlipped;
        }
        isFlipped = !isFlipped;
    }
}
