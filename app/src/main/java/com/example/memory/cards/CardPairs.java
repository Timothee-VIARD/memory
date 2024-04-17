package com.example.memory.cards;

public enum CardPairs {
    PAIR_1(1, "pair_1"),
    PAIR_2(1, "pair_2"),
    PAIR_3(1, "pair_3"),
    PAIR_4(1, "pair_4"),
    PAIR_5(1, "pair_5"),
    PAIR_6(2, "pair_6"),
    PAIR_7(2, "pair_7"),
    PAIR_8(2, "pair_8"),
    PAIR_9(2, "pair_9"),
    PAIR_10(2, "pair_10"),
    PAIR_11(3, "pair_11"),
    PAIR_12(3, "pair_12"),
    PAIR_13(3, "pair_13"),
    PAIR_14(3, "pair_14"),
    PAIR_15(3, "pair_15");

    private final int difficulty;
    private final String name;

    CardPairs(int difficulty, String name) {
        this.difficulty = difficulty;
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }
}