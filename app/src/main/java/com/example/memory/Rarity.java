package com.example.memory;

public enum Rarity {
    COMMON(0),     // 0
    UNCOMMON(1),   // 1
    RARE(2),       // 2
    EPIC(3),       // 3
    LEGENDARY(4),  // 4
    UNIQUE(5);     // 5

    private final int order;

    Rarity(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public static Rarity fromString(String rarity) {
        switch (rarity) {
            case "Common":
                return COMMON;
            case "Uncommon":
                return UNCOMMON;
            case "Rare":
                return RARE;
            case "Epic":
                return EPIC;
            case "Legendary":
                return LEGENDARY;
            case "Unique":
                return UNIQUE;
            default:
                return null;
        }
    }
}