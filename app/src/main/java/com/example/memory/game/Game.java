package com.example.memory.game;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.example.memory.game.cards.CardSet;
import com.example.memory.game.cards.GameCard;

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
    private double scoreMultiplier = 1.0;
    private int successStreak = 0;
    private int failureStreak = 0;

    /**
     * Constructor for the Game class
     * @param context The context of the activity
     * @param difficulty The difficulty of the game
     * @param gameActivity The activity of the game
     */
    public Game(Context context, int difficulty, GameActivity gameActivity) {
        this.difficulty = difficulty;
        this.context = context;
        this.cardSet = new CardSet(context, this.difficulty);
        this.NB_FINAL_PAIRS = cardSet.getPairs().size();
        this.gameActivity = gameActivity;
        generateGame();
    }

    /**
     * Getter for the game cards
     * @return The list of game cards
     */
    public List<GameCard> getGameCards() {
        return gameCards;
    }

    /**
     * Method to generate the game
     */
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
        this.gameCards = duplicatedGameCards;
    }

    /**
     * Method to flip a card
     * @param gameCard The card to flip
     */
    public void flipCard(GameCard gameCard) {
        if (isFlipping) {
            return;
        }
        if (lastFlippedCard == null) {
            lastFlippedCard = gameCard;
            gameCard.flip();
        } else {
            if (checkMatch(lastFlippedCard, gameCard)) {
                lastFlippedCard = null;
                gameCard.flip();
                pairsFound++;
                successStreak++;
                failureStreak = 0;
                if (successStreak > 1) {
                    scoreMultiplier += getSuccessMultiplier();
                }
                score += 100 * scoreMultiplier;
                checkEndGame();
            } else {
                isFlipping = true;
                gameCard.flip();
                successStreak = 0;
                failureStreak++;
                if (failureStreak > 1) {
                    scoreMultiplier -= getFailureMultiplier();
                }
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

    /**
     * Method to check if two cards match
     * @param card1 The first card
     * @param card2 The second card
     * @return True if the cards match, false otherwise
     */
    private boolean checkMatch(GameCard card1, GameCard card2) {
        return card1.getId() == card2.getId();
    }

    /**
     * Method to check if the game has ended
     */
    private void checkEndGame() {
        if (pairsFound == NB_FINAL_PAIRS) {
            gameActivity.endGame();
        }
    }

    /**
     * Getter for the score
     * @return The score of the game
     */
    public int getScore() {
        return score;
    }

    /**
     * Method to update the score
     * @param seconds The time taken to flip the card
     */
    public void updateScore(int seconds) {
        switch (difficulty) {
            case 1:
                if (seconds != 0) {
                    score += 250 / seconds;
                } else {
                    score += 250;
                }
                break;
            case 2:
                if (seconds != 0) {
                    score += 500 / seconds;
                } else {
                    score += 500;
                }
                break;
            case 3:
                if (seconds != 0) {
                    score += 1000 / seconds;
                } else {
                    score += 1000;
                }
                break;
        }
    }

    /**
     * Getter for the score multiplier
     * @return The score multiplier
     */
    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    /**
     * Getter for the card set
     * @return The card set
     */
    private double getSuccessMultiplier() {
        switch (difficulty) {
            case 1:
                return 2.0;
            case 2:
                return 3.0;
            case 3:
                return 4.0;
            default:
                return 1.0;
        }
    }

    /**
     * Getter for the failure multiplier
     * @return The failure multiplier
     */
    private double getFailureMultiplier() {
        switch (difficulty) {
            case 1:
                return 0.1;
            case 2:
                return 0.15;
            case 3:
                return 0.2;
            default:
                return 0.0;
        }
    }
}
