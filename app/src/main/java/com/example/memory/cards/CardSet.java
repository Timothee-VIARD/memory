package com.example.memory.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.memory.R;
import com.example.memory.cards.CardPairs;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class CardSet {

    private final int ROWS = 3;
    private final int COLS = 5;
    private final int difficulty;
    private final int sourceImage;
    private ImageView sourceImageView;
    private List<CardPairs> pairs;
    private List<GameCard> gameCards = new ArrayList<>();
    private Context context;

    public CardSet(Context context, int sourceImage, int difficulty) {
        this.sourceImage = sourceImage;
        this.difficulty = difficulty;
        this.context = context;
        this.pairs = getPairsForDifficulty(this.difficulty);
        this.sourceImageView = new ImageView(context);
        this.sourceImageView.setImageResource(sourceImage);
        loadImages();
    }

    public ImageView getSourceImageView() {
        return sourceImageView;
    }

    public List<GameCard> getGameCards() {
        return this.gameCards;
    }

    /**
     * Returns the list of pairs.
     */
    public List<CardPairs> getPairs() {
        return pairs;
    }

    /**
     * Returns the number of pairs for a given difficulty.
     *
     * @param difficulty the difficulty
     * @return the list of pairs
     */
    private static List<CardPairs> getPairsForDifficulty(int difficulty) {
        List<CardPairs> pairs = new ArrayList<>();
        for (CardPairs cardPairs : CardPairs.values()) {
            if (cardPairs.getDifficulty() <= difficulty) {
                pairs.add(cardPairs);
            }
        }
        return pairs;
    }

    /**
     * Loads the images.
     */
    public void loadImages() {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), sourceImage);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int chunkWidth = width / COLS;
        int chunkHeight = height / ROWS;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (i*COLS + j >= this.pairs.size()) {
                    break;
                }
                Bitmap chunk = Bitmap.createBitmap(bitmap, j * chunkWidth, i * chunkHeight, chunkWidth, chunkHeight);
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(chunk);
                ImageView imageViewBack = new ImageView(context);
                imageViewBack.setImageResource(R.drawable.zoo_drawable);
                GameCard gameCard = new GameCard(i*COLS + j, imageView,  imageViewBack);
                gameCards.add(gameCard);
            }
        }
    }
}
