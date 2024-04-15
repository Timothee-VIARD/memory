package com.example.memory.game;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.example.memory.cards.CardSet;
import com.example.memory.cards.GameCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private CardSet cardSet;
    private int difficulty;
    private Context context;
    private List<GameCard> gameCards;
    private GameCard lastFlippedCard = null;
    private boolean isFlipping = false;
    private GameActivity gameActivity;
    private int NB_FINAL_PAIRS;
    private int pairsFound = 0;
    private int score = 0;
    private int attempts = 0;


    public Game(Context context, int sourceImage, int difficulty, GameActivity gameActivity) {
        this.difficulty = difficulty;
        this.context = context;
        this.cardSet = new CardSet(context, sourceImage, this.difficulty);
        this.NB_FINAL_PAIRS = cardSet.getPairs().size();
        this.gameActivity = gameActivity;
        generateGame();
    }

    public void setGameCards(List<GameCard> gameCards) {
        this.gameCards = gameCards;
    }

    public List<GameCard> getGameCards() {
        return gameCards;
    }

    private void generateGame() {
        List<GameCard> duplicatedGameCards = new ArrayList<>();
        for (GameCard gameCard : cardSet.getGameCards()) {
            duplicatedGameCards.add(gameCard);
            ImageView newImageView = new ImageView(gameCard.getImage().getContext());
            newImageView.setImageDrawable(gameCard.getImageFlipped().getDrawable());
            ImageView imageViewBack = new ImageView(context);
            imageViewBack.setImageDrawable(gameCard.imageBack.getDrawable());
            GameCard newGameCard = new GameCard(gameCard.getId(), newImageView, imageViewBack);
            duplicatedGameCards.add(newGameCard);
        }
        Collections.shuffle(duplicatedGameCards);
        setGameCards(duplicatedGameCards);
    }

    public void flipCard(GameCard gameCard) {
        if (isFlipping) {
            return;
        }
        attempts++;
        if (lastFlippedCard == null) {
            lastFlippedCard = gameCard;
            gameCard.flip();
        } else {
            if (checkMatch(lastFlippedCard, gameCard)) {
                lastFlippedCard = null;
                gameCard.flip();
                pairsFound++;
                checkEndGame();
            } else {
                isFlipping = true;
                gameCard.flip();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameCard.flip();
                        lastFlippedCard.flip();
                        gameActivity.updateUI();
                        lastFlippedCard = null;
                        isFlipping = false;
                    }
                }, 500);
            }
        }
    }

    private boolean checkMatch(GameCard card1, GameCard card2) {
        return card1.getId() == card2.getId();
    }

    private void checkEndGame() {
        if (pairsFound == NB_FINAL_PAIRS) {
            gameActivity.endGame();
        }
    }

    public int getScore() {
        return score;
    }

    public int getAttempts() {
        return attempts;
    }
}
