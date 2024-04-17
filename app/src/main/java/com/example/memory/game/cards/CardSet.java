package com.example.memory.game.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.memory.R;
import com.example.memory.utilities.ReadWriteJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardSet {

    private final int ROWS = 3;
    private final int COLS = 5;
    private final int difficulty;
    private int imageFront;
    private int imageBack;
    private ImageView imageFrontView;
    private List<CardPairs> pairs;
    private List<GameCard> gameCards = new ArrayList<>();
    private Context context;

    /**
     * Constructor for the CardSet class.
     * @param context the context
     * @param difficulty the difficulty
     */
    public CardSet(Context context, int difficulty) {
        this.context = context;
        this.difficulty = difficulty;
        this.pairs = getPairsForDifficulty(this.difficulty);
        try {
            loadImages();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the image front.
     * @return the image front
     */
    public ImageView getImageFrontView() {
        return imageFrontView;
    }

    /**
     * Returns the image back.
     * @return the image back
     */
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
    public void loadImages() throws JSONException {
        ReadWriteJSON readWriteJSON = new ReadWriteJSON(context, "cards.json");
        JSONObject cards = new JSONObject(readWriteJSON.readJSON("cards.json"));
        JSONArray jsonArray = cards.getJSONArray("cards");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject card = jsonArray.getJSONObject(i);
            if (card.getString("selected").equals("true")) {
                this.imageFront = context.getResources().getIdentifier(card.getString("imageFront"), "drawable", context.getPackageName());
                this.imageBack = context.getResources().getIdentifier(card.getString("imageBack"), "drawable", context.getPackageName());
                break;
            } else {
                this.imageFront = R.drawable.basic_set;
            }
        }

        this.imageFrontView = new ImageView(context);
        this.imageFrontView.setImageResource(imageFront);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageFront);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int chunkWidth = width / COLS;
        int chunkHeight = height / ROWS;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (i * COLS + j >= this.pairs.size()) {
                    break;
                }
                Bitmap chunk = Bitmap.createBitmap(bitmap, j * chunkWidth, i * chunkHeight, chunkWidth, chunkHeight);
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(chunk);
                ImageView imageViewBack = new ImageView(context);
                imageViewBack.setImageResource(this.imageBack);
                GameCard gameCard = new GameCard(i * COLS + j, imageView, imageViewBack);
                gameCards.add(gameCard);
            }
        }
    }

}
